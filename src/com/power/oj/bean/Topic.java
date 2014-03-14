package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Topic extends Model<Topic>
{
  private static final long serialVersionUID = 1L;
  
  public static final Topic dao = new Topic();
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String PID = "pid";
  public static final String TITLE = "title";
  public static final String CONTENT = "content";
  public static final String ATIME = "atime";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String STATUS = "status";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Topic setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Topic setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getPid()
  {
    return get(PID);
  }
  
  public Topic setPid(Object value)
  {
    return set(PID, value);
  }
  
  public <T> T getTitle()
  {
    return get(TITLE);
  }
  
  public Topic setTitle(Object value)
  {
    return set(TITLE, value);
  }
  
  public <T> T getContent()
  {
    return get(CONTENT);
  }
  
  public Topic setContent(Object value)
  {
    return set(CONTENT, value);
  }
  
  public <T> T getAtime()
  {
    return get(ATIME);
  }
  
  public Topic setAtime(Object value)
  {
    return set(ATIME, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Topic setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
  public <T> T getMtime()
  {
    return get(MTIME);
  }
  
  public Topic setMtime(Object value)
  {
    return set(MTIME, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Topic setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
