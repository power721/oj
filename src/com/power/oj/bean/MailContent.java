package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class MailContent extends Model<MailContent>
{
  private static final long serialVersionUID = 1L;
  
  public static final MailContent dao = new MailContent();
  
  public static final String TABLE_NAME = "mail_content";
  public static final String ID = "id";
  public static final String FROM_UID = "fromUid";
  public static final String TO_UID = "toUid";
  public static final String CONTENT = "content";
  public static final String CTIME = "ctime";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public MailContent setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getFromUid()
  {
    return getInt(FROM_UID);
  }
  
  public MailContent setFromUid(Integer value)
  {
    return set(FROM_UID, value);
  }
  
  public Integer getToUid()
  {
    return getInt(TO_UID);
  }
  
  public MailContent setToUid(Integer value)
  {
    return set(TO_UID, value);
  }
  
  public String getContent()
  {
    return getStr(CONTENT);
  }
  
  public MailContent setContent(String value)
  {
    return set(CONTENT, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public MailContent setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
