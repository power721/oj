package com.power.oj.judge;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import com.jfinal.log.Logger;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.bean.Solution;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionModel;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;

public class JudgeService
{
  //private static final Logger log = Logger.getLogger(JudgeService.class);
  private static final JudgeService me = new JudgeService();
  private static final ContestService contestService = ContestService.me();
  private static final ProblemService problemService = ProblemService.me();
  private static final SolutionService solutionService = SolutionService.me();
  private static final UserService userService = UserService.me();
  private static final ExecutorService judgeExecutor = Executors.newSingleThreadExecutor();
  private static final ExecutorService rejudgeExecutor = Executors.newSingleThreadExecutor();

  private JudgeService()
  {
  }

  public static JudgeService me()
  {
    return me;
  }

  public void judge(Solution solution)
  {
    if (solution instanceof SolutionModel)
    {
      problemService.incSubmission(solution.getPid());
      userService.incSubmission(solution.getUid());
    }

    synchronized (JudgeAdapter.class)
    {
      JudgeAdapter.addSolution(solution);
      if (JudgeAdapter.size() <= 1)
      {
        JudgeAdapter judgeThread = null;
        if (OjConfig.isLinux())
        {
          judgeThread = new UestcJudgeAdapter(solution);
        } else
        {
          judgeThread = new PojJudgeAdapter(solution);
        }
        judgeExecutor.execute(judgeThread);
      }
    }
  }

  public void rejudge(Solution solution)
  {
    if (solution instanceof SolutionModel)
    {
      userService.revertAccepted((SolutionModel) solution);
    }

    solution.setResult(ResultType.WAIT).setTest(0).setMtime(OjConfig.timeStamp);
    solution.setMemory(0).setTime(0).setError(null).setSystemError(null);
    if (solution instanceof SolutionModel)
    {
      ((SolutionModel) solution).update();
    } else
    {
      ((ContestSolutionModel) solution).update();
    }

    synchronized (JudgeAdapter.class)
    {
      JudgeAdapter.addSolution(solution);
      if (JudgeAdapter.size() <= 1)
      {
        JudgeAdapter judgeThread = null;
        if (OjConfig.isLinux())
        {
          judgeThread = new UestcJudgeAdapter(solution);
        } else
        {
          judgeThread = new PojJudgeAdapter(solution);
        }
        judgeExecutor.execute(judgeThread);
      }
    }
  }

  public void rejudgeSolution(Integer sid)
  {
    SolutionModel solution = solutionService.findSolution(sid);

    problemService.revertAccepted(solution);

    rejudge(solution);
  }

  public void rejudgeProblem(final Integer pid)
  {
    Thread rejudgeThread = new Thread(new Runnable() {
      @Override
      public void run()
      {
        problemService.reset(pid);
        List<SolutionModel> solutionList = solutionService.getSolutionListForProblem(pid);

        // TODO lock this problem
        for (SolutionModel solution : solutionList)
        {
          rejudge(solution);
        }
      }
    });
    rejudgeExecutor.execute(rejudgeThread);
  }

  public void rejudgeProblem4Wait(final Integer pid)
  {
    Thread rejudgeThread = new Thread(new Runnable() {
      @Override
      public void run()
      {
        List<SolutionModel> solutionList = solutionService.getWaitSolutionListForProblem(pid);

        // TODO lock this problem
        for (SolutionModel solution : solutionList)
        {
          problemService.revertAccepted(solution);
          rejudge(solution);
        }
      }
    });
    rejudgeExecutor.execute(rejudgeThread);
  }

  public void rejudgeContestSolution(Integer sid)
  {
    ContestSolutionModel contestSolutionModel = solutionService.findContestSolution(sid);
    int result = contestSolutionModel.getResult();

    contestSolutionModel.setResult(ResultType.WAIT).setTest(0).setMtime(OjConfig.timeStamp);
    contestSolutionModel.setMemory(0).setTime(0).setError(null).setSystemError(null);
    contestSolutionModel.update();

    contestSolutionModel.put("originalResult", result);

    rejudge(contestSolutionModel);
  }

  public void rejudgeContest(final Integer cid)
  {
    Thread rejudgeThread = new Thread(new Runnable() {
      @Override
      public void run()
      {
        contestService.reset(cid);
        List<ContestSolutionModel> solutionList = solutionService.getSolutionListForContest(cid);

        for (ContestSolutionModel solutionModel : solutionList)
        {
          rejudge(solutionModel);
        }
      }
    });
    rejudgeExecutor.execute(rejudgeThread);
  }

  public void rejudgeContestProblem(Integer cid, Integer pid)
  {
    // revert contest problem
    // build contest rank
  }

}
