package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Notice extends Model<Notice>
{
  private static final long serialVersionUID = 1L;
  
  public static final Notice dao = new Notice();
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String EDITOR_UID = "editorUid";
  public static final String CID = "cid";
  public static final String TITLE = "title";
  public static final String START_TIME = "startTime";
  public static final String END_TIME = "endTime";
  public static final String CONTENT = "content";
  public static final String ATIME = "atime";
  public static final String CTIME = "ctime";
  public static final String MTIME = "mtime";
  public static final String VIEW = "view";
  public static final String STATUS = "status";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Notice setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Notice setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getEditorUid()
  {
    return get(EDITOR_UID);
  }
  
  public Notice setEditorUid(Object value)
  {
    return set(EDITOR_UID, value);
  }
  
  public <T> T getCid()
  {
    return get(CID);
  }
  
  public Notice setCid(Object value)
  {
    return set(CID, value);
  }
  
  public <T> T getTitle()
  {
    return get(TITLE);
  }
  
  public Notice setTitle(Object value)
  {
    return set(TITLE, value);
  }
  
  public <T> T getStartTime()
  {
    return get(START_TIME);
  }
  
  public Notice setStartTime(Object value)
  {
    return set(START_TIME, value);
  }
  
  public <T> T getEndTime()
  {
    return get(END_TIME);
  }
  
  public Notice setEndTime(Object value)
  {
    return set(END_TIME, value);
  }
  
  public <T> T getContent()
  {
    return get(CONTENT);
  }
  
  public Notice setContent(Object value)
  {
    return set(CONTENT, value);
  }
  
  public <T> T getAtime()
  {
    return get(ATIME);
  }
  
  public Notice setAtime(Object value)
  {
    return set(ATIME, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Notice setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
  public <T> T getMtime()
  {
    return get(MTIME);
  }
  
  public Notice setMtime(Object value)
  {
    return set(MTIME, value);
  }
  
  public <T> T getView()
  {
    return get(VIEW);
  }
  
  public Notice setView(Object value)
  {
    return set(VIEW, value);
  }
  
  public <T> T getStatus()
  {
    return get(STATUS);
  }
  
  public Notice setStatus(Object value)
  {
    return set(STATUS, value);
  }
  
}
