package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Session extends Model<Session>
{
  private static final long serialVersionUID = 1L;
  
  public static final Session dao = new Session();
  
  public static final String TABLE_NAME = "session";
  public static final String SESSION_ID = "sessionId";
  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String IP_ADDRESS = "ipAddress";
  public static final String USER_AGENT = "userAgent";
  public static final String CTIME = "ctime";
  public static final String LAST_ACTIVITY = "lastActivity";
  public static final String SESSION_EXPIRES = "sessionExpires";
  public static final String URI = "uri";

  /*
   * auto generated getter and setter
   */
  public String getSessionId()
  {
    return getStr(SESSION_ID);
  }
  
  public Session setSessionId(String value)
  {
    return set(SESSION_ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public Session setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public Session setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getIpAddress()
  {
    return getStr(IP_ADDRESS);
  }
  
  public Session setIpAddress(String value)
  {
    return set(IP_ADDRESS, value);
  }
  
  public String getUserAgent()
  {
    return getStr(USER_AGENT);
  }
  
  public Session setUserAgent(String value)
  {
    return set(USER_AGENT, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public Session setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
  public Integer getLastActivity()
  {
    return getInt(LAST_ACTIVITY);
  }
  
  public Session setLastActivity(Integer value)
  {
    return set(LAST_ACTIVITY, value);
  }
  
  public Integer getSessionExpires()
  {
    return getInt(SESSION_EXPIRES);
  }
  
  public Session setSessionExpires(Integer value)
  {
    return set(SESSION_EXPIRES, value);
  }
  
  public String getUri()
  {
    return getStr(URI);
  }
  
  public Session setUri(String value)
  {
    return set(URI, value);
  }
  
}
