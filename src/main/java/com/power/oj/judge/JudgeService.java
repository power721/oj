package com.power.oj.judge;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jfinal.log.Logger;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionModel;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;
import jodd.format.Printf;
import jodd.io.FileNameUtil;
import jodd.io.FileUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class JudgeService {

    public static final Set<PosixFilePermission> FILE_PERMISSIONS = new HashSet<>();

    private static final Logger log = Logger.getLogger(JudgeService.class);
    private static final JudgeService me = new JudgeService();
    private static final ContestService contestService = ContestService.me();
    private static final ProblemService problemService = ProblemService.me();
    private static final SolutionService solutionService = SolutionService.me();
    private static final UserService userService = UserService.me();
    // private static final ExecutorService judgeExecutor = Executors.newSingleThreadExecutor();
    private static final ExecutorService rejudgeExecutor = Executors.newSingleThreadExecutor();
    // TODO: store task in redis with expire time
    private static final Cache<String, RejudgeTask> rejudgeTasks =
        CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();
    // TODO: store token in redis with expire time
    private static final Cache<Integer, String> tokens =
        CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();
    // TODO: store originalResult in redis with expire time
    private static final Cache<Integer, Integer> originalResult =
        CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();

    static {
        FILE_PERMISSIONS.add(PosixFilePermission.OWNER_READ);
        FILE_PERMISSIONS.add(PosixFilePermission.OWNER_WRITE);
        FILE_PERMISSIONS.add(PosixFilePermission.OWNER_EXECUTE);
        FILE_PERMISSIONS.add(PosixFilePermission.GROUP_READ);
        FILE_PERMISSIONS.add(PosixFilePermission.GROUP_WRITE);
        FILE_PERMISSIONS.add(PosixFilePermission.GROUP_EXECUTE);
    }

    private JudgeService() {
    }

    public static JudgeService me() {
        return me;
    }

    public RejudgeTask getRejudgeTask(String key) {
        return rejudgeTasks.getIfPresent(key);
    }

    public boolean isRejudging(String key) {
        return rejudgeTasks.getIfPresent(key) != null;
    }

    public String generateToken(Integer sid) {
        String token = UUID.randomUUID().toString() + "-" + sid;
        tokens.put(sid, token);
        return token;
    }

    public boolean verifyToken(Integer sid, String token) {
        String serverToken = tokens.getIfPresent(sid);
        if (token != null && token.equals(serverToken)) {
            tokens.invalidate(sid);
            return true;
        }
        log.error("server token is" + serverToken + "but judge return token is " + token);
        return false;
    }

    public Integer removeOriginalResult(Integer sid) {
        Integer value = originalResult.getIfPresent(sid);
        originalResult.invalidate(sid);
        return value;
    }

    public void judge(Solution solution) {
        if (solution instanceof SolutionModel) {
            problemService.incSubmission(solution.getPid());
            userService.incSubmission(solution.getUid());
        }

        startJudgeThread(solution, false);
    }

    public void rejudge(Solution solution, boolean deleteTempDir) {
        if (solution instanceof SolutionModel) {
            userService.revertAccepted(solution);
        }

        solution.setResult(ResultType.REJUDGE).setTest(0).setMtime(OjConfig.timeStamp);
        solution.setMemory(0).setTime(0).setError(null).setSystemError(null);
        solution.update();

        startJudgeThread(solution, deleteTempDir);
    }

    private void startJudgeThread(Solution solution, boolean deleteTempDir) {
        synchronized (JudgeAdapter.class) {
            JudgeAdapter judgeThread;
            if (OjConfig.isLinux()) {
                if (OjConfig.judgeVersion != null && OjConfig.judgeVersion.startsWith("v2.")) {
                    judgeThread = new PowerJudgeV2Adapter(solution);
                } else {
                    judgeThread = new PowerJudgeAdapter(solution);
                }
            } else {
                judgeThread = new PojJudgeAdapter(solution);
            }
            RejudgeTask task;
            if (solution.isContest()) {
                task = new RejudgeTask(solution.getCid(), solution.getSid(), RejudgeType.SOLUTION);
            } else {
                task = new RejudgeTask(solution.getSid(), RejudgeType.SOLUTION);
            }
            task.setDeleteTempDir(deleteTempDir);
            rejudgeTasks.put(task.getKey(), task);
            judgeThread.setDeleteTempDir(deleteTempDir);
            // judgeExecutor.execute(judgeThread); // this will store session in the thread
            Thread thread = new Thread(judgeThread);
            thread.start();
        }
    }

    public void cleanRejudge(Solution solution) {
        RejudgeTask task;
        String key;
        File dir = new File(getWorkPath(solution) + solution.getSid());
        if (solution.isContest()) {
            key = RejudgeType.SOLUTION.getKey(solution.getCid(), solution.getSid());
        } else {
            key = RejudgeType.SOLUTION.getKey(solution.getSid());
        }

        task = rejudgeTasks.getIfPresent(key);
        if (task != null) {
            rejudgeTasks.invalidate(key);
            if (task.isDeleteTempDir()) {
                try {
                    FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                    log.error("delete work directory failed!", e);
                }
            }
        }

        tokens.invalidate(solution.getSid());
    }

    public void rejudge(Solution solution) {
        rejudge(solution, false);
    }

    public boolean rejudgeSolution(Integer sid) {
        String key = RejudgeType.SOLUTION.getKey(sid);
        if (containsKey(rejudgeTasks, key)) {
            log.warn("Do not rejudge solution " + sid + " since rejudge this solution is ongoing.");
            return false;
        }

        SolutionModel solution = solutionService.findSolution(sid);
        if (containsKey(rejudgeTasks, RejudgeType.PROBLEM.getKey(solution.getPid()))) {
            log.warn("Do not rejudge solution " + sid + " since rejudge problem is ongoing.");
            return false;
        }

        final RejudgeTask task = new RejudgeTask(sid, RejudgeType.SOLUTION);
        rejudgeTasks.put(task.getKey(), task);
        try {
            problemService.revertAccepted(solution);

            rejudge(solution);
        } catch (Exception e) {
            log.error("rejudge solution " + sid + " failed!", e);
        } finally {
            rejudgeTasks.invalidate(task.getKey());
        }

        return true;
    }

    public boolean rejudgeProblem(final Integer pid) {
        String key = RejudgeType.PROBLEM.getKey(pid);
        if (containsKey(rejudgeTasks, key)) {
            log.warn("Do not rejudge problem " + pid + " since rejudge this problem is ongoing.");
            return false;
        }

        final long startTime = System.currentTimeMillis();
        final RejudgeTask task = new RejudgeTask(pid, RejudgeType.PROBLEM);
        rejudgeTasks.put(task.getKey(), task);
        Thread rejudgeThread = new Thread(() -> {
            try {
                problemService.reset(pid);
                List<SolutionModel> solutionList = solutionService.getSolutionListForProblemRejudge(pid);
                task.setTotal(solutionList.size());

                // TODO lock this problem
                for (SolutionModel solution : solutionList) {
                    rejudge(solution, true);
                    task.increaseCount();
                }
            } catch (Exception e) {
                log.error("rejudge problem " + pid + " failed!", e);
            } finally {
                rejudgeTasks.invalidate(task.getKey());
            }
            log.info("Rejudge problem " + pid + " finished, total judge: " + task.getTotal() + " total time: " + (
                System.currentTimeMillis() - startTime) + " ms");
        });
        rejudgeExecutor.execute(rejudgeThread);
        return true;
    }

    public void rejudgeProblem4Wait(final Integer pid) {
        Thread rejudgeThread = new Thread(() -> {
            List<SolutionModel> solutionList = solutionService.getWaitSolutionListForProblem(pid);

            // TODO lock this problem
            for (SolutionModel solution : solutionList) {
                problemService.revertAccepted(solution);
                rejudge(solution);
            }
        });
        rejudgeExecutor.execute(rejudgeThread);
    }

    public void rejudgeContestSolution(Solution contestSolution) {
        int result = contestSolution.getResult();

        originalResult.put(contestSolution.getSid(), result);

        contestSolution.put("originalResult", result); // for PowerJudgeAdapter

        rejudge(contestSolution);
    }

    public void rejudgeContestSolution(Integer sid) {
        Solution contestSolution = solutionService.findContestSolution(sid);

        rejudgeContestSolution(contestSolution);
    }

    public boolean rejudgeContest(final Integer cid) {
        String key = RejudgeType.CONTEST.getKey(cid);
        if (containsKey(rejudgeTasks, key)) {
            log.warn("Do not rejudge contest " + cid + " since rejudge this contest is ongoing.");
            return false;
        }

        final long startTime = System.currentTimeMillis();
        final RejudgeTask task = new RejudgeTask(cid, RejudgeType.CONTEST);
        rejudgeTasks.put(task.getKey(), task);
        Thread rejudgeThread = new Thread(() -> {
            try {
                FileUtil.deleteDir(getWorkPath(cid));
            } catch (IOException e) {
                if (OjConfig.isDevMode())
                    e.printStackTrace();
                log.error(e.getLocalizedMessage());
            }
            try {
                List<ContestSolutionModel> solutions =
                    Collections.synchronizedList(solutionService.getSolutionListForContest(cid));
                task.setTotal(solutions.size());
                synchronized (solutions) {
                    for (ContestSolutionModel solution : solutions) {
                        rejudgeContestSolution(solution);
                        task.increaseCount();
                    }
                }
            } catch (Exception e) {
                log.error("rejudge contest " + cid + " failed!", e);
            } finally {
                rejudgeTasks.invalidate(task.getKey());
            }
            log.info(
                "Rejudge contest contest " + cid + " finished, total judge: " + task.getTotal() + " total time: " + (
                    System.currentTimeMillis() - startTime) + " ms");
        });
        rejudgeExecutor.execute(rejudgeThread);
        return true;
    }

    public boolean rejudgeContestProblem(final Integer cid, final Integer pid, final Integer num) {
        String key = RejudgeType.CONTEST_PROBLEM.getKey(cid, num);
        if (containsKey(rejudgeTasks, key)) {
            log.warn("Do not rejudge contest problem " + cid + "-" + pid + " since rejudge this problem is ongoing.");
            return false;
        }

        if (containsKey(rejudgeTasks, RejudgeType.CONTEST.getKey(cid))) {
            log.warn("Do not rejudge contest problem " + cid + "-" + pid + " since rejudge this contest is ongoing.");
            return false;
        }

        final long startTime = System.currentTimeMillis();
        final RejudgeTask task = new RejudgeTask(cid, num, RejudgeType.CONTEST_PROBLEM);
        rejudgeTasks.put(key, task);
        Thread rejudgeThread = new Thread(() -> {
            try {
                List<ContestSolutionModel> solutions = contestService.getContestProblemSolutions(cid, pid);
                task.setTotal(solutions.size());

                for (ContestSolutionModel solution : solutions) {
                    rejudgeContestSolution(solution);
                    task.increaseCount();
                }
            } catch (Exception e) {
                log.error("rejudge contest problem " + cid + "-" + pid + " failed!", e);
            } finally {
                rejudgeTasks.invalidate(task.getKey());
            }
            log.info("Rejudge contest problem " + cid + "-" + pid + " finished, total judge: " + task.getTotal()
                + " total time: " + (System.currentTimeMillis() - startTime) + " ms");
        });

        rejudgeExecutor.execute(rejudgeThread);
        return true;
    }

    public int getDataFiles(Integer pid, List<String> inFiles, List<String> outFiles) throws IOException {
        File dataDir = new File(OjConfig.getString("dataPath") + File.separator + pid);
        if (!dataDir.isDirectory()) {
            throw new IOException("Data files does not exist.");
        }

        File[] arrayOfFile = dataDir.listFiles();
        if (arrayOfFile == null) {
            return 0;
        }

        if (arrayOfFile.length > 3) {
            Arrays.sort(arrayOfFile);
        }

        for (File inFile : arrayOfFile) {
            if (!inFile.getName().toLowerCase().endsWith(OjConstants.DATA_EXT_IN)) {
                continue;
            }
            File outFile = new File(dataDir.getAbsolutePath() + File.separator + inFile.getName()
                .substring(0, inFile.getName().length() - OjConstants.DATA_EXT_IN.length()) + OjConstants.DATA_EXT_OUT);
            if (!outFile.isFile()) {
                log.warn(Printf.str("Output file for input file does not exist: %s", inFile.getAbsolutePath()));
                continue;
            }
            inFiles.add(inFile.getAbsolutePath());
            outFiles.add(outFile.getAbsolutePath());
        }

        return inFiles.size();
    }

    public String getWorkPath(Integer cid) {
        return FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")) + File.separator + "c" + cid
            + File.separator;
    }

    public String getWorkPath(Solution solution) {
        String workPath = FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")) + File.separator;
        if (solution.isContest()) {
            workPath = workPath + "c" + solution.getCid() + File.separator;
            File contestDir = new File(workPath);
            if (!contestDir.isDirectory()) {
                if (contestDir.mkdirs()) {
                    try {
                        Files.setPosixFilePermissions(contestDir.toPath(), FILE_PERMISSIONS);
                    } catch (IOException e) {
                        log.error("set file permissions failed!", e);
                    }
                }
            }
        }

        return workPath;
    }

    public String getWorkDirPath(Solution solution) {
        String workPath = FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")) + File.separator;
        if (solution.isContest()) {
            workPath = workPath + "c" + solution.getCid() + File.separator + solution.getSid() + File.separator;
        } else {
            workPath = workPath + File.separator + solution.getSid() + File.separator;
        }

        return workPath;
    }

    private boolean containsKey(Cache<?, ?> cache, Object key) {
        return cache.getIfPresent(key) != null;
    }
}
