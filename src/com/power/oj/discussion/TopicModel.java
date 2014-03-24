package com.power.oj.discussion;

import com.jfinal.plugin.activerecord.Model;

public class TopicModel extends Model<TopicModel>
{
  private static final long serialVersionUID = 1L;
  
  public static final TopicModel dao = new TopicModel();
  
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

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public TopicModel setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public TopicModel setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getPid()
  {
    return getInt(PID);
  }
  
  public TopicModel setPid(Integer value)
  {
    return set(PID, value);
  }
  
  public String getTitle()
  {
    return getStr(TITLE);
  }
  
  public TopicModel setTitle(String value)
  {
    return set(TITLE, value);
  }
  
  public String getContent()
  {
    return getStr(CONTENT);
  }
  
  public TopicModel setContent(String value)
  {
    return set(CONTENT, value);
  }

  public Integer getView()
  {
    return getInt(VIEW);
  }
  
  public TopicModel setView(Integer value)
  {
    return set(VIEW, value);
  }
  
  public Integer getAtime()
  {
    return getInt(ATIME);
  }
  
  public TopicModel setAtime(Integer value)
  {
    return set(ATIME, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public TopicModel setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getMtime()
  {
    return getInt(MTIME);
  }
  
  public TopicModel setMtime(Integer value)
  {
    return set(MTIME, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public TopicModel setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
}
