package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Comment extends Model<Comment>
{
  private static final long serialVersionUID = 1L;
  
  public static final Comment dao = new Comment();
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String THREAD_ID = "threadId";
  public static final String QUOTE_ID = "quoteId";
  public static final String CONTENT = "content";
  public static final String IP = "ip";
  public static final String CTIME = "ctime";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public Comment setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public Comment setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getThreadId()
  {
    return getInt(THREAD_ID);
  }
  
  public Comment setThreadId(Integer value)
  {
    return set(THREAD_ID, value);
  }
  
  public Integer getQuoteId()
  {
    return getInt(QUOTE_ID);
  }
  
  public Comment setQuoteId(Integer value)
  {
    return set(QUOTE_ID, value);
  }
  
  public String getContent()
  {
    return getStr(CONTENT);
  }
  
  public Comment setContent(String value)
  {
    return set(CONTENT, value);
  }
  
  public String getIp()
  {
    return getStr(IP);
  }
  
  public Comment setIp(String value)
  {
    return set(IP, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Comment setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
