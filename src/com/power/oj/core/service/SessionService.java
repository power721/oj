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
  private static final SessionService me = new SessionService();
  private static final SessionModel dao = SessionModel.dao;
  
  private SessionService()
  {
    
  }
  
  public static SessionService me()
  {
    return me;
  }
  
  /**
   * @see AccessLogInterceptor
   */
  public String getLastAccessURL()
  {
    String lastAccessURL = (String) getSession().getAttribute(OjConstants.LAST_ACCESS_URL);
    if (lastAccessURL == null)
      lastAccessURL = "/";
    
    return lastAccessURL;
  }

  public void setLastAccessURL(String lastAccessURL)
  {
    getSession().setAttribute(OjConstants.LAST_ACCESS_URL, lastAccessURL);
  }
  
  public Session getSession()
  {
    return SecurityUtils.getSubject().getSession();
  }

  public String getHost()
  {
    return getSession().getHost();
  }

  public void updateLogin()
  {
    Subject currentUser = UserService.me().getSubject();
    Session session = currentUser.getSession();
    UserModel userModel = UserService.me().getPrincipal();
    String id = (String) session.getId();
    int uid = userModel.getUid();
    String name = userModel.getStr("name");
    
    SessionModel sessionModel = dao.findById(id);
    sessionModel.set("uid", uid).set("name", name).update();
    SessionService.me().putModel(id, sessionModel);
  }
  
  public SessionModel updateSession(Session session, String url)
  {
    String id = (String) session.getId();
    SessionModel sessionModel = SessionService.me().getModel(id);
    if (sessionModel == null)
    {
      sessionModel = new SessionModel().set("session_id", id);
    }
          
    sessionModel.set("last_activity", OjConfig.timeStamp).set("uri", url);
    return SessionService.me().putModel(id, sessionModel);
  }
  
  public SessionModel saveSession(Session session)
  {
    String id = (String) session.getId();
    
    SessionModel sessionModel = new SessionModel().set("session_id", id).set("ip_address", session.getHost());//.set("user_agent", agent);
    sessionModel.set("ctime", OjConfig.timeStamp).set("last_activity", OjConfig.timeStamp);
    sessionModel.set("session_expires", OjConfig.timeStamp + session.getTimeout());
    sessionModel.save();

    SessionService.me().putModel(id, sessionModel);
    SessionService.me().putShiroSession(id, session);
    
    return sessionModel;
  }
  
  public int deleteSession(Session session)
  {
    String id = (String) session.getId();
    
    SessionService.me().removeModel(id);
    SessionService.me().removeShiroSession(id);
    
    return dao.deleteSession(id);
  }

  public SessionModel putModel(SessionModel session)
  {
    return accessLog.put(session.getId(), session);
  }
  
  public SessionModel putModel(String id, SessionModel session)
  {
    return accessLog.put(id, session);
  }
  
  public SessionModel getModel(String id)
  {
    return accessLog.get(id);
  }
  
  public SessionModel removeModel(String id)
  {
    return accessLog.remove(id);
  }

  public List<SessionModel> getAccessLog()
  {
    List<SessionModel> sessions = new ArrayList<SessionModel>();
    for (Enumeration<SessionModel> e = accessLog.elements(); e.hasMoreElements();)
      sessions.add(e.nextElement());
    
    return sessions;
  }
  
  public int size()
  {
    return accessLog.size();
  }
  
  public Session putShiroSession(Session session)
  {
    return shiroSession.put((String) session.getId(), session);
  }
  
  public Session putShiroSession(String id, Session session)
  {
    return shiroSession.put(id, session);
  }
  
  public Session getShiroSession(String id)
  {
    return shiroSession.get(id);
  }
  
  public Session removeShiroSession(String id)
  {
    return shiroSession.remove(id);
  }
  
  public int getUserNumber()
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
