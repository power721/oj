package com.power.oj.judge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import jodd.io.FileNameUtil;
import jodd.io.FileUtil;
import jodd.util.StringUtil;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestProblemModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.problem.ProblemModel;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;

public abstract class JudgeAdapter implements Runnable
{
  public static final String DATA_EXT_IN = ".in";
  public static final String DATA_EXT_OUT = ".out";
  public static final String sourceFileName = "Main";
  
  protected final Logger log = Logger.getLogger(getClass());
  protected static final String workPath;
  protected static final ContestService contestService = ContestService.me();
  protected static final UserService userService = UserService.me();
  protected static final ProblemService problemService = ProblemService.me();
  protected static ConcurrentLinkedQueue<SolutionModel> judgeList = new ConcurrentLinkedQueue<SolutionModel>();
  protected SolutionModel solutionModel;
  protected ProblemModel problemModel;
  protected LanguageModel language;
  protected String workDirPath;
  
  protected List<String> inFiles = new ArrayList<String>();
  protected List<String> outFiles = new ArrayList<String>();
  
  static
  {
    // TODO move to OjConfig
    workPath = new StringBuilder(2).append(FileNameUtil.normalizeNoEndSeparator(OjConfig.get("work_path"))).append(File.separator).toString();
  }
  
  public JudgeAdapter()
  {
    super();
  }
  
  protected abstract boolean Compile() throws IOException;
  
  protected abstract boolean RunProcess() throws IOException, InterruptedException;

  public void run()
  {
    log.info("JudgeAdapter run()");
    while (!judgeList.isEmpty())
    {
      log.info("Judge threads: " + judgeList.size());
      solutionModel = judgeList.poll();
      synchronized (JudgeAdapter.class)
      {
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
    language = (LanguageModel) OjConfig.language_type.get(solutionModel.getInt("language"));
    problemModel = ProblemModel.dao.findById(solutionModel.getInt("pid"));
    
    if (OjConfig.getBoolean("delete_tmp_file"))
    {
      File prevWorkDir = new File(new StringBuilder(2).append(workPath).append(solutionModel.getInt("sid") - 2).toString());
      if (prevWorkDir.isDirectory())
      {
        FileUtil.deleteDir(prevWorkDir);
        log.info("Delete previous work directory " + prevWorkDir.getAbsolutePath());
      }
    }
    
    File workDir = new File(new StringBuilder(2).append(workPath).append(solutionModel.getInt("sid")).toString());
    FileUtil.mkdirs(workDir);
    workDirPath = workDir.getAbsolutePath();
    log.info("mkdirs workDir: " + workDirPath);
  }

  protected String getCompileCmd(String compileCmd, String path, String name, String ext)
  {
    path = new StringBuilder(2).append(path).append(File.separator).toString();
    compileCmd = StringUtil.replace(compileCmd, "%PATH%", path);
    compileCmd = StringUtil.replace(compileCmd, "%NAME%", name);
    compileCmd = StringUtil.replace(compileCmd, "%EXT%", ext);
    compileCmd = new StringBuilder(2).append(compileCmd).append("\n").toString();

    return compileCmd;
  }
  
  protected int getDataFiles() throws IOException
  {
    File dataDir = new File(new StringBuilder(3).append(OjConfig.get("data_path")).append(File.separator).append(solutionModel.getInt("pid")).toString());
    if (!dataDir.isDirectory())
    {
      throw new IOException("Data files does not exist.");
    }
    log.info("dataDir: " + dataDir.getAbsolutePath());
    
    inFiles = new ArrayList<String>();
    outFiles = new ArrayList<String>();
    File[] arrayOfFile = dataDir.listFiles();

    for (int i = 0; i < arrayOfFile.length; i++)
    {
      File in_file = arrayOfFile[i];
      if (!in_file.getName().toLowerCase().endsWith(DATA_EXT_IN))
        continue;
      File out_file = new File(new StringBuilder().append(dataDir.getAbsolutePath()).append(File.separator)
          .append(in_file.getName().substring(0, in_file.getName().length() - DATA_EXT_IN.length())).append(DATA_EXT_OUT).toString());
      if (!out_file.isFile())
      {
        log.warn("Output file for input file does not exist: " + in_file.getAbsolutePath());
        continue;
      }
      inFiles.add(in_file.getAbsolutePath());
      outFiles.add(out_file.getAbsolutePath());
    }
    
    return inFiles.size();
  }
  
  protected boolean updateCompileError(String error)
  {
    solutionModel.set("result", ResultType.CE).set("error", error);
    return solutionModel.update();
  }

  protected boolean updateSystemError(String error)
  {
    solutionModel.set("result", ResultType.SE).set("systemError", error);
    return solutionModel.update();
  }
  
  protected boolean updateResult(int result, int time, int memory)
  {
    solutionModel.set("result", result).set("time", time).set("memory", memory);
    return solutionModel.update();
  }
  
  protected boolean updateUser()
  {
    if (solutionModel.getInt("result") != ResultType.AC)
    {
      return false;
    }
    
    Integer uid = solutionModel.getUid();
    Db.update("UPDATE user SET accept=accept+1 WHERE uid=?", uid);
    return true;
  }

  protected boolean updateProblem()
  {
    if (solutionModel.getInt("result") != ResultType.AC)
    {
      return false;
    }
    
    Integer pid = solutionModel.getInt("pid");
    Integer sid = solutionModel.getInt("sid");
    Integer uid = solutionModel.getUid();
    ProblemModel problemModel = ProblemService.me().findProblem(pid);
    
    problemModel.set("accept", problemModel.getInt("accept")+1);
    Integer lastAccepted = Db.queryInt("SELECT sid FROM solution WHERE pid=? AND uid=? AND sid<? AND result=? LIMIT 1", pid, uid, sid, ResultType.AC);
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
      Integer uid = solutionModel.getUid();
      Integer num = solutionModel.getInt("num");
      Long submitTime = solutionModel.getLong("ctime");
      char c = (char) (num + 'A');
      Record board = Db.findFirst("SELECT * FROM board WHERE cid=? AND uid=?", cid, uid);
      if (board == null)
      {
        board = new Record();
        board.set("cid", cid);
        board.set("uid", uid);
        Db.save("board", board);
        board = Db.findById("board", board.get("id"));
      }
      Integer wrongSubmits = board.getInt(c + "_WrongNum");
      
      if (solutionModel.getInt("result") == ResultType.AC)
      {
        ContestProblemModel contestProblem = ContestProblemModel.dao.findFirst("SELECT * FROM contest_problem WHERE cid=? AND num=?", cid, num);
        ContestModel contestModle = contestService.getContest(cid);
        Integer contestStartTime = contestModle.getStartTime();
        
        if (contestProblem.getFirstBloodUid() == 0)
        {
          contestProblem.setFirstBloodUid(solutionModel.getUid());
          contestProblem.setFirstBloodTime((int) ((submitTime - contestStartTime) / 60));
        }
        contestProblem.setAccepted(contestProblem.getAccepted()+1);
        contestProblem.update();
        
        Integer acTime = board.getInt(c + "_SolvedTime");
        if (acTime == null || acTime == 0)
        {
          acTime = (int) ((submitTime - contestStartTime) / 60);
          board.set(c+"_SolvedTime", acTime);
          board.set("solved", board.getInt("solved")+1);
          board.set("penalty", board.getInt("penalty") + acTime + wrongSubmits * OjConstants.PENALTY_FOR_WRONG_SUBMISSION);
        }
      }
      else
      {
        board.set(c+"_WrongNum", wrongSubmits+1);
      }
      
      Db.update("board", board);
      return true;
    }
    return false;
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
