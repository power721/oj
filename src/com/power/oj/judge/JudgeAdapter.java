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
  protected Solution solutionModel;
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
  
  protected abstract boolean Compile() throws IOException;
  
  protected abstract boolean RunProcess() throws IOException, InterruptedException;

  public void run()
  {
    while (!judgeList.isEmpty())
    {
      log.info(Printf.str("Judge threads: %d", judgeList.size()));
      synchronized (JudgeAdapter.class)
      {
        solutionModel = judgeList.poll();
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
    log.info(String.valueOf(solutionModel.getSid()));
    Integer cid = solutionModel.getCid();
    programLanguage = OjConfig.language_type.get(solutionModel.getLanguage());
    if (cid != null && cid > 0)
    {
      problemModel = problemService.findProblemForContest(solutionModel.getPid());
    }
    else
    {
      problemModel = problemService.findProblem(solutionModel.getPid());
    }
    
    workPath = new StringBuilder(2).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.getString("workPath"))).append(File.separator).toString();
    if (cid != null && cid > 0)
    {
      workPath = new StringBuilder(4).append(workPath).append("c").append(cid).append(File.separator).toString();
    }
    else if (OjConfig.getBoolean("deleteTmpFile", false))
    {
      File prevWorkDir = new File(new StringBuilder(2).append(workPath).append(solutionModel.getSid() - 5).toString());
      if (prevWorkDir.isDirectory())
      {
        FileUtil.deleteDir(prevWorkDir);
        log.info("Delete previous work directory " + prevWorkDir.getAbsolutePath());
      }
    }
    
    File workDir = new File(new StringBuilder(2).append(workPath).append(solutionModel.getSid()).toString());
    if (workDir.isDirectory())
    {
      FileUtil.cleanDir(workDir);
    }
    else
    {
      FileUtil.mkdirs(workDir);
    }
    workDirPath = workDir.getAbsolutePath();
    log.info("mkdirs workDir: " + workDirPath);
    
    sourceFile = new File(new StringBuilder(5).append(workDirPath).append(File.separator).
        append(OjConstants.SOURCE_FILE_NAME).append(".").append(programLanguage.getExt()).toString());
    FileUtil.touch(sourceFile);
    FileUtil.writeString(sourceFile, solutionModel.getSource());
  }

  protected int getDataFiles() throws IOException
  {
    File dataDir = new File(new StringBuilder(3).append(OjConfig.getString("dataPath")).append(File.separator).append(solutionModel.getPid()).toString());
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
    solutionModel.setResult(ResultType.CE).setError(error);
    
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      return ((ContestSolutionModel)solutionModel).update();
    }
    return ((SolutionModel)solutionModel).update();
  }

  protected boolean updateRuntimeError(String error)
  {
    solutionModel.setResult(ResultType.RE).setError(error);
    
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      return ((ContestSolutionModel)solutionModel).update();
    }
    return ((SolutionModel)solutionModel).update();
  }

  protected boolean updateSystemError(String error)
  {
    solutionModel.setResult(ResultType.SE).setSystemError(error);

    Integer cid = solutionModel.getCid();
    log.info(solutionModel.toString());
    if (cid != null && cid > 0)
    {
      return ((ContestSolutionModel)solutionModel).update();
    }
    return ((SolutionModel)solutionModel).update();
  }
  
  protected boolean updateResult(int result, int time, int memory)
  {
    solutionModel.setResult(result).setTime(time).setMemory(memory);

    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      return ((ContestSolutionModel)solutionModel).update();
    }
    return ((SolutionModel)solutionModel).update();
  }

  protected boolean updateResult(boolean ac, Integer test)
  {
    if (ac)
    {
      solutionModel.setResult(ResultType.AC);
      solutionModel.setTime(totalRunTime);
    }
    else if (solutionModel.getResult() != ResultType.CE && solutionModel.getResult() != ResultType.RF)
    {
      solutionModel.setTest(test);
    }
    
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      return ((ContestSolutionModel)solutionModel).update();
    }
    return ((SolutionModel)solutionModel).update();
  }

  protected boolean setResult(int result, int time, int memory)
  {
    boolean needUpdate = (result != solutionModel.getResult());
    solutionModel.setResult(result);
    if (solutionModel.getTime() == null)
    {
      solutionModel.setTime(time);
    }
    else
    {
      solutionModel.setTime(Math.max(time, solutionModel.getTime()));
    }
    if (solutionModel.getMemory() == null)
    {
      solutionModel.setMemory(memory);
    }
    else
    {
      solutionModel.setMemory(Math.max(memory, solutionModel.getMemory()));
    }
    
    if (!needUpdate)
    {
      return true;
    }
    
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      return ((ContestSolutionModel)solutionModel).update();
    }
    return ((SolutionModel)solutionModel).update();
  }
  
  protected boolean updateUser()
  {
    if (solutionModel.getResult() != ResultType.AC)
    {
      return false;
    }
    
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      return false;
    }
    return userService.incAccepted((SolutionModel)solutionModel);
  }

  protected boolean updateProblem()
  {
    if (solutionModel.getResult() != ResultType.AC)
    {
      return false;
    }

    return problemService.incAccepted((SolutionModel)solutionModel);
  }

  protected boolean updateContest()
  {
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      if (((ContestSolutionModel)solutionModel).get("originalResult") != null)
      {
        log.info("updateBoard4Rejudge");
        contestService.updateBoard4Rejudge((ContestSolutionModel)solutionModel);
      }
      else
      {
        contestService.updateBoard((ContestSolutionModel)solutionModel);
      }
      return true;
    }
    return false;
  }

  public static void addSolution(Solution solutionModel)
  {
    judgeList.add(solutionModel);
  }
  
  public static int size()
  {
    return judgeList.size();
  }
  
}
