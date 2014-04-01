package com.power.oj.judge;

import com.jfinal.log.Logger;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;

public class JudgeService
{
  private final Logger log = Logger.getLogger(JudgeService.class);
  private static final JudgeService me = new JudgeService();
  private static final ProblemService problemService = ProblemService.me();
  private static final UserService userService = UserService.me();
  
  private JudgeService() {}
  
  public static JudgeService me()
  {
    return me;
  }
  
  public void judge(SolutionModel solutionModel)
  {
    Integer uid = solutionModel.getUid();
    Integer pid = solutionModel.getPid();
    Integer cid = solutionModel.getCid();
    
    if (cid == null || cid == 0)
    {
      problemService.incSubmission(pid);
      userService.incSubmission(uid);
    }
    
    synchronized (JudgeAdapter.class)
    {
      JudgeAdapter.addSolution(solutionModel);
      log.info("JudgeAdapter.addSolution");
      if (JudgeAdapter.size() <= 1)
      {
        JudgeAdapter judge = new PojJudgeAdapter();
        new Thread(judge).start();
        log.info("judge.start()");
      }
    }
    System.out.println(solutionModel.getSid());
  }

  public void rejudge(SolutionModel solutionModel)
  {
    // revert user accepted/solved
    // revert problem accepted/solved
    // revert contest problem
    // build contest rank
    // judge
  }
  
  public void rejudgeSolution(Integer sid)
  {
    
  }
  
  public void rejudgeProblem(Integer pid)
  {
    
  }

  public void rejudgeContest(Integer cid)
  {
    
  }

  public void rejudgeContestProblem(Integer cid, Integer pid)
  {
    
  }
  
}
