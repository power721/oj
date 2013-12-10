package com.power.oj.judge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jodd.io.FileNameUtil;
import jodd.io.FileUtil;
import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.log.Logger;
import com.power.oj.contest.ContestRankWebSocket;
import com.power.oj.core.OjConfig;
import com.power.oj.core.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.problem.ProblemModel;
import com.power.oj.solution.SolutionModel;

public class Judge extends Thread
{

  public static int threads = 0;
  public static Object mute = new Object();
  public static ArrayList<SolutionModel> judgeList = new ArrayList<SolutionModel>();
  public static final String DATA_EXT_IN = ".in";
  public static final String DATA_EXT_OUT = ".out";
  private static final Logger log = Logger.getLogger(Judge.class);

  public Judge()
  {
    start();
  }

  public void run()
  {
    while (true)
    {
      SolutionModel solutionModel = null;
      synchronized (judgeList)
      {
        if (judgeList.isEmpty())
        {
          threads = threads > 0 ? threads - 1 : 0;
          return;
        }
        log.info("Judge threads: " + threads);
        solutionModel = judgeList.get(0);
        judgeList.remove(0);
      }
      synchronized (mute)
      {
        try
        {
          if (Compile(solutionModel))
            RunProcess(solutionModel);
        } catch (Exception e)
        {
          solutionModel.set("result", ResultType.SE).set("system_error", e.getMessage());
          solutionModel.update();

          threads = threads > 0 ? threads - 1 : 0;
          log.error(e.getMessage());
        }
      }
    }
  }

  public boolean Compile(SolutionModel solutionModel) throws IOException
  {
    log.info(solutionModel.getInt("sid") + " Start compiling...");
    String workPath = OjConfig.get("work_path");
    workPath = new StringBand(2).append(FileNameUtil.normalizeNoEndSeparator(workPath)).append("\\").toString();
    // workPath = FileNameUtil.separatorsToSystem(workPath); //Converts all
    // separators to the system separator.
    if (OjConfig.getBoolean("delete_tmp_file"))
    {
      File prevWorkDir = new File(new StringBand(2).append(workPath).append(solutionModel.getInt("sid") - 2).toString());
      if (prevWorkDir.isDirectory())
      {
        FileUtil.deleteDir(prevWorkDir);
        log.info("Delete previous work directory " + prevWorkDir.getAbsolutePath());
      }
    }
    log.info("workPath: " + workPath);

    File workDir = new File(new StringBand(2).append(workPath).append(solutionModel.getInt("sid")).toString());
    FileUtil.mkdirs(workDir);
    log.info("workDir: " + workDir.getAbsolutePath());

    LanguageModel language = (LanguageModel) OjConfig.language_type.get(solutionModel.getInt("language"));
    File sourceFile = new File(new StringBand().append(workDir.getAbsolutePath()).append("\\Main.").append(language.getStr("ext")).toString());
    FileUtil.touch(sourceFile);
    FileUtil.writeString(sourceFile, solutionModel.getStr("source"));

    String comShellName = OjConfig.get("compile_shell");
    String compileCmdName = getCompileCmd(language.getStr("compile_cmd"), workDir.getAbsolutePath(), "Main", language.getStr("ext"));
    log.info("compileCmd: " + compileCmdName);

    Process compileProcess = Runtime.getRuntime().exec(comShellName);
    OutputStream comShellOutputStream = compileProcess.getOutputStream();
    comShellOutputStream.write(compileCmdName.getBytes());
    comShellOutputStream.flush();

    BufferedReader compileErrorBufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
    long startTime = System.currentTimeMillis();
    StringBand sb = new StringBand();
    String errorOutput = "";
    while ((startTime + 10000L > System.currentTimeMillis()) && ((errorOutput = compileErrorBufferedReader.readLine()) != null))
      sb.append(errorOutput).append("\n");
    compileErrorBufferedReader.close();

    try
    {
      compileProcess.waitFor();
    } catch (InterruptedException localInterruptedException)
    {
      // log
      log.warn("Compile Process is interrupted.");
    }

    File mainProgram = new File(new StringBand(3).append(workDir.getAbsolutePath()).append("\\Main.").append(language.getStr("exe")).toString());
    log.info(mainProgram.getAbsolutePath());
    boolean success = mainProgram.isFile();

    if (!success)
    {
      synchronized (mute)
      {
        // update DataBase
        solutionModel.set("result", ResultType.CE).set("time", 0).set("memory", 0).set("error", sb.toString());
        solutionModel.update();
      }
    }

    return success;
  }

  public boolean RunProcess(SolutionModel solutionModel) throws IOException, InterruptedException
  {
    log.info(solutionModel.getInt("sid") + " RunProcess...");
    Process runProcess = Runtime.getRuntime().exec(OjConfig.get("run_shell"));
    OutputStream runProcessOutputStream = runProcess.getOutputStream();
    log.info("runProcess: " + OjConfig.get("run_shell"));
    File dataDir = new File(new StringBand(3).append(OjConfig.get("data_path")).append("\\").append(solutionModel.getInt("pid")).toString());
    if (!dataDir.isDirectory())
    {
      String system_error = new StringBand(3).append("Data directory ").append(dataDir).append(" not exists.").toString();
      solutionModel.set("result", ResultType.SE).set("system_error", system_error);
      solutionModel.update();
      log.error(system_error);
      return false;
    }
    ProblemModel problemModel = ProblemModel.dao.findById(solutionModel.getInt("pid"), "time_limit,memory_limit");

    List<String> inFiles = new ArrayList<String>();
    List<String> outFiles = new ArrayList<String>();
    File[] arrayOfFile = dataDir.listFiles();

    for (int i = 0; i < arrayOfFile.length; i++)
    {
      File in_file = arrayOfFile[i];
      if (!in_file.getName().toLowerCase().endsWith(DATA_EXT_IN))
        continue;
      File out_file = new File(new StringBand().append(dataDir.getAbsolutePath()).append("\\")
          .append(in_file.getName().substring(0, in_file.getName().length() - DATA_EXT_IN.length())).append(DATA_EXT_OUT).toString());
      if (!out_file.isFile())
        continue;
      inFiles.add(in_file.getAbsolutePath());
      outFiles.add(out_file.getAbsolutePath());
    }
    int numOfData = inFiles.size();

    LanguageModel language = (LanguageModel) OjConfig.language_type.get(solutionModel.getInt("language"));
    long timeLimit = problemModel.getInt("time_limit") * language.getInt("time_factor") + numOfData * language.getInt("ext_time");
    long caseTimeLimit = problemModel.getInt("time_limit") * language.getInt("time_factor") + language.getInt("ext_time");
    runProcessOutputStream.write((timeLimit + "\n").getBytes());
    runProcessOutputStream.write((caseTimeLimit + "\n").getBytes());

    long memoryLimit = (problemModel.getInt("memory_limit") + language.getInt("ext_memory")) * 1024L;
    runProcessOutputStream.write((memoryLimit + "\n").getBytes());
    log.info("timeLimit: " + timeLimit);
    log.info("caseTimeLimit: " + caseTimeLimit);
    log.info("memoryLimit: " + memoryLimit);

    File workDir = new File(new StringBand(2).append(OjConfig.get("work_path")).append("\\").append(solutionModel.getInt("sid")).toString());
    String mainProgram = new StringBand(4).append(workDir.getAbsolutePath()).append("\\Main.").append(language.getStr("exe")).append("\n").toString();
    runProcessOutputStream.write(mainProgram.getBytes());
    runProcessOutputStream.write((workDir.getAbsolutePath() + "\n").getBytes());
    log.info("mainProgram: " + mainProgram);
    log.info("dataDir: " + dataDir.getAbsolutePath());

    runProcessOutputStream.write((numOfData + "\n").getBytes());
    log.info("data files: " + numOfData);
    if (numOfData < 1)
      log.warn("No data file for problem " + solutionModel.getInt("pid"));
    for (int i = 0; i < inFiles.size(); ++i)
    {
      runProcessOutputStream.write((inFiles.get(i) + "\n").getBytes());
      String userOutFile = new StringBand(4).append(workDir.getAbsolutePath()).append("\\").append(new File(outFiles.get(i)).getName()).append("\n").toString();
      runProcessOutputStream.write(userOutFile.getBytes());
      runProcessOutputStream.write((outFiles.get(i) + "\n").getBytes());
      log.info(inFiles.get(i));
      log.info(userOutFile);
      log.info(outFiles.get(i));
    }
    runProcessOutputStream.flush();

    BufferedReader inputStreamBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));

    String buff = inputStreamBufferedReader.readLine();
    log.info("original time: " + buff);
    int time = Integer.parseInt(buff);
    buff = inputStreamBufferedReader.readLine();
    log.info("original memory: " + buff);
    int memory = Integer.parseInt(buff);
    if (memory > 0)
      memory -= language.getInt("ext_memory");
    StringBand sb = new StringBand();

    InputStream errorStream = runProcess.getErrorStream();
    while (errorStream.available() > 0)
    {
      Character c = new Character((char) errorStream.read());
      sb.append(c);
    }
    String errorOut = sb.toString();
    log.info("errorOut: " + errorOut);

    runProcessOutputStream.close();
    inputStreamBufferedReader.close();
    errorStream.close();

    int result = runProcess.waitFor();
    result = runProcess.exitValue();
    log.info("original result: " + result);
    if (result != ResultType.AC && (errorOut.indexOf("Exception") != -1 || errorOut.indexOf("Traceback") != -1))
      result = ResultType.RE;
    if (result < 0)
    {
      result = ResultType.SE;
    }
    log.info("result: " + result + "  time: " + time + "  memory: " + memory);

    synchronized (mute)
    {
      int cid = solutionModel.getInt("cid");
      if (cid > 0)
      {
        int uid = solutionModel.getUid();
        int num = solutionModel.getInt("num");
        char c = (char) (num + 'A');
        StringBand message = new StringBand().append("UID: ").append(uid).append(" Problem: ").append(c).append(" result: ").append(result).append("  time: ")
            .append(time).append("  memory: ").append(memory);
        // OjMessageInbound.broadcast(message.toString());
        ContestRankWebSocket.broadcast(cid, message.toString());
      }
      // update DataBase
      solutionModel.set("result", result).set("time", time).set("memory", memory);
      return solutionModel.update();
    }
  }

  public String getCompileCmd(String compileCmd, String path, String name, String ext)
  {
    path = new StringBand(2).append(path).append("\\").toString();
    compileCmd = StringUtil.replace(compileCmd, "%PATH%", path);
    compileCmd = StringUtil.replace(compileCmd, "%NAME%", name);
    compileCmd = StringUtil.replace(compileCmd, "%EXT%", ext);
    compileCmd = new StringBand(2).append(compileCmd).append("\n").toString();

    return compileCmd;
  }
}
