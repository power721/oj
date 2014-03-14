package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Contest extends Model<Contest>
{
  private static final long serialVersionUID = 1L;
  
  public static final Contest dao = new Contest();
  
  public static final String CID = "cid";
  public static final String UID = "uid";
  public static final String TITLE = "title";
  public static final String START_TIME = "startTime";
  public static final String END_TIME = "endTime";
  public static final String DESCRIPTION = "description";
  public static final String TYPE = "type";
  public static final String PASSWORD = "password";
  public static final String ATIME = "atime";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String FREEZE = "freeze";
  public static final String STATUS = "status";

  public <T> T getCid()
  {
    return get(CID);
  }
  
  public Contest setCid(Object value)
  {
    return set(CID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Contest setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getTitle()
  {
    return get(TITLE);
  }
  
  public Contest setTitle(Object value)
  {
    return set(TITLE, value);
  }
  
  public <T> T getStartTime()
  {
    return get(START_TIME);
  }
  
  public Contest setStartTime(Object value)
  {
    return set(START_TIME, value);
  }
  
  public <T> T getEndTime()
  {
    return get(END_TIME);
  }
  
  public Contest setEndTime(Object value)
  {
    return set(END_TIME, value);
  }
  
  public <T> T getDescription()
  {
    return get(DESCRIPTION);
  }
  
  public Contest setDescription(Object value)
  {
    return set(DESCRIPTION, value);
  }
  
  public <T> T getType()
  {
    return get(TYPE);
  }
  
  public Contest setType(Object value)
  {
    return set(TYPE, value);
  }
  
  public <T> T getPassword()
  {
    return get(PASSWORD);
  }
  
  public Contest setPassword(Object value)
  {
    return set(PASSWORD, value);
  }
  
  public <T> T getAtime()
  {
    return get(ATIME);
  }
  
  public Contest setAtime(Object value)
  {
    return set(ATIME, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Contest setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
  public <T> T getMtime()
  {
    return get(MTIME);
  }
  
  public Contest setMtime(Object value)
  {
    return set(MTIME, value);
  }
  
  public <T> T getFreeze()
  {
    return get(FREEZE);
  }
  
  public Contest setFreeze(Object value)
  {
    return set(FREEZE, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Contest setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
