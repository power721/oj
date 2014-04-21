package com.power.oj.judge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import com.power.oj.core.model.ProgramLanguageModel;
import com.power.oj.problem.ProblemModel;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;

public abstract class JudgeAdapter implements Runnable
{

  protected static final ContestService contestService = ContestService.me();
  protected static final UserService userService = UserService.me();
  protected static final ProblemService problemService = ProblemService.me();
  protected static ConcurrentLinkedQueue<Solution> judgeList = new ConcurrentLinkedQueue<Solution>();

  protected final Logger log = Logger.getLogger(getClass());
  protected Solution solution;
  protected ProblemModel problemModel;
  protected ProgramLanguageModel programLanguage;
  protected int totalRunTime;
  protected String workPath;
  protected String workDirPath;
  protected File sourceFile;

  protected List<String> inFiles = new ArrayList<String>();
  protected List<String> outFiles = new ArrayList<String>();

  public JudgeAdapter()
  {
    super();
  }

  public JudgeAdapter(Solution solution)
  {
    this();
    this.solution = solution;
  }

  protected abstract boolean Compile() throws IOException;

  protected abstract boolean RunProcess() throws IOException, InterruptedException;

  @Override
  public void run()
  {
    while (!judgeList.isEmpty())
    {
      log.info(Printf.str("Judge threads: %d", judgeList.size()));
      synchronized (JudgeAdapter.class)
      {
        solution = judgeList.poll();
        try
        {
          prepare();
          if (Compile())
          {
            RunProcess();
          }
          else
          {
            log.warn("Compile failed.");
          }
        } catch (Exception e)
        {
          updateSystemError(e.getLocalizedMessage());
          
          if (OjConfig.getDevMode())
            e.printStackTrace();
          log.error(e.getLocalizedMessage());
        }
      }
    }
  }
  
  protected void prepare() throws IOException
  {
    // log.info(String.valueOf(solution.getSid()));
    Integer cid = solution.getCid();
    programLanguage = OjConfig.language_type.get(solution.getLanguage());
    if (solution instanceof ContestSolutionModel)
    {
      problemModel = problemService.findProblemForContest(solution.getPid());
    } else
    {
      problemModel = problemService.findProblem(solution.getPid());
    }

    workPath = new StringBuilder(2).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath"))).append(File.separator).toString();
    if (solution instanceof ContestSolutionModel)
    {
      workPath = new StringBuilder(4).append(workPath).append("c").append(cid).append(File.separator).toString();
    } else if (OjConfig.getBoolean("deleteTmpFile", false))
    {
      File prevWorkDir = new File(new StringBuilder(2).append(workPath).append(solution.getSid() - 5).toString());
      if (prevWorkDir.isDirectory())
      {
        FileUtil.deleteDir(prevWorkDir);
        log.info("Delete previous work directory " + prevWorkDir.getAbsolutePath());
      }
    }

    File workDir = new File(new StringBuilder(2).append(workPath).append(solution.getSid()).toString());
    if (workDir.isDirectory())
    {
      FileUtil.cleanDir(workDir);
    } else
    {
      FileUtil.mkdirs(workDir);
    }
    workDirPath = workDir.getAbsolutePath();
    // log.info("mkdirs workDir: " + workDirPath);

    sourceFile = new File(new StringBuilder(5).append(workDirPath).append(File.separator).append(OjConstants.SOURCE_FILE_NAME).append(".")
        .append(programLanguage.getExt()).toString());
    FileUtil.touch(sourceFile);
    FileUtil.writeString(sourceFile, solution.getSource());
  }

  protected int getDataFiles() throws IOException
  {
    File dataDir = new File(new StringBuilder(3).append(OjConfig.getString("dataPath")).append(File.separator).append(solution.getPid()).toString());
    if (!dataDir.isDirectory())
    {
      throw new IOException("Data files does not exist.");
    }

    inFiles = new ArrayList<String>();
    outFiles = new ArrayList<String>();
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

  protected boolean updateCompileError(String error)
  {
    solution.setResult(ResultType.CE).setError(error);

    if (solution instanceof ContestSolutionModel)
    {
      return ((ContestSolutionModel) solution).update();
    }
    return ((SolutionModel) solution).update();
  }

  protected boolean updateRuntimeError(String error)
  {
    solution.setResult(ResultType.RE).setError(error);

    if (solution instanceof ContestSolutionModel)
    {
      return ((ContestSolutionModel) solution).update();
    }
    return ((SolutionModel) solution).update();
  }

  protected boolean updateSystemError(String error)
  {
    solution.setResult(ResultType.SE).setSystemError(error);

    if (solution instanceof ContestSolutionModel)
    {
      return ((ContestSolutionModel) solution).update();
    }
    return ((SolutionModel) solution).update();
  }

  protected boolean updateResult(int result, int time, int memory)
  {
    solution.setResult(result).setTime(time).setMemory(memory);

    if (solution instanceof ContestSolutionModel)
    {
      return ((ContestSolutionModel) solution).update();
    }
    return ((SolutionModel) solution).update();
  }

  protected boolean updateResult(boolean ac, Integer test)
  {
    if (ac)
    {
      solution.setResult(ResultType.AC);
      solution.setTime(totalRunTime);
    } else if (solution.getResult() != ResultType.CE && solution.getResult() != ResultType.RF)
    {
      solution.setTest(test);
    }

    if (solution instanceof ContestSolutionModel)
    {
      return ((ContestSolutionModel) solution).update();
    }
    return ((SolutionModel) solution).update();
  }

  protected boolean setResult(int result, int time, int memory)
  {
    boolean needUpdate = (result != solution.getResult());
    solution.setResult(result);
    if (solution.getTime() == null)
    {
      solution.setTime(time);
    } else
    {
      solution.setTime(Math.max(time, solution.getTime()));
    }
    if (solution.getMemory() == null)
    {
      solution.setMemory(memory);
    } else
    {
      solution.setMemory(Math.max(memory, solution.getMemory()));
    }

    if (!needUpdate)
    {
      return true;
    }

    if (solution instanceof ContestSolutionModel)
    {
      return ((ContestSolutionModel) solution).update();
    }
    return ((SolutionModel) solution).update();
  }

  protected boolean updateUser()
  {
    if (solution.getResult() != ResultType.AC)
    {
      return false;
    }

    Integer cid = solution.getCid();
    if (cid != null && cid > 0)
    {
      return false;
    }
    return userService.incAccepted((SolutionModel) solution);
  }

  protected boolean updateProblem()
  {
    if (solution.getResult() != ResultType.AC)
    {
      return false;
    }

    return problemService.incAccepted((SolutionModel) solution);
  }

  protected boolean updateContest()
  {
    if (solution instanceof ContestSolutionModel)
    {
      if (((ContestSolutionModel) solution).get("originalResult") != null)
      {
        contestService.updateBoard4Rejudge((ContestSolutionModel) solution);
      } else
      {
        contestService.updateBoard((ContestSolutionModel) solution);
      }
      return true;
    }
    return false;
  }

  public static void addSolution(Solution solution)
  {
    judgeList.add(solution);
  }
  
  public static int size()
  {
    return judgeList.size();
  }
  
}
