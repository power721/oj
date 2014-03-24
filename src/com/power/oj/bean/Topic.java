package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Topic extends Model<Topic>
{
  private static final long serialVersionUID = 1L;
  
  public static final Topic dao = new Topic();
  
  public static final String TABLE_NAME = "topic";
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String PID = "pid";
  public static final String TITLE = "title";
  public static final String CONTENT = "content";
  public static final String VIEW = "view";
  public static final String ATIME = "atime";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String STATUS = "status";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Topic setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public Topic setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public Topic setPid(Integer value)
  {
    return set(PID, value);
  }
  
  public String getTitle()
  {
    return getStr(TITLE);
  }
  
  public Topic setTitle(String value)
  {
    return set(TITLE, value);
  }
  
  public String getContent()
  {
    return getStr(CONTENT);
  }
  
  public Topic setContent(String value)
  {
    return set(CONTENT, value);
  }
  
  public Integer getView()
  {
    return getInt(VIEW);
  }
  
  public Topic setView(Integer value)
  {
    return set(VIEW, value);
  }
  
  public Integer getAtime()
  {
    return getInt(ATIME);
  }
  
  public Topic setAtime(Integer value)
  {
    return set(ATIME, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Topic setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public Topic setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public Topic setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
