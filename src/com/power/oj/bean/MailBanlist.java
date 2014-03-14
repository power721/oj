package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class MailBanlist extends Model<MailBanlist>
{
  private static final long serialVersionUID = 1L;
  
  public static final MailBanlist dao = new MailBanlist();
  
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String BANNED_UID = "bannedUid";
  public static final String CTIME = "ctime";

  public <T> T getId()
  {
    return get(ID);
  }
  
  public MailBanlist setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public MailBanlist setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getBannedUid()
  {
    return get(BANNED_UID);
  }
  
  public MailBanlist setBannedUid(Object value)
  {
    return set(BANNED_UID, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public MailBanlist setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
}
