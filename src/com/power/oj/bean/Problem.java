package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Problem extends Model<Problem>
{
  private static final long serialVersionUID = 1L;
  
  public static final Problem dao = new Problem();
  
  public static final String PID = "pid";
  public static final String UID = "uid";
  public static final String TITLE = "title";
  public static final String DESCRIPTION = "description";
  public static final String INPUT = "input";
  public static final String OUTPUT = "output";
  public static final String SAMPLE_INPUT = "sampleInput";
  public static final String SAMPLE_OUTPUT = "sampleOutput";
  public static final String HINT = "hint";
  public static final String SOURCE = "source";
  public static final String SAMPLE_PROGRAM = "sampleProgram";
  public static final String TIME_LIMIT = "timeLimit";
  public static final String MEMORY_LIMIT = "memoryLimit";
  public static final String ATIME = "atime";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String STIME = "stime";
  public static final String ACCEPTED = "accepted";
  public static final String SOLVED = "solved";
  public static final String SUBMISSION = "submission";
  public static final String SUBMIT_USER = "submitUser";
  public static final String ERROR = "error";
  public static final String RATIO = "ratio";
  public static final String DIFFICULTY = "difficulty";
  public static final String VIEW = "view";
  public static final String STATUS = "status";

  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public Problem setPid(Integer value)
  {
    return set(PID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public Problem setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getTitle()
  {
    return getStr(TITLE);
  }
  
  public Problem setTitle(String value)
  {
    return set(TITLE, value);
  }
  
  public String getDescription()
  {
    return getStr(DESCRIPTION);
  }
  
  public Problem setDescription(String value)
  {
    return set(DESCRIPTION, value);
  }
  
  public String getInput()
  {
    return getStr(INPUT);
  }
  
  public Problem setInput(String value)
  {
    return set(INPUT, value);
  }
  
  public String getOutput()
  {
    return getStr(OUTPUT);
  }
  
  public Problem setOutput(String value)
  {
    return set(OUTPUT, value);
  }
  
  public String getSampleInput()
  {
    return getStr(SAMPLE_INPUT);
  }
  
  public Problem setSampleInput(String value)
  {
    return set(SAMPLE_INPUT, value);
  }
  
  public String getSampleOutput()
  {
    return getStr(SAMPLE_OUTPUT);
  }
  
  public Problem setSampleOutput(String value)
  {
    return set(SAMPLE_OUTPUT, value);
  }
  
  public String getHint()
  {
    return getStr(HINT);
  }
  
  public Problem setHint(String value)
  {
    return set(HINT, value);
  }
  
  public String getSource()
  {
    return getStr(SOURCE);
  }
  
  public Problem setSource(String value)
  {
    return set(SOURCE, value);
  }
  
  public String getSampleProgram()
  {
    return getStr(SAMPLE_PROGRAM);
  }
  
  public Problem setSampleProgram(String value)
  {
    return set(SAMPLE_PROGRAM, value);
  }
  
  public Integer getTimeLimit()
  {
    return getInt(TIME_LIMIT);
  }
  
  public Problem setTimeLimit(Integer value)
  {
    return set(TIME_LIMIT, value);
  }
  
  public Integer getMemoryLimit()
  {
    return getInt(MEMORY_LIMIT);
  }
  
  public Problem setMemoryLimit(Integer value)
  {
    return set(MEMORY_LIMIT, value);
  }
  
  public Integer getAtime()
  {
    return getInt(ATIME);
  }
  
  public Problem setAtime(Integer value)
  {
    return set(ATIME, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Problem setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public Problem setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
  public Integer getStime()
  {
    return getInt(STIME);
  }
  
  public Problem setStime(Integer value)
  {
    return set(STIME, value);
  }
  
  public Integer getAccepted()
  {
    return getInt(ACCEPTED);
  }
  
  public Problem setAccepted(Integer value)
  {
    return set(ACCEPTED, value);
  }
  
  public Integer getSolved()
  {
    return getInt(SOLVED);
  }
  
  public Problem setSolved(Integer value)
  {
    return set(SOLVED, value);
  }
  
  public Integer getSubmission()
  {
    return getInt(SUBMISSION);
  }
  
  public Problem setSubmission(Integer value)
  {
    return set(SUBMISSION, value);
  }
  
  public Integer getSubmitUser()
  {
    return getInt(SUBMIT_USER);
  }
  
  public Problem setSubmitUser(Integer value)
  {
    return set(SUBMIT_USER, value);
  }
  
  public Integer getError()
  {
    return getInt(ERROR);
  }
  
  public Problem setError(Integer value)
  {
    return set(ERROR, value);
  }
  
  public Integer getRatio()
  {
    return getInt(RATIO);
  }
  
  public Problem setRatio(Integer value)
  {
    return set(RATIO, value);
  }
  
  public Integer getDifficulty()
  {
    return getInt(DIFFICULTY);
  }
  
  public Problem setDifficulty(Integer value)
  {
    return set(DIFFICULTY, value);
  }
  
  public Integer getView()
  {
    return getInt(VIEW);
  }
  
  public Problem setView(Integer value)
  {
    return set(VIEW, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public Problem setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
