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

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Comment setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Comment setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getThreadId()
  {
    return get(THREAD_ID);
  }
  
  public Comment setThreadId(Object value)
  {
    return set(THREAD_ID, value);
  }
  
  public <T> T getQuoteId()
  {
    return get(QUOTE_ID);
  }
  
  public Comment setQuoteId(Object value)
  {
    return set(QUOTE_ID, value);
  }
  
  public <T> T getContent()
  {
    return get(CONTENT);
  }
  
  public Comment setContent(Object value)
  {
    return set(CONTENT, value);
  }
  
  public <T> T getIp()
  {
    return get(IP);
  }
  
  public Comment setIp(Object value)
  {
    return set(IP, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Comment setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
}
