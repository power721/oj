package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Solution extends Model<Solution>
{
  private static final long serialVersionUID = 1L;
  
  public static final Solution dao = new Solution();
  
  public static final String SID = "sid";
  public static final String UID = "uid";
  public static final String PID = "pid";
  public static final String CID = "cid";
  public static final String NUM = "num";
  public static final String TIME = "time";
  public static final String MEMORY = "memory";
  public static final String RESULT = "result";
  public static final String LANGUAGE = "language";
  public static final String SOURCE = "source";
  public static final String CODE_LEN = "codeLen";
  public static final String ERROR = "error";
  public static final String SYSTEM_ERROR = "systemError";
  public static final String CTIME = "ctime";

  public <T> T getSid()
  {
    return get(SID);
  }
  
  public Solution setSid(Object value)
  {
    return set(SID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Solution setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getPid()
  {
    return get(PID);
  }
  
  public Solution setPid(Object value)
  {
    return set(PID, value);
  }
  
  public <T> T getCid()
  {
    return get(CID);
  }
  
  public Solution setCid(Object value)
  {
    return set(CID, value);
  }
  
  public <T> T getNum()
  {
    return get(NUM);
  }
  
  public Solution setNum(Object value)
  {
    return set(NUM, value);
  }
  
  public <T> T getTime()
  {
    return get(TIME);
  }
  
  public Solution setTime(Object value)
  {
    return set(TIME, value);
  }
  
  public <T> T getMemory()
  {
    return get(MEMORY);
  }
  
  public Solution setMemory(Object value)
  {
    return set(MEMORY, value);
  }
  
  public <T> T getResult()
  {
    return get(RESULT);
  }
  
  public Solution setResult(Object value)
  {
    return set(RESULT, value);
  }
  
  public <T> T getLanguage()
  {
    return get(LANGUAGE);
  }
  
  public Solution setLanguage(Object value)
  {
    return set(LANGUAGE, value);
  }
  
  public <T> T getSource()
  {
    return get(SOURCE);
  }
  
  public Solution setSource(Object value)
  {
    return set(SOURCE, value);
  }
  
  public <T> T getCodeLen()
  {
    return get(CODE_LEN);
  }
  
  public Solution setCodeLen(Object value)
  {
    return set(CODE_LEN, value);
  }
  
  public <T> T getError()
  {
    return get(ERROR);
  }
  
  public Solution setError(Object value)
  {
    return set(ERROR, value);
  }
  
  public <T> T getSystemError()
  {
    return get(SYSTEM_ERROR);
  }
  
  public Solution setSystemError(Object value)
  {
    return set(SYSTEM_ERROR, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Solution setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
}
