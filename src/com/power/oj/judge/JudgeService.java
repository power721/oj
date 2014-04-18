package com.power.oj.judge;

import java.util.List;

//import com.jfinal.log.Logger;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionModel;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;

public class JudgeService
{
  //private final Logger log = Logger.getLogger(JudgeService.class);
  private static final JudgeService me = new JudgeService();
  private static final ContestService contestService = ContestService.me();
  private static final ProblemService problemService = ProblemService.me();
  private static final SolutionService solutionService = SolutionService.me();
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
      if (JudgeAdapter.size() <= 1)
      {
        JudgeAdapter judge = null;
        if (OjConfig.isLinux())
        {
          judge = new UestcJudgeAdapter();
        }
        else
        {
          judge = new PojJudgeAdapter();
        }
        
        new Thread(judge).start();
      }
    }
  }

  public void rejudge(SolutionModel solutionModel)
  {
    // revert user accepted/solved
    if (solutionModel.getCid() == null)
    {
      userService.revertAccepted(solutionModel);
    }
    solutionModel.setResult(ResultType.WAIT).setTest(0).setMtime(OjConfig.timeStamp);
    solutionModel.setMemory(0).setTime(0).setError(null).setSystemError(null);
    solutionModel.update();
    
    JudgeAdapter.addSolution(solutionModel);
    if (JudgeAdapter.size() <= 1)
    {
      JudgeAdapter judge = null;
      if (OjConfig.isLinux())
      {
        judge = new UestcJudgeAdapter();
      }
      else
      {
        judge = new PojJudgeAdapter();
      }
      
      new Thread(judge).start();
    }
  }
  
  public void rejudgeSolution(Integer sid)
  {
    SolutionModel solutionModel = solutionService.findSolution(sid);
    // revert problem accepted/solved
    problemService.revertAccepted(solutionModel);
    
    rejudge(solutionModel);
  }
  
  public void rejudgeProblem(Integer pid)
  {
    problemService.reset(pid);
    List<SolutionModel> solutionList = solutionService.getSolutionListForProblem(pid);
    
    // TODO lock this problem
    for (SolutionModel solutionModel: solutionList)
    {
      rejudge(solutionModel);
    }
  }

  public void rejudgeProblem4Wait(Integer pid)
  {
    List<SolutionModel> solutionList = solutionService.getWaitSolutionListForProblem(pid);
    
    // TODO lock this problem
    for (SolutionModel solutionModel: solutionList)
    {
      problemService.revertAccepted(solutionModel);
      rejudge(solutionModel);
    }
  }

  public void rejudgeContestSolution(Integer sid)
  {
    ContestSolutionModel contestSolutionModel = solutionService.findContestSolution(sid);
    int result = contestSolutionModel.getResult();
    
    contestSolutionModel.setResult(ResultType.WAIT).setTest(0).setMtime(OjConfig.timeStamp);
    contestSolutionModel.setMemory(0).setTime(0).setError(null).setSystemError(null);
    contestSolutionModel.update();
    
    SolutionModel solutionModel = solutionService.build(contestSolutionModel);
    solutionModel.put("originalResult", result);
    
    rejudge(solutionModel);
  }
  
  public void rejudgeContest(Integer cid)
  {
    contestService.reset(cid);
    List<ContestSolutionModel> solutionList = solutionService.getSolutionListForContest(cid);
    for (ContestSolutionModel solutionModel: solutionList)
    {
      rejudge(solutionService.build(solutionModel));
    }
  }

  public void rejudgeContestProblem(Integer cid, Integer pid)
  {
    // revert contest problem
    // build contest rank
  }
/*
  public boolean updateCompileError(SolutionModel solutionModel, String error)
  {
    solutionModel.setResult(ResultType.CE).setError(error);
    
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      log.info("updateCompileError");
      ContestSolutionModel contestSolution = new ContestSolutionModel(solutionModel);
      return contestSolution.update();
    }
    return solutionModel.update();
  }

  public boolean updateSystemError(SolutionModel solutionModel, String error)
  {
    solutionModel.setResult(ResultType.SE).setSystemError(error);

    Integer cid = solutionModel.getCid();
    log.info(solutionModel.toString());
    if (cid != null && cid > 0)
    {
      log.info("updateSystemError");
      ContestSolutionModel contestSolution = new ContestSolutionModel(solutionModel);
      return contestSolution.update();
    }
    return solutionModel.update();
  }
  
  public boolean updateResult(SolutionModel solutionModel, int result, int time, int memory)
  {
    solutionModel.setResult(result).setTime(time).setMemory(memory);

    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      log.info("updateResult");
      ContestSolutionModel contestSolution = new ContestSolutionModel(solutionModel);
      return contestSolution.update();
    }
    return solutionModel.update();
  }
  
  public boolean updateUser(SolutionModel solutionModel)
  {
    if (solutionModel.getResult() != ResultType.AC)
    {
      return false;
    }
    
    return userService.incAccepted(solutionModel);
  }

  public boolean updateProblem(SolutionModel solutionModel)
  {
    if (solutionModel.getResult() != ResultType.AC)
    {
      return false;
    }

    return problemService.incAccepted(solutionModel);
  }

  public boolean updateContest(SolutionModel solutionModel)
  {
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      contestService.updateBoard(solutionModel);
      return true;
    }
    return false;
  }
*/
}
