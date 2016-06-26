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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private static final Map<String, RejudgeTask> rejudgeTasks = new HashMap<>();

    private JudgeService() {
    }

    public static JudgeService me() {
        return me;
    }

    public RejudgeTask getRejudgeTask(String key) {
        return rejudgeTasks.get(key);
    }

    public boolean isRejudging(RejudgeType type, Integer id) {
        return rejudgeTasks.containsKey(type.getKey(id));
    }

    public boolean isRejudging(RejudgeType type, Integer cid, Integer pid) {
        return rejudgeTasks.containsKey(type.getKey(cid, pid));
    }

    public void judge(Solution solution) {
        if (solution instanceof SolutionModel) {
            problemService.incSubmission(solution.getPid());
            userService.incSubmission(solution.getUid());
        }

        synchronized (JudgeAdapter.class) {
            JudgeAdapter judgeThread = null;
            if (OjConfig.isLinux()) {
                judgeThread = new PowerJudgeAdapter(solution);
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
            userService.revertAccepted((SolutionModel) solution);
        }

        solution.setResult(ResultType.WAIT).setTest(0).setMtime(OjConfig.timeStamp);
        solution.setMemory(0).setTime(0).setError(null).setSystemError(null);
        if (solution instanceof SolutionModel) {
            ((SolutionModel) solution).update();
        } else {
            ((ContestSolutionModel) solution).update();
        }

        synchronized (JudgeAdapter.class) {
            JudgeAdapter judgeThread = null;
            if (OjConfig.isLinux()) {
                judgeThread = new PowerJudgeAdapter(solution);
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

    public void rejudgeSolution(Integer sid) {
        SolutionModel solution = solutionService.findSolution(sid);

        problemService.revertAccepted(solution);

        rejudge(solution);
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

    public void rejudgeContest(final Integer cid) {
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
                // contestService.reset(cid);
                List<ContestSolutionModel> solutionList =
                    Collections.synchronizedList(solutionService.getSolutionListForContest(cid));
                synchronized (solutionList) {
                    Iterator<ContestSolutionModel> it = solutionList.iterator();
                    while (it.hasNext()) {
                        rejudgeContestSolution(it.next());
                    }
                }
            }
        });
        rejudgeExecutor.execute(rejudgeThread);
    }

    public boolean rejudgeContestProblem(final Integer cid, final Integer pid) {
        String key = RejudgeType.CONTEST_PROBLEM.getKey(cid, pid);
        if (rejudgeTasks.containsKey(key)) {
            log.warn("Do not rejudge contest problem " + cid + "-" + pid + " since rejudge this problem is ongoing.");
            return false;
        }

        final long startTime = System.currentTimeMillis();
        final RejudgeTask task = new RejudgeTask(cid, pid, RejudgeType.CONTEST);
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
                log.info("Rejudge contest problem " + cid + "-" + pid + " finished, total judge: "
                    + task.getTotal() + " total time: " + (System.currentTimeMillis() - startTime) + " ms");
            }
        });

        rejudgeExecutor.execute(rejudgeThread);
        return true;
    }

    public int getDataFiles(Integer pid, List<String> inFiles, List<String> outFiles) throws IOException {
        File dataDir = new File(
            new StringBuilder(3).append(OjConfig.getString("dataPath")).append(File.separator).append(pid).toString());
        if (!dataDir.isDirectory()) {
            throw new IOException("Data files does not exist.");
        }

        File[] arrayOfFile = dataDir.listFiles();
        if (arrayOfFile.length > 3) {
            Arrays.sort(arrayOfFile);
        }

        for (int i = 0; i < arrayOfFile.length; i++) {
            File inFile = arrayOfFile[i];
            if (!inFile.getName().toLowerCase().endsWith(OjConstants.DATA_EXT_IN)) {
                continue;
            }
            File outFile = new File(new StringBuilder().append(dataDir.getAbsolutePath()).append(File.separator)
                .append(inFile.getName().substring(0, inFile.getName().length() - OjConstants.DATA_EXT_IN.length()))
                .append(OjConstants.DATA_EXT_OUT).toString());
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
            new StringBuilder(5).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")))
                .append(File.separator).append("c").append(cid).append(File.separator).toString();

        return workPath;
    }

    public String getWorkPath(Solution solution) {
        String workPath =
            new StringBuilder(2).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")))
                .append(File.separator).toString();
        if (solution instanceof ContestSolutionModel) {
            workPath =
                new StringBuilder(4).append(workPath).append("c").append(solution.getCid()).append(File.separator)
                    .toString();
        }

        return workPath;
    }

    public String getWorkDirPath(Solution solution) {
        String workPath =
            new StringBuilder(2).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")))
                .append(File.separator).toString();
        if (solution instanceof ContestSolutionModel) {
            workPath =
                new StringBuilder(6).append(workPath).append("c").append(solution.getCid()).append(File.separator)
                    .append(solution.getSid()).append(File.separator).toString();
        } else {
            workPath = new StringBuilder(4).append(workPath).append(File.separator).append(solution.getSid())
                .append(File.separator).toString();
        }

        return workPath;
    }

}
