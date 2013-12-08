package com.power.oj.core.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class SessionModel extends Model<SessionModel>
{
  public static final SessionModel dao = new SessionModel();
  
  public void updateURL(String url, String sessionID)
  {
    Db.update("UPDATE session SET last_activity=UNIX_TIMESTAMP(),uri=? WHERE session_id=?", url, sessionID);
  }
  
  public void updateUser(int uid, String name, String sessionID)
  {
    Db.update("UPDATE session SET uid=?,name=? WHERE session_id=?", uid, name, sessionID);
  }
  
  public void deleteSession(String sessionID)
  {
    Db.update("DELETE FROM session WHERE session_id=? OR session_expires <= UNIX_TIMESTAMP()", sessionID);
  }
}
