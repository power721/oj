package com.power.oj.contest.model;

import com.jfinal.plugin.activerecord.Model;
import com.power.oj.core.OjConfig;

public class ContestModel extends Model<ContestModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = 2557500234914909223L;
  
  public static final ContestModel dao = new ContestModel();

  public static final String CID = "cid";
  public static final String UID = "uid";
  public static final String TITLE = "title";
  public static final String START_TIME = "startTime";
  public static final String END_TIME = "endTime";
  public static final String DESCRIPTION = "description";
  public static final String REPORT = "report";
  public static final String TYPE = "type";
  public static final String PASSWORD = "password";
  public static final String ATIME = "atime";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String FREEZE = "freeze";
  public static final String STATUS = "status";

  public static final int TYPE_PUBLIC = 0;
  public static final int TYPE_PASSWORD = 1;
  public static final int TYPE_PRIVATE = 2;
  public static final int TYPE_STRICT_PRIVATE = 3;
  public static final int TYPE_TEST = 4;

  public static final int PENDING = 0;
  public static final int RUNNING = 1;
  public static final int FINISHED = 2;

  public boolean saveContest()
  {
    setCtime(OjConfig.timeStamp);

    return save();
  }
  
  public boolean isPending()
  {
    return getStartTime() > OjConfig.timeStamp;
  }
  
  public boolean isFinished()
  {
    return getEndTime() < OjConfig.timeStamp;
  }
  
  public boolean isRunning()
  {
    return !isPending() && !isFinished();
  }

  public boolean hasPassword()
  {
    return getType() == ContestModel.TYPE_PASSWORD;
  }

  public boolean isPrivate()
  {
    return getType() == ContestModel.TYPE_PRIVATE;
  }

  public boolean isStrictPrivate()
  {
    return getType() == ContestModel.TYPE_STRICT_PRIVATE;
  }

  public boolean isTest()
  {
    return getType() == ContestModel.TYPE_TEST;
  }
  
  public boolean checkPassword(String password)
  {
    return hasPassword() && getPassword().equals(password);
  }

  public Integer getCid()
  {
    return getInt(CID);
  }
  
  public ContestModel setCid(Integer value)
  {
    return set(CID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public ContestModel setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getTitle()
  {
    return getStr(TITLE);
  }
  
  public ContestModel setTitle(String value)
  {
    return set(TITLE, value);
  }
  
  public Integer getStartTime()
  {
    return getInt(START_TIME);
  }
  
  public ContestModel setStartTime(Integer value)
  {
    return set(START_TIME, value);
  }
  
  public Integer getEndTime()
  {
    return getInt(END_TIME);
  }
  
  public ContestModel setEndTime(Integer value)
  {
    return set(END_TIME, value);
  }
  
  public String getDescription()
  {
    return getStr(DESCRIPTION);
  }
  
  public ContestModel setDescription(String value)
  {
    return set(DESCRIPTION, value);
  }

  public String getReport()
  {
    return getStr(REPORT);
  }
  
  public ContestModel setReport(String value)
  {
    return set(REPORT, value);
  }
  
  public Integer getType()
  {
    return getInt(TYPE);
  }
  
  public ContestModel setType(Integer value)
  {
    return set(TYPE, value);
  }
  
  public String getPassword()
  {
    return getStr(PASSWORD);
  }
  
  public ContestModel setPassword(String value)
  {
    return set(PASSWORD, value);
  }
  
  public Integer getAtime()
  {
    return getInt(ATIME);
  }
  
  public ContestModel setAtime(Integer value)
  {
    return set(ATIME, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public ContestModel setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public ContestModel setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
  public Boolean getFreeze()
  {
    return getBoolean(FREEZE);
  }
  
  public ContestModel setFreeze(Boolean value)
  {
    return set(FREEZE, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public ContestModel setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
