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

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Tag setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public Tag setPid(Integer value)
  {
    return set(PID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public Tag setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getTag()
  {
    return getStr(TAG);
  }
  
  public Tag setTag(String value)
  {
    return set(TAG, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Tag setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public Tag setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public Tag setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
