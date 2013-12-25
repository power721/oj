package com.power.oj.core.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.interceptor.AccessLogInterceptor;
import com.power.oj.core.model.SessionModel;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class SessionService
{
  private static ConcurrentHashMap<String, SessionModel> accessLog = new ConcurrentHashMap<String, SessionModel>();
  private static ConcurrentHashMap<String, Session> shiroSession = new ConcurrentHashMap<String, Session>();
  private static final SessionModel dao = SessionModel.dao;
  
  /**
   * @see AccessLogInterceptor
   */
  public static String getLastAccessURL()
  {
    String lastAccessURL = (String) getSession().getAttribute(OjConstants.LAST_ACCESS_URL);
    if (lastAccessURL == null)
      lastAccessURL = "/";
    
    return lastAccessURL;
  }

  public static void setLastAccessURL(String lastAccessURL)
  {
    getSession().setAttribute(OjConstants.LAST_ACCESS_URL, lastAccessURL);
  }
  
  public static Session getSession()
  {
    return SecurityUtils.getSubject().getSession();
  }

  public static String getHost()
  {
    return getSession().getHost();
  }

  public static void updateLogin()
  {
    Subject currentUser = UserService.getSubject();
    Session session = currentUser.getSession();
    UserModel userModel = UserService.getPrincipal();
    String id = (String) session.getId();
    int uid = userModel.getUid();
    String name = userModel.getStr("name");
    
    SessionModel sessionModel = dao.findById(id);
    sessionModel.set("uid", uid).set("name", name).update();
    SessionService.putModel(id, sessionModel);
  }
  
  public static SessionModel updateSession(Session session, String url)
  {
    String id = (String) session.getId();
    SessionModel sessionModel = SessionService.getModel(id);
    if (sessionModel == null)
    {
      sessionModel = new SessionModel().set("session_id", id);
    }
          
    sessionModel.set("last_activity", OjConfig.timeStamp).set("uri", url);
    return SessionService.putModel(id, sessionModel);
  }
  
  public static SessionModel saveSession(Session session)
  {
    String id = (String) session.getId();
    
    SessionModel sessionModel = new SessionModel().set("session_id", id).set("ip_address", session.getHost());//.set("user_agent", agent);
    sessionModel.set("ctime", OjConfig.timeStamp).set("last_activity", OjConfig.timeStamp);
    sessionModel.set("session_expires", OjConfig.timeStamp + session.getTimeout());
    sessionModel.save();

    SessionService.putModel(id, sessionModel);
    SessionService.putShiroSession(id, session);
    
    return sessionModel;
  }
  
  public static int deleteSession(Session session)
  {
    String id = (String) session.getId();
    
    SessionService.removeModel(id);
    SessionService.removeShiroSession(id);
    
    return dao.deleteSession(id);
  }

  public static SessionModel putModel(SessionModel session)
  {
    return accessLog.put(session.getId(), session);
  }
  
  public static SessionModel putModel(String id, SessionModel session)
  {
    return accessLog.put(id, session);
  }
  
  public static SessionModel getModel(String id)
  {
    return accessLog.get(id);
  }
  
  public static SessionModel removeModel(String id)
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
  
  public static Session putShiroSession(Session session)
  {
    return shiroSession.put((String) session.getId(), session);
  }
  
  public static Session putShiroSession(String id, Session session)
  {
    return shiroSession.put(id, session);
  }
  
  public static Session getShiroSession(String id)
  {
    return shiroSession.get(id);
  }
  
  public static Session removeShiroSession(String id)
  {
    return shiroSession.remove(id);
  }
  
  public static int getUserNumber()
  {
    int number = 0;
    for (Enumeration<SessionModel> e = accessLog.elements(); e.hasMoreElements();)
    {
      SessionModel sessionModel = e.nextElement();
      if (sessionModel.getInt("uid") != null)
        number++;
    }
    
    return number;
  }
  
}
