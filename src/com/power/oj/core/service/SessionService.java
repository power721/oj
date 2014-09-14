package com.power.oj.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import jodd.util.StringUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.interceptor.AccessLogInterceptor;
import com.power.oj.core.model.SessionModel;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import com.power.oj.util.Tool;

public final class SessionService
{
  private static final Logger log = Logger.getLogger(SessionService.class);
  private static final SessionService me = new SessionService();
  private static final SessionModel dao = SessionModel.dao;
  
  private SessionService()
  {
    
  }
  
  public static SessionService me()
  {
    return me;
  }

  public static SessionService newInstance()
  {
    return new SessionService();
  }
  
  /**
   * @see AccessLogInterceptor
   */
  public String getLastAccessURL()
  {
    String lastAccessURL = (String) getSession().getAttribute(OjConstants.LAST_ACCESS_URL);
    if (StringUtil.isBlank(lastAccessURL))
    {
      lastAccessURL = "/";
    }
    
    if (OjConfig.isDevMode())
    {
      log.info(lastAccessURL);
    }
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
    Subject currentUser = ShiroKit.getSubject();
    Session session = currentUser.getSession();
    UserModel userModel = UserService.me().getCurrentUser();
    String id = (String) session.getId();
    int uid = userModel.getUid();
    String name = userModel.getName();
    
    SessionModel sessionModel = getSessionModel(id);
    sessionModel.setUid(uid).setName(name).update();
    updateCache(sessionModel);
  }

  public void updateOnline(Integer uid, String name)
  {
    String id = (String) ShiroKit.getSubject().getSession().getId();
   
    SessionModel sessionModel = getSessionModel(id);
    sessionModel.setUid(uid).setName(name);//.update();
    updateCache(sessionModel);
  }
  
  public SessionModel updateSession(HttpSession session, String url)
  {
    String id = (String) session.getId();
    SessionModel sessionModel = getSessionModel(id);
    if (sessionModel == null)
    {
      log.info("create new session!");
      sessionModel = new SessionModel().setSessionId(id);
    }
    if (sessionModel.getBoolean("first") != null && sessionModel.getUid() == null)
    {
      UserModel userModel = UserService.me().getCurrentUser();
      if (userModel != null)
      {
        sessionModel.setUid(userModel.getUid()).setName(userModel.getName());
        sessionModel.remove("first");
      }
    }

    sessionModel.setLastActivity(OjConfig.timeStamp).setUri(url);
    updateCache(sessionModel);
    return sessionModel;
  }
  
  public SessionModel saveSession(Session session)
  {
    int timeStamp = (int) (System.currentTimeMillis() / 1000);
    String id = (String) session.getId();
    
    SessionModel sessionModel = new SessionModel();
    sessionModel.setSessionId(id);
    sessionModel.setIpAddress(Tool.getIpAddr());
    sessionModel.setUserAgent(Tool.getUserAgent());
    sessionModel.setCtime(timeStamp).setLastActivity(timeStamp);
    sessionModel.setSessionExpires((int) (timeStamp + session.getTimeout()));
    sessionModel.save();

    sessionModel.put("first", true);
    updateCache(sessionModel);

    return sessionModel;
  }
  
  public boolean deleteSession(Session session)
  {
    String id = (String) session.getId();
    
    evictCache(id);
    
    return dao.deleteById(id);
  }

  public boolean deleteSession(String id)
  {
    evictCache(id);
    
    return dao.deleteById(id);
  }
  
  public int deleteAllSession()
  {
    return Db.update("DELETE FROM session");
  }

  public int expiresSession()
  {
    return dao.expiresSession();
  }

  public List<SessionModel> getAccessLog()
  {
    List<SessionModel> oldSessions = dao.find("SELECT * FROM session ORDER BY ctime DESC");
    List<SessionModel> sessions = new ArrayList<SessionModel>(oldSessions.size());
    SessionModel newSession;
    
    for (SessionModel session : oldSessions)
    {
      newSession = getSessionModel(session.getId());
      sessions.add(newSession);
    }
    
    return sessions;
  }
  
  public long size()
  {
    return Db.queryLong("SELECT COUNT(*) FROM session");
  }
  
  public long getUserNumber()
  {
    return Db.queryLong("SELECT COUNT(*) FROM session WHERE uid != 0");
  }

  private SessionModel getSessionModel(String id)
  {
    return dao.findFirstByCache("session", id, "SELECT * FROM session WHERE sessionId=?", id);
  }
  
  private void updateCache(SessionModel session)
  {
    CacheKit.put("session", session.getId(), session);
  }
  
  private void evictCache(String id)
  {
    CacheKit.remove("session", id);
  }
  
}
