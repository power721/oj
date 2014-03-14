package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class MailContent extends Model<MailContent>
{
  private static final long serialVersionUID = 1L;
  
  public static final MailContent dao = new MailContent();
  
  public static final String ID = "id";
  public static final String FROM_UID = "fromUid";
  public static final String TO_UID = "toUid";
  public static final String CONTENT = "content";
  public static final String CTIME = "ctime";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public MailContent setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getFromUid()
  {
    return get(FROM_UID);
  }
  
  public MailContent setFromUid(Object value)
  {
    return set(FROM_UID, value);
  }
  
  public <T> T getToUid()
  {
    return get(TO_UID);
  }
  
  public MailContent setToUid(Object value)
  {
    return set(TO_UID, value);
  }
  
  public <T> T getContent()
  {
    return get(CONTENT);
  }
  
  public MailContent setContent(Object value)
  {
    return set(CONTENT, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public MailContent setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
}
