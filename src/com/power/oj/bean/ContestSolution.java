package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class ContestSolution extends Model<ContestSolution>
{
  private static final long serialVersionUID = 1L;
  
  public static final ContestSolution dao = new ContestSolution();
  
  public static final String TABLE_NAME = "contest_solution";
  public static final String SID = "sid";
  public static final String UID = "uid";
  public static final String PID = "pid";
  public static final String CID = "cid";
  public static final String NUM = "num";
  public static final String TIME = "time";
  public static final String MEMORY = "memory";
  public static final String RESULT = "result";
  public static final String LANGUAGE = "language";
  public static final String CTIME = "ctime";
  public static final String ERROR = "error";
  public static final String SOURCE = "source";
  public static final String CODE_LEN = "codeLen";
  public static final String SYSTEM_ERROR = "systemError";

  /*
   * auto generated getter and setter
   */
  public Integer getSid()
  {
    return getInt(SID);
  }
  
  public ContestSolution setSid(Integer value)
  {
    return set(SID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public ContestSolution setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public ContestSolution setPid(Integer value)
  {
    return set(PID, value);
  }
  
  public Integer getCid()
  {
    return getInt(CID);
  }
  
  public ContestSolution setCid(Integer value)
  {
    return set(CID, value);
  }
  
  public Integer getNum()
  {
    return getInt(NUM);
  }
  
  public ContestSolution setNum(Integer value)
  {
    return set(NUM, value);
  }
  
  public Integer getTime()
  {
    return getInt(TIME);
  }
  
  public ContestSolution setTime(Integer value)
  {
    return set(TIME, value);
  }
  
  public Integer getMemory()
  {
    return getInt(MEMORY);
  }
  
  public ContestSolution setMemory(Integer value)
  {
    return set(MEMORY, value);
  }
  
  public Integer getResult()
  {
    return getInt(RESULT);
  }
  
  public ContestSolution setResult(Integer value)
  {
    return set(RESULT, value);
  }
  
  public Integer getLanguage()
  {
    return getInt(LANGUAGE);
  }
  
  public ContestSolution setLanguage(Integer value)
  {
    return set(LANGUAGE, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public ContestSolution setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public String getError()
  {
    return getStr(ERROR);
  }
  
  public ContestSolution setError(String value)
  {
    return set(ERROR, value);
  }
  
  public String getSource()
  {
    return getStr(SOURCE);
  }
  
  public ContestSolution setSource(String value)
  {
    return set(SOURCE, value);
  }
  
  public Integer getCodeLen()
  {
    return getInt(CODE_LEN);
  }
  
  public ContestSolution setCodeLen(Integer value)
  {
    return set(CODE_LEN, value);
  }
  
  public String getSystemError()
  {
    return getStr(SYSTEM_ERROR);
  }
  
  public ContestSolution setSystemError(String value)
  {
    return set(SYSTEM_ERROR, value);
  }
  
}
