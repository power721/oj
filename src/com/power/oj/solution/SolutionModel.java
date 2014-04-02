package com.power.oj.solution;

import com.jfinal.plugin.activerecord.Model;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.bean.ResultType;
public class SolutionModel extends Model<SolutionModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = -287562753616099282L;

  public static final SolutionModel dao = new SolutionModel();
  
  public static final String TABLE_NAME = "solution";
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
  
  public SolutionModel build(ContestSolutionModel contestSolution)
  {
    for (String filed : contestSolution.getAttrNames())
    {
      set(filed, contestSolution.get(filed));
    }
    return this;
  }
  
  public boolean addSolution()
  {
    setCtime(OjConfig.timeStamp);
    setResult(ResultType.WAIT);
    setTime(0);
    setMemory(0);
    setCodeLen(getSource().length());
    if (getCodeLen() < 10 || getCodeLen() > 30000)
    {
      // TODO throw exception or validator
      return false;
    }

    return save();
  }

  public Integer getSid()
  {
    return getInt(SID);
  }
  
  public SolutionModel setSid(Integer value)
  {
    return set(SID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public SolutionModel setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public SolutionModel setPid(Integer value)
  {
    return set(PID, value);
  }
  
  public Integer getCid()
  {
    return getInt(CID);
  }
  
  public SolutionModel setCid(Integer value)
  {
    return set(CID, value);
  }
  
  public Integer getNum()
  {
    return getInt(NUM);
  }
  
  public SolutionModel setNum(Integer value)
  {
    return set(NUM, value);
  }
  
  public Integer getTime()
  {
    return getInt(TIME);
  }
  
  public SolutionModel setTime(Integer value)
  {
    return set(TIME, value);
  }
  
  public Integer getMemory()
  {
    return getInt(MEMORY);
  }
  
  public SolutionModel setMemory(Integer value)
  {
    return set(MEMORY, value);
  }
  
  public Integer getResult()
  {
    return getInt(RESULT);
  }
  
  public SolutionModel setResult(Integer value)
  {
    return set(RESULT, value);
  }
  
  public Integer getLanguage()
  {
    return getInt(LANGUAGE);
  }
  
  public SolutionModel setLanguage(Integer value)
  {
    return set(LANGUAGE, value);
  }
  
  public String getSource()
  {
    return getStr(SOURCE);
  }
  
  public SolutionModel setSource(String value)
  {
    return set(SOURCE, value);
  }
  
  public Integer getCodeLen()
  {
    return getInt(CODE_LEN);
  }
  
  public SolutionModel setCodeLen(Integer value)
  {
    return set(CODE_LEN, value);
  }
  
  public String getError()
  {
    return getStr(ERROR);
  }
  
  public SolutionModel setError(String value)
  {
    return set(ERROR, value);
  }
  
  public String getSystemError()
  {
    return getStr(SYSTEM_ERROR);
  }
  
  public SolutionModel setSystemError(String value)
  {
    return set(SYSTEM_ERROR, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public SolutionModel setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
