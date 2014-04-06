package com.power.oj.judge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jodd.io.FileNameUtil;
import jodd.util.StringUtil;

import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;

public class UestcJudgeAdapter extends JudgeAdapter
{

  @Override
  protected boolean Compile() throws IOException
  {
    return true;
  }

  @Override
  protected boolean RunProcess() throws IOException, InterruptedException
  {
    log.info(solutionModel.getSid() + " RunProcess...");
    int numOfData = getDataFiles();

    int i;
    long timeLimit = problemModel.getTimeLimit();
    long memoryLimit = problemModel.getMemoryLimit();
    long outputLimit = 8192L;
    boolean isSpj = problemService.checkSpj(solutionModel.getPid());
    
    log.info("data files: " + numOfData);
    if (numOfData < 1)
    {
      log.warn("No data file for problem " + solutionModel.getPid());
    }
    boolean isAccepted = true;
    for (i = 0; isAccepted && i < numOfData; ++i)
    {
      String cmd = buildCommand(timeLimit, memoryLimit, outputLimit, isSpj,
          FileNameUtil.getName(inFiles.get(i)), FileNameUtil.getName(outFiles.get(i)), i == 0);
      Process process = Runtime.getRuntime().exec(cmd);
      InputStream inputStream = process.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      StringBuilder stringBuilder = new StringBuilder();
      while ((line = bufferedReader.readLine()) != null)
      {
        stringBuilder.append(line);
      }
      bufferedReader.close();

      StringBuilder sb = new StringBuilder();
      InputStream errorStream = process.getErrorStream();
      while (errorStream.available() > 0)
      {
        Character c = new Character((char) errorStream.read());
        sb.append(c);
      }

      String[] resultStr = stringBuilder.toString().split(" ");
      int exitValue = process.waitFor();
      if (exitValue > 0)
      {
        updateSystemError(sb.toString());
        return false;
      }
      isAccepted = checkResult(resultStr, sb.toString());
    }

    synchronized (JudgeAdapter.class)
    {
      updateResult(isAccepted, i);
      updateUser();
      if (!updateContest())
      {
        updateProblem();
      }
    }
    return true;
  }

  /**
   * Build the pylon core judging command line content.
   * 
   * @return command line content
   */
  private String buildCommand(long timeLimit, long memoryLimit, long outputLimit, boolean isSpj, String inputFile, String outputFile,
      boolean firstCase)
  {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(OjConfig.get("runShell"));
    // stringBuilder.append("/pyloncore ");
    stringBuilder.append(" -u ");
    stringBuilder.append(solutionModel.getSid());
    stringBuilder.append(" -s ");
    stringBuilder.append(sourceFile.getName());
    stringBuilder.append(" -n ");
    stringBuilder.append(solutionModel.getPid());
    stringBuilder.append(" -D ");
    stringBuilder.append(FileNameUtil.normalizeNoEndSeparator(OjConfig.get("dataPath")));
    stringBuilder.append(" -d ");
    stringBuilder.append(FileNameUtil.normalizeNoEndSeparator(workPath));
    stringBuilder.append(" -t ");
    stringBuilder.append(timeLimit);
    stringBuilder.append(" -m ");
    stringBuilder.append(memoryLimit);
    stringBuilder.append(" -o ");
    stringBuilder.append(outputLimit);
    if (isSpj)
      stringBuilder.append(" -S");
    stringBuilder.append(" -l ");
    stringBuilder.append(solutionModel.getLanguage());
    stringBuilder.append(" -I ");
    stringBuilder.append(inputFile);
    stringBuilder.append(" -O ");
    stringBuilder.append(outputFile);
    if (firstCase)
      stringBuilder.append(" -C");
    System.out.println(stringBuilder.toString());
    return stringBuilder.toString();
  }

  private boolean checkResult(String[] resultStr, String errorOut)
  {
    boolean isAccepted = true;
    if (resultStr != null && resultStr.length >= 3)
    {
      try
      {
        int result = convertResult(Integer.parseInt(resultStr[0]));
        if (result == ResultType.AC)
        {
          result = ResultType.RUN;
        }
        else
        {
          isAccepted = false;
        }
        int time = Integer.parseInt(resultStr[1]);
        int memory = Integer.parseInt(resultStr[2]);
        setResult(result, time, memory);
      } catch (NumberFormatException e) 
      {
        updateSystemError(e.getLocalizedMessage());
        
        if (OjConfig.getDevMode())
          e.printStackTrace();
        log.error(e.getLocalizedMessage());
        isAccepted = false;
      }
    } else
    {
      updateSystemError(errorOut);
      isAccepted = false;
    }
    
    if (solutionModel.getResult() == ResultType.CE)
    {
      updateCompileError(readError("stderr_compiler.txt"));
    }
    else if (solutionModel.getResult() == ResultType.RE)
    {
      if (StringUtil.isBlank(errorOut))
      {
        errorOut = readError("stderr_executive.txt");
      }
      updateRuntimeError(errorOut);
    }
    return isAccepted;
  }
  
  private String readError(String fileName)
  {
    StringBuilder sb = new StringBuilder();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(workDirPath + "/" + fileName));
      String line;
      while ((line = br.readLine()) != null) {
        if (line.trim().startsWith(workPath)) {
          line = line.substring(workPath.length());
        }
        sb.append(line).append('\n');
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException ignored) {
        }
      }
    }
    
    return sb.length()>0 ? sb.toString() : null;
  }
  
  private int convertResult(int result)
  {
    /*
    const int OJ_WAIT           = 0; //Queue
    const int OJ_AC             = 1; //Accepted
    const int OJ_PE             = 2; //Presentation Error
    const int OJ_TLE            = 3; //Time Limit Exceeded
    const int OJ_MLE            = 4; //Memory Limit Exceeded
    const int OJ_WA             = 5; //Wrong Answer
    const int OJ_OLE            = 6; //Output Limit Exceeded
    const int OJ_CE             = 7; //Compilation Error
    const int OJ_RE_SEGV        = 8; //Segment Violation
    const int OJ_RE_FPE         = 9; //FPU Error
    const int OJ_RE_BUS         = 10;//Bus Error
    const int OJ_RE_ABRT        = 11;//Abort
    const int OJ_RE_UNKNOWN     = 12;//Unknow
    const int OJ_RF             = 13;//Restricted Function
    const int OJ_SE             = 14;//System Error
    const int OJ_RE_JAVA        = 15;//JAVA Run Time Exception
    */
    switch (result)
    {
      case 0: return ResultType.WAIT;
      case 1: return ResultType.AC;
      case 2: return ResultType.PE;
      case 3: return ResultType.TLE;
      case 4: return ResultType.MLE;
      case 5: return ResultType.WA;
      case 6: return ResultType.OLE;
      case 7: return ResultType.CE;
      case 8:
      case 9:
      case 10:
      case 11:
      case 12: return ResultType.RE;
      case 13: return ResultType.RF;
      case 14: return ResultType.SE;
      case 15: return ResultType.RE;
      default: return ResultType.SE;
    }
  }
  
}
