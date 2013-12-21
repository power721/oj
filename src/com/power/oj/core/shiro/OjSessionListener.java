package com.power.oj.core.shiro;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.model.SessionModel;

public class OjSessionListener implements SessionListener
{

  private static final Logger log = Logger.getLogger(OjSessionListener.class);
  private static ConcurrentHashMap<String, SessionModel> accessLog = new ConcurrentHashMap<String, SessionModel>();
  private static ConcurrentHashMap<String, Session> shiroSession = new ConcurrentHashMap<String, Session>();
      
  @Override
  public void onExpiration(Session session)
  {
    String id = (String) session.getId();
    
    accessLog.remove(id);
    shiroSession.remove(id);
    
    SessionModel.dao.deleteSession(id);
  }

  @Override
  public void onStart(Session session)
  {
    // TODO Auto-generated method stub
    log.info(session.toString());
    String id = (String) session.getId();
    SessionModel sessionModel = new SessionModel().set("session_id", id);//.set("ip_address", ip).set("user_agent", agent);
    sessionModel.set("ctime", OjConfig.timeStamp).set("last_activity", OjConfig.timeStamp).set("session_expires", OjConfig.timeStamp + session.getTimeout());
    sessionModel.save();
    
    accessLog.put(id, sessionModel);
    shiroSession.put(id, session);
  }

  @Override
  public void onStop(Session session)
  {
    String id = (String) session.getId();
    
    accessLog.remove(id);
    shiroSession.remove(id);
    
    SessionModel.dao.deleteSession(id);

    log.info(session.toString());
    log.info(session.getStartTimestamp().toString());
  }

  
  public static SessionModel update(Session session, String url)
  {
    String id = (String) session.getId();
    SessionModel sessionModel = get(id);
    if (sessionModel == null)
    {
      sessionModel = new SessionModel().set("session_id", id);
    }
          
    sessionModel.set("last_activity", OjConfig.timeStamp).set("uri", url);
    return accessLog.put(id, sessionModel);
  }

  public static SessionModel put(SessionModel session)
  {
    return accessLog.put(session.getId(), session);
  }
  
  public static SessionModel put(String id, SessionModel session)
  {
    return accessLog.put(id, session);
  }
  
  public static SessionModel get(String id)
  {
    return accessLog.get(id);
  }
  
  public static SessionModel remove(String id)
  {
    return accessLog.remove(id);
  }

  public static List<SessionModel> getAccessLog()
  {
    List<SessionModel> sessions = new ArrayList<SessionModel>();
    for (Enumeration<SessionModel> e = accessLog.elements(); e.hasMoreElements();)
      sessions.add(e.nextElement());
    
    return sessions;
  }
  
  public static int size()
  {
    return accessLog.size();
  }
  
}
