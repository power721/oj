package com.power.oj.core.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class SessionModel extends Model<SessionModel>
{
  /**
   * 
   */
  private static final long serialVersionUID = -7121738309176622245L;
  
  public static final SessionModel dao = new SessionModel();

  public static final String TABLE_NAME = "session";
  public static final String SESSION_ID = "sessionId";
  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String IP_ADDRESS = "ipAddress";
  public static final String USER_AGENT = "userAgent";
  public static final String URI = "uri";
  public static final String LAST_ACTIVITY = "lastActivity";
  public static final String SESSION_EXPIRES = "sessionExpires";
  public static final String CTIME = "ctime";

  public String getId()
  {
    return getStr(SESSION_ID);
  }
  
  public int updateURL(String url, String sessionID)
  {
    return Db.update("UPDATE session SET lastActivity=UNIX_TIMESTAMP(),uri=? WHERE sessionId=?", url, sessionID);
  }
  
  public int updateUser(int uid, String name, String sessionID)
  {
    return Db.update("UPDATE session SET uid=?,name=? WHERE sessionId=?", uid, name, sessionID);
  }
  
  public int expiresSession()
  {
    return Db.update("DELETE FROM session WHERE sessionExpires <= UNIX_TIMESTAMP() OR lastActivity + 1800 < UNIX_TIMESTAMP()");
  }

  public String getSessionId()
  {
    return getStr(SESSION_ID);
  }
  
  public SessionModel setSessionId(String value)
  {
    return set(SESSION_ID, value);
  }
  
  public Integer getUid()
  {
    return getInt(UID);
  }
  
  public SessionModel setUid(Integer value)
  {
    return set(UID, value);
  }
  
  public String getName()
  {
    return getStr(NAME);
  }
  
  public SessionModel setName(String value)
  {
    return set(NAME, value);
  }
  
  public String getIpAddress()
  {
    return getStr(IP_ADDRESS);
  }
  
  public SessionModel setIpAddress(String value)
  {
    return set(IP_ADDRESS, value);
  }
  
  public String getUserAgent()
  {
    return getStr(USER_AGENT);
  }
  
  public SessionModel setUserAgent(String value)
  {
    return set(USER_AGENT, value);
  }
  
  public String getUri()
  {
    return getStr(URI);
  }
  
  public SessionModel setUri(String value)
  {
    return set(URI, value);
  }
  
  public Integer getLastActivity()
  {
    return getInt(LAST_ACTIVITY);
  }
  
  public SessionModel setLastActivity(Integer value)
  {
    return set(LAST_ACTIVITY, value);
  }
  
  public Integer getSessionExpires()
  {
    return getInt(SESSION_EXPIRES);
  }
  
  public SessionModel setSessionExpires(Integer value)
  {
    return set(SESSION_EXPIRES, value);
  }
  
  public Integer getCtime()
  {
    return getInt(CTIME);
  }
  
  public SessionModel setCtime(Integer value)
  {
    return set(CTIME, value);
  }
  
}
