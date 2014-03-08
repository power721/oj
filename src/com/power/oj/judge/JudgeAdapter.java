package com.power.oj.judge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import jodd.io.FileNameUtil;
import jodd.util.StringUtil;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.ContestModel;
import com.power.oj.contest.ContestService;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
import com.power.oj.problem.ProblemModel;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;

public abstract class JudgeAdapter extends Thread
{
  public static final String DATA_EXT_IN = ".in";
  public static final String DATA_EXT_OUT = ".out";
  public static final String sourceFileName = "Main";
  public static SolutionModel solutionModel;
  
  protected final Logger log = Logger.getLogger(getClass());
  protected static final String workPath;
  protected static final ContestService contestService = ContestService.me();
  protected static final UserService userService = UserService.me();
  protected static final ProblemService problemService = ProblemService.me();
  protected static ConcurrentLinkedQueue<SolutionModel> judgeList = new ConcurrentLinkedQueue<SolutionModel>();
  
  protected List<String> inFiles = new ArrayList<String>();
  protected List<String> outFiles = new ArrayList<String>();
  
  static
  {
    workPath = new StringBuilder(2).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.get("work_path"))).append(File.separator).toString();
  }
  
  public JudgeAdapter()
  {
    super();
    log.info("JudgeAdapter()");
    run();
  }
  
  protected abstract boolean Compile() throws IOException;
  
  protected abstract boolean RunProcess() throws IOException, InterruptedException;

  public void run()
  {
    log.info("JudgeAdapter run()");
    if (judgeList.isEmpty())
    {
      return;
    }
    log.info("Judge threads: " + judgeList.size());
    solutionModel = judgeList.poll();
    synchronized (JudgeAdapter.class)
    {
      try
      {
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
        solutionModel.set("result", ResultType.SE).set("system_error", e.getMessage());
        solutionModel.update();

        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.error(e.getMessage());
      }
    }
  }

  protected String getCompileCmd(String compileCmd, String path, String name, String ext)
  {
    path = new StringBuilder(2).append(path).append("\\").toString();
    compileCmd = StringUtil.replace(compileCmd, "%PATH%", path);
    compileCmd = StringUtil.replace(compileCmd, "%NAME%", name);
    compileCmd = StringUtil.replace(compileCmd, "%EXT%", ext);
    compileCmd = new StringBuilder(2).append(compileCmd).append("\n").toString();

    return compileCmd;
  }
  
  protected boolean getDataFiles()
  {
    File dataDir = new File(new StringBuilder(3).append(OjConfig.get("data_path")).append(File.separator).append(solutionModel.getInt("pid")).toString());
    if (!dataDir.isDirectory())
    {
      String system_error = new StringBuilder(3).append("Data directory ").append(dataDir).append(" does not exist.").toString();
      solutionModel.set("result", ResultType.SE).set("system_error", system_error);
      solutionModel.update();
      log.error(system_error);
      return false;
    }
    log.info("dataDir: " + dataDir.getAbsolutePath());
    
    List<String> inFiles = new ArrayList<String>();
    List<String> outFiles = new ArrayList<String>();
    File[] arrayOfFile = dataDir.listFiles();

    for (int i = 0; i < arrayOfFile.length; i++)
    {
      File in_file = arrayOfFile[i];
      if (!in_file.getName().toLowerCase().endsWith(DATA_EXT_IN))
        continue;
      File out_file = new File(new StringBuilder().append(dataDir.getAbsolutePath()).append(File.separator)
          .append(in_file.getName().substring(0, in_file.getName().length() - DATA_EXT_IN.length())).append(DATA_EXT_OUT).toString());
      if (!out_file.isFile())
        continue;
      inFiles.add(in_file.getAbsolutePath());
      outFiles.add(out_file.getAbsolutePath());
    }
    
    return true;
  }
  
  protected boolean updateError(int result, String error)
  {
    solutionModel.set("result", result).set("time", 0).set("memory", 0).set("error", error);
    return solutionModel.update();
  }
  
  protected boolean updateResult(int result, int time, int memory)
  {
    solutionModel.set("result", result).set("time", time).set("memory", memory);
    return solutionModel.update();
  }
  
  protected int updateUser()
  {
    Integer uid = solutionModel.getUid();
    return Db.update("UPDATE user SET accept=accept+1 WHERE uid=?", uid);
  }

  protected boolean updateProblem()
  {
    Integer pid = solutionModel.getInt("pid");
    Integer sid = solutionModel.getInt("sid");
    Integer uid = solutionModel.getUid();
    ProblemModel problemModel = ProblemService.me().findProblem(pid);
    
    problemModel.set("accept", problemModel.getInt("accept")+1);
    Integer lastAccepted = Db.queryInt("SELECT sid FROM solution WHERE pid=? AND uid=? AND sid!=? AND result=?", pid, uid, sid, ResultType.AC);
    if (lastAccepted == null)
    {
      problemModel.set("solved", problemModel.getInt("solved")+1);
    }
    
    return problemModel.update();
  }

  protected boolean updateContest()
  {
    Integer cid = solutionModel.getInt("cid");
    if (cid != null && cid > 0)
    {
      // TODO move to contestService
      // TODO update board
      Record contestProblem = Db.findFirst("SELECT * FROM contest_problem WHERE cid=? AND num=?", cid, solutionModel.getInt("num"));
      if (contestProblem.getInt("first_blood") == 0)
      {
        ContestModel contestModle = contestService.getContest(cid);
        int contestStartTime = contestModle.getInt("start_time");
        contestProblem.set("first_blood", solutionModel.getUid());
        contestProblem.set("first_blood_time", (solutionModel.getInt("ctime") - contestStartTime) / 60);
      }
      contestProblem.set("accept", contestProblem.getInt("accept")+1);
      return Db.update("contest_problem", contestProblem);
    }
    return true;
  }

  public static void addSolution(SolutionModel solutionModel)
  {
    judgeList.add(solutionModel);
  }
  
  public static int size()
  {
    return judgeList.size();
  }
  
}
