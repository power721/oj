package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Contest extends Model<Contest>
{
  private static final long serialVersionUID = 1L;
  
  public static final Contest dao = new Contest();
  
  public static final String TABLE_NAME = "contest";
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

  public Integer getCid()
  {
    return getInt(CID);
  }
  
  public Contest setCid(Integer value)
  {
    return set(CID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public Contest setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getTitle()
  {
    return getStr(TITLE);
  }
  
  public Contest setTitle(String value)
  {
    return set(TITLE, value);
  }
  
  public Integer getStartTime()
  {
    return getInt(START_TIME);
  }
  
  public Contest setStartTime(Integer value)
  {
    return set(START_TIME, value);
  }
  
  public Integer getEndTime()
  {
    return getInt(END_TIME);
  }
  
  public Contest setEndTime(Integer value)
  {
    return set(END_TIME, value);
  }
  
  public String getDescription()
  {
    return getStr(DESCRIPTION);
  }
  
  public Contest setDescription(String value)
  {
    return set(DESCRIPTION, value);
  }
  
  public String getReport()
  {
    return getStr(REPORT);
  }
  
  public Contest setReport(String value)
  {
    return set(REPORT, value);
  }
  
  public Integer getType()
  {
    return getInt(TYPE);
  }
  
  public Contest setType(Integer value)
  {
    return set(TYPE, value);
  }
  
  public String getPassword()
  {
    return getStr(PASSWORD);
  }
  
  public Contest setPassword(String value)
  {
    return set(PASSWORD, value);
  }
  
  public Integer getAtime()
  {
    return getInt(ATIME);
  }
  
  public Contest setAtime(Integer value)
  {
    return set(ATIME, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Contest setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public Contest setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
  public Boolean getFreeze()
  {
    return getBoolean(FREEZE);
  }
  
  public Contest setFreeze(Boolean value)
  {
    return set(FREEZE, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public Contest setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
