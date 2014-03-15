package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class WebLogin extends Model<WebLogin>
{
  private static final long serialVersionUID = 1L;
  
  public static final WebLogin dao = new WebLogin();
  
  public static final String ID = "id";
  public static final String OPEN_ID = "openId";
  public static final String UID = "uid";
  public static final String NICK = "nick";
  public static final String AVATAR = "avatar";
  public static final String TYPE = "type";
  public static final String STATUS = "status";
  public static final String CTIME = "ctime";

  public Integer getId()
  {
    return getInt(ID);
  }
  
  public WebLogin setId(Integer value)
  {
    return set(ID, value);
  }
  
  public String getOpenId()
  {
    return getStr(OPEN_ID);
  }
  
  public WebLogin setOpenId(String value)
  {
    return set(OPEN_ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public WebLogin setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getNick()
  {
    return getStr(NICK);
  }
  
  public WebLogin setNick(String value)
  {
    return set(NICK, value);
  }
  
  public String getAvatar()
  {
    return getStr(AVATAR);
  }
  
  public WebLogin setAvatar(String value)
  {
    return set(AVATAR, value);
  }
  
  public String getType()
  {
    return getStr(TYPE);
  }
  
  public WebLogin setType(String value)
  {
    return set(TYPE, value);
  }
  
  public Boolean getStatus()
  {
    return getBoolean(STATUS);
  }
  
  public WebLogin setStatus(Boolean value)
  {
    return set(STATUS, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public WebLogin setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
