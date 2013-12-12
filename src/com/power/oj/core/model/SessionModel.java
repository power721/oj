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
  
  public String getId()
  {
    return getStr("session_id");
  }
  
  public int updateURL(String url, String sessionID)
  {
    return Db.update("UPDATE session SET last_activity=UNIX_TIMESTAMP(),uri=? WHERE session_id=?", url, sessionID);
  }
  
  public int updateUser(int uid, String name, String sessionID)
  {
    return Db.update("UPDATE session SET uid=?,name=? WHERE session_id=?", uid, name, sessionID);
  }
  
  public int deleteSession(String sessionID)
  {
    return Db.update("DELETE FROM session WHERE session_id=?", sessionID);
  }
  
  public int expiresSession()
  {
    return Db.update("DELETE FROM session WHERE session_expires <= UNIX_TIMESTAMP()");
  }
}
