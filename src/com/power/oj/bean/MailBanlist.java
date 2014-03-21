package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class MailBanlist extends Model<MailBanlist>
{
  private static final long serialVersionUID = 1L;
  
  public static final MailBanlist dao = new MailBanlist();
  
  public static final String TABLE_NAME = "mail_banlist";
  public static final String ID = "id";
  public static final String UID = "uid";
  public static final String BANNED_UID = "bannedUid";
  public static final String CTIME = "ctime";

  /*
   * auto generated getter and setter
   */
  public Integer getId()
  {
    return getInt(ID);
  }
  
  public MailBanlist setId(Integer value)
  {
    return set(ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public MailBanlist setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public Integer getBannedUid()
  {
    return getInt(BANNED_UID);
  }
  
  public MailBanlist setBannedUid(Integer value)
  {
    return set(BANNED_UID, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public MailBanlist setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
