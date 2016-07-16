package com.power.oj.judge;

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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class JudgeService {
    private static final Logger log = Logger.getLogger(JudgeService.class);
    private static final JudgeService me = new JudgeService();
    private static final ContestService contestService = ContestService.me();
    private static final ProblemService problemService = ProblemService.me();
    private static final SolutionService solutionService = SolutionService.me();
    private static final UserService userService = UserService.me();
    //private static final ExecutorService judgeExecutor = Executors.newSingleThreadExecutor();
    private static final ExecutorService rejudgeExecutor = Executors.newSingleThreadExecutor();
    private static final ConcurrentHashMap<String, RejudgeTask> rejudgeTasks = new ConcurrentHashMap<>();
    // TODO: store token in redis with expire time
    private static final ConcurrentHashMap<Integer, String> tokens = new ConcurrentHashMap<>();
    public static final Set<PosixFilePermission> FILE_PERMISSIONS = new HashSet<>();

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
        return rejudgeTasks.get(key);
    }

    public boolean isRejudging(String key) {
        return rejudgeTasks.containsKey(key);
    }

    public String generateToken(Integer sid) {
        String token = UUID.randomUUID().toString() + "-" + sid;
        tokens.put(sid, token);
        return token;
    }

    public boolean verifyToken(Integer sid, String token) {
        if (token != null && token.equals(tokens.get(sid))) {
            tokens.remove(sid);
            return true;
        }
        return false;
    }

    public void judge(Solution solution) {
        if (solution instanceof SolutionModel) {
            problemService.incSubmission(solution.getPid());
            userService.incSubmission(solution.getUid());
        }

        synchronized (JudgeAdapter.class) {
            JudgeAdapter judgeThread;
            if (OjConfig.isLinux()) {
                if (OjConfig.judgeVersion != null && OjConfig.judgeVersion.startsWith("v2.")) {
                    judgeThread = new PowerJudgeV2Adapter(solution);
                } else {
                    judgeThread = new PojJudgeAdapter(solution);
                }
            } else {
                judgeThread = new PojJudgeAdapter(solution);
            }
            //judgeExecutor.execute(judgeThread);  // this will store session in the thread
            Thread thread = new Thread(judgeThread);
            thread.start();
        }
    }

    public void rejudge(Solution solution, boolean deleteTempDir) {
        if (solution instanceof SolutionModel) {
            userService.revertAccepted(solution);
        }

        solution.setResult(ResultType.WAIT).setTest(0).setMtime(OjConfig.timeStamp);
        solution.setMemory(0).setTime(0).setError(null).setSystemError(null);
        solution.update();

        synchronized (JudgeAdapter.class) {
            JudgeAdapter judgeThread;
            if (OjConfig.isLinux()) {
                judgeThread = new PowerJudgeV2Adapter(solution);
            } else {
                judgeThread = new PojJudgeAdapter(solution);
            }
            judgeThread.setDeleteTempDir(deleteTempDir);
            //judgeExecutor.execute(judgeThread);  // this will store session in the thread
            Thread thread = new Thread(judgeThread);
            thread.start();
        }
    }

    public void rejudge(Solution solution) {
        rejudge(solution, false);
    }

    public boolean rejudgeSolution(Integer sid) {
        String key = RejudgeType.SOLUTION.getKey(sid);
        if (rejudgeTasks.containsKey(key)) {
            log.warn("Do not rejudge solution " + sid + " since rejudge this solution is ongoing.");
            return false;
        }

        SolutionModel solution = solutionService.findSolution(sid);
        if (rejudgeTasks.containsKey(RejudgeType.PROBLEM.getKey(solution.getPid()))) {
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
            rejudgeTasks.remove(task.getKey());
        }

        return true;
    }

    public boolean rejudgeProblem(final Integer pid) {
        String key = RejudgeType.PROBLEM.getKey(pid);
        if (rejudgeTasks.containsKey(key)) {
            log.warn("Do not rejudge problem " + pid + " since rejudge this problem is ongoing.");
            return false;
        }

        final long startTime = System.currentTimeMillis();
        final RejudgeTask task = new RejudgeTask(pid, RejudgeType.PROBLEM);
        rejudgeTasks.put(task.getKey(), task);
        Thread rejudgeThread = new Thread(new Runnable() {
            @Override
            public void run() {
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
                    rejudgeTasks.remove(task.getKey());
                }
                log.info("Rejudge problem " + pid + " finished, total judge: " + task.getTotal() + " total time: " + (
                    System.currentTimeMillis() - startTime) + " ms");
            }
        });
        rejudgeExecutor.execute(rejudgeThread);
        return true;
    }

    public void rejudgeProblem4Wait(final Integer pid) {
        Thread rejudgeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<SolutionModel> solutionList = solutionService.getWaitSolutionListForProblem(pid);

                // TODO lock this problem
                for (SolutionModel solution : solutionList) {
                    problemService.revertAccepted(solution);
                    rejudge(solution);
                }
            }
        });
        rejudgeExecutor.execute(rejudgeThread);
    }

    public void rejudgeContestSolution(ContestSolutionModel contestSolution) {
        int result = contestSolution.getResult();

        contestSolution.update();

        contestSolution.put("originalResult", result);

        rejudge(contestSolution);
    }

    public void rejudgeContestSolution(Integer sid) {
        ContestSolutionModel contestSolution = solutionService.findContestSolution(sid);

        rejudgeContestSolution(contestSolution);
    }

    public boolean rejudgeContest(final Integer cid) {
        String key = RejudgeType.CONTEST.getKey(cid);
        if (rejudgeTasks.containsKey(key)) {
            log.warn("Do not rejudge contest " + cid + " since rejudge this contest is ongoing.");
            return false;
        }

        final long startTime = System.currentTimeMillis();
        final RejudgeTask task = new RejudgeTask(cid, RejudgeType.CONTEST);
        rejudgeTasks.put(task.getKey(), task);
        Thread rejudgeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileUtil.deleteDir(getWorkPath(cid));
                } catch (IOException e) {
                    if (OjConfig.isDevMode())
                        e.printStackTrace();
                    log.error(e.getLocalizedMessage());
                }
                try {
                    // contestService.reset(cid);
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
                    rejudgeTasks.remove(task.getKey());
                }
                log.info(
                    "Rejudge contest contest " + cid + " finished, total judge: " + task.getTotal() + " total time: "
                        + (System.currentTimeMillis() - startTime) + " ms");
            }
        });
        rejudgeExecutor.execute(rejudgeThread);
        return true;
    }

    public boolean rejudgeContestProblem(final Integer cid, final Integer pid, final Integer num) {
        String key = RejudgeType.CONTEST_PROBLEM.getKey(cid, num);
        if (rejudgeTasks.containsKey(key)) {
            log.warn("Do not rejudge contest problem " + cid + "-" + pid + " since rejudge this problem is ongoing.");
            return false;
        }

        if (rejudgeTasks.containsKey(RejudgeType.CONTEST.getKey(cid))) {
            log.warn("Do not rejudge contest problem " + cid + "-" + pid + " since rejudge this contest is ongoing.");
            return false;
        }

        final long startTime = System.currentTimeMillis();
        final RejudgeTask task = new RejudgeTask(cid, num, RejudgeType.CONTEST_PROBLEM);
        rejudgeTasks.put(key, task);
        Thread rejudgeThread = new Thread(new Runnable() {
            @Override
            public void run() {
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
                    rejudgeTasks.remove(task.getKey());
                }
                log.info("Rejudge contest problem " + cid + "-" + pid + " finished, total judge: " + task.getTotal()
                    + " total time: " + (System.currentTimeMillis() - startTime) + " ms");
            }
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
        String workPath =
            FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")) + File.separator + "c" + cid
                + File.separator;

        return workPath;
    }

    public String getWorkPath(Solution solution) {
        String workPath = FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")) + File.separator;
        if (solution instanceof ContestSolutionModel) {
            workPath = workPath + "c" + solution.getCid() + File.separator;
            File contestDir = new File(workPath);
            if (!contestDir.isDirectory()) {
                if(contestDir.mkdirs()) {
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
        if (solution instanceof ContestSolutionModel) {
            workPath = workPath + "c" + solution.getCid() + File.separator + solution.getSid() + File.separator;
        } else {
            workPath = workPath + File.separator + solution.getSid() + File.separator;
        }

        return workPath;
    }

}
