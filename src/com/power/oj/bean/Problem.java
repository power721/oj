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

  public <T> T getPid()
  {
    return get(PID);
  }
  
  public Problem setPid(Object value)
  {
    return set(PID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Problem setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getTitle()
  {
    return get(TITLE);
  }
  
  public Problem setTitle(Object value)
  {
    return set(TITLE, value);
  }
  
  public <T> T getDescription()
  {
    return get(DESCRIPTION);
  }
  
  public Problem setDescription(Object value)
  {
    return set(DESCRIPTION, value);
  }
  
  public <T> T getInput()
  {
    return get(INPUT);
  }
  
  public Problem setInput(Object value)
  {
    return set(INPUT, value);
  }
  
  public <T> T getOutput()
  {
    return get(OUTPUT);
  }
  
  public Problem setOutput(Object value)
  {
    return set(OUTPUT, value);
  }
  
  public <T> T getSampleInput()
  {
    return get(SAMPLE_INPUT);
  }
  
  public Problem setSampleInput(Object value)
  {
    return set(SAMPLE_INPUT, value);
  }
  
  public <T> T getSampleOutput()
  {
    return get(SAMPLE_OUTPUT);
  }
  
  public Problem setSampleOutput(Object value)
  {
    return set(SAMPLE_OUTPUT, value);
  }
  
  public <T> T getHint()
  {
    return get(HINT);
  }
  
  public Problem setHint(Object value)
  {
    return set(HINT, value);
  }
  
  public <T> T getSource()
  {
    return get(SOURCE);
  }
  
  public Problem setSource(Object value)
  {
    return set(SOURCE, value);
  }
  
  public <T> T getSampleProgram()
  {
    return get(SAMPLE_PROGRAM);
  }
  
  public Problem setSampleProgram(Object value)
  {
    return set(SAMPLE_PROGRAM, value);
  }
  
  public <T> T getTimeLimit()
  {
    return get(TIME_LIMIT);
  }
  
  public Problem setTimeLimit(Object value)
  {
    return set(TIME_LIMIT, value);
  }
  
  public <T> T getMemoryLimit()
  {
    return get(MEMORY_LIMIT);
  }
  
  public Problem setMemoryLimit(Object value)
  {
    return set(MEMORY_LIMIT, value);
  }
  
  public <T> T getAtime()
  {
    return get(ATIME);
  }
  
  public Problem setAtime(Object value)
  {
    return set(ATIME, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Problem setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
  public <T> T getMtime()
  {
    return get(MTIME);
  }
  
  public Problem setMtime(Object value)
  {
    return set(MTIME, value);
  }
  
  public <T> T getStime()
  {
    return get(STIME);
  }
  
  public Problem setStime(Object value)
  {
    return set(STIME, value);
  }
  
  public <T> T getAccepted()
  {
    return get(ACCEPTED);
  }
  
  public Problem setAccepted(Object value)
  {
    return set(ACCEPTED, value);
  }
  
  public <T> T getSolved()
  {
    return get(SOLVED);
  }
  
  public Problem setSolved(Object value)
  {
    return set(SOLVED, value);
  }
  
  public <T> T getSubmission()
  {
    return get(SUBMISSION);
  }
  
  public Problem setSubmission(Object value)
  {
    return set(SUBMISSION, value);
  }
  
  public <T> T getSubmitUser()
  {
    return get(SUBMIT_USER);
  }
  
  public Problem setSubmitUser(Object value)
  {
    return set(SUBMIT_USER, value);
  }
  
  public <T> T getError()
  {
    return get(ERROR);
  }
  
  public Problem setError(Object value)
  {
    return set(ERROR, value);
  }
  
  public <T> T getRatio()
  {
    return get(RATIO);
  }
  
  public Problem setRatio(Object value)
  {
    return set(RATIO, value);
  }
  
  public <T> T getDifficulty()
  {
    return get(DIFFICULTY);
  }
  
  public Problem setDifficulty(Object value)
  {
    return set(DIFFICULTY, value);
  }
  
  public <T> T getView()
  {
    return get(VIEW);
  }
  
  public Problem setView(Object value)
  {
    return set(VIEW, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Problem setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
