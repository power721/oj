package com.power.oj.judge;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jodd.format.Printf;
import jodd.io.FileNameUtil;
import jodd.io.FileUtil;

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

public class JudgeService
{
  private static final Logger log = Logger.getLogger(JudgeService.class);
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

    //synchronized (JudgeAdapter.class)
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

    try
    {
      FileUtil.appendString(getWorkPath(solution) + "solution.log", solution.getSid() + "\n");
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } // DEBUG
    synchronized (JudgeAdapter.class)
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
        try
        {
          FileUtil.deleteDir(getWorkPath(cid));
        } catch (IOException e)
        {
          if (OjConfig.getDevMode())
            e.printStackTrace();
          log.error(e.getLocalizedMessage());
        }
        contestService.reset(cid);
        List<ContestSolutionModel> solutionList = Collections.synchronizedList(solutionService.getSolutionListForContest(cid));
        synchronized(solutionList)
        {
          Iterator<ContestSolutionModel> it = solutionList.iterator();
          while (it.hasNext())
          {
            rejudge(it.next());
          }
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

  public int getDataFiles(Integer pid, List<String> inFiles, List<String> outFiles) throws IOException
  {
    File dataDir = new File(new StringBuilder(3).append(OjConfig.getString("dataPath")).append(File.separator).append(pid).toString());
    if (!dataDir.isDirectory())
    {
      throw new IOException("Data files does not exist.");
    }

    File[] arrayOfFile = dataDir.listFiles();
    if (arrayOfFile.length > 3)
    {
      Arrays.sort(arrayOfFile);
    }

    for (int i = 0; i < arrayOfFile.length; i++)
    {
      File in_file = arrayOfFile[i];
      if (!in_file.getName().toLowerCase().endsWith(OjConstants.DATA_EXT_IN))
        continue;
      File out_file = new File(new StringBuilder().append(dataDir.getAbsolutePath()).append(File.separator)
          .append(in_file.getName().substring(0, in_file.getName().length() - OjConstants.DATA_EXT_IN.length())).append(OjConstants.DATA_EXT_OUT).toString());
      if (!out_file.isFile())
      {
        log.warn(Printf.str("Output file for input file does not exist: %s", in_file.getAbsolutePath()));
        continue;
      }
      inFiles.add(in_file.getAbsolutePath());
      outFiles.add(out_file.getAbsolutePath());
    }

    return inFiles.size();
  }

  public String getWorkPath(Integer cid)
  {
    String workPath = new StringBuilder(5).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath")))
        .append(File.separator).append("c").append(cid).append(File.separator).toString();
    
    return workPath;
  }

  public String getWorkPath(Solution solution)
  {
    String workPath = new StringBuilder(2).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath"))).append(File.separator).toString();
    if (solution instanceof ContestSolutionModel)
    {
      workPath = new StringBuilder(4).append(workPath).append("c").append(solution.getCid()).append(File.separator).toString();
    }
    
    return workPath;
  }

  public String getWorkDirPath(Solution solution)
  {
    String workPath = new StringBuilder(2).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath"))).append(File.separator).toString();
    if (solution instanceof ContestSolutionModel)
    {
      workPath = new StringBuilder(6).append(workPath).append("c").append(solution.getCid()).append(File.separator)
          .append(solution.getSid()).append(File.separator).toString();
    }
    else
    {
      workPath = new StringBuilder(4).append(workPath).append(File.separator)
          .append(solution.getSid()).append(File.separator).toString();
    }
    
    return workPath;
  }
  
}
