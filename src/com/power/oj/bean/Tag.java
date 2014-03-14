package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Tag extends Model<Tag>
{
  private static final long serialVersionUID = 1L;
  
  public static final Tag dao = new Tag();
  
  public static final String ID = "id";
  public static final String PID = "pid";
  public static final String UID = "uid";
  public static final String TAG = "tag";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String STATUS = "status";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Tag setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getPid()
  {
    return get(PID);
  }
  
  public Tag setPid(Object value)
  {
    return set(PID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Tag setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getTag()
  {
    return get(TAG);
  }
  
  public Tag setTag(Object value)
  {
    return set(TAG, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Tag setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
  public <T> T getMtime()
  {
    return get(MTIME);
  }
  
  public Tag setMtime(Object value)
  {
    return set(MTIME, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Tag setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
