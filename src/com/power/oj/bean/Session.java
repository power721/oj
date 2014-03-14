package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Session extends Model<Session>
{
  private static final long serialVersionUID = 1L;
  
  public static final Session dao = new Session();
  
  public static final String SESSION_ID = "sessionId";
  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String IP_ADDRESS = "ipAddress";
  public static final String USER_AGENT = "userAgent";
  public static final String URI = "uri";
  public static final String LAST_ACTIVITY = "lastActivity";
  public static final String SESSION_EXPIRES = "sessionExpires";
  public static final String CTIME = "ctime";

  public <T> T getSessionId()
  {
    return get(SESSION_ID);
  }
  
  public Session setSessionId(Object value)
  {
    return set(SESSION_ID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Session setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getName()
  {
    return get(NAME);
  }
  
  public Session setName(Object value)
  {
    return set(NAME, value);
  }
  
  public <T> T getIpAddress()
  {
    return get(IP_ADDRESS);
  }
  
  public Session setIpAddress(Object value)
  {
    return set(IP_ADDRESS, value);
  }
  
  public <T> T getUserAgent()
  {
    return get(USER_AGENT);
  }
  
  public Session setUserAgent(Object value)
  {
    return set(USER_AGENT, value);
  }
  
  public <T> T getUri()
  {
    return get(URI);
  }
  
  public Session setUri(Object value)
  {
    return set(URI, value);
  }
  
  public <T> T getLastActivity()
  {
    return get(LAST_ACTIVITY);
  }
  
  public Session setLastActivity(Object value)
  {
    return set(LAST_ACTIVITY, value);
  }
  
  public <T> T getSessionExpires()
  {
    return get(SESSION_EXPIRES);
  }
  
  public Session setSessionExpires(Object value)
  {
    return set(SESSION_EXPIRES, value);
  }
  
  public <T> T getCtime()
  {
    return get(CTIME);
  }
  
  public Session setCtime(Object value)
  {
    return set(CTIME, value);
  }
  
}
