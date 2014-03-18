package com.power.oj.core;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.shiro.session.Session;

import com.jfinal.log.Logger;
import com.power.oj.core.model.SessionModel;
import com.power.oj.user.UserModel;
import com.power.oj.util.Tool;

/**
 * Listen the session event and record user info.
 * 
 * @author power
 * 
 */
public class OnlineListener implements HttpSessionListener, HttpSessionAttributeListener, ServletRequestListener
{
  private static final Logger log = Logger.getLogger(OnlineListener.class);
  
  private HttpServletRequest request = null;
  private static ConcurrentHashMap<String, SessionModel> accessLog = new ConcurrentHashMap<String, SessionModel>();
  private static ConcurrentHashMap<String, HttpSession> httpSession = new ConcurrentHashMap<String, HttpSession>();

  public OnlineListener()
  {
  }

  /**
   * When session is created(user or robot access), save the session ID, client
   * IP, browser and timestamp in DB.
   */
  public void sessionCreated(HttpSessionEvent httpsessionevent)
  {
    String ip = Tool.getRemoteAddr(request);
    String agent = request.getHeader("User-Agent");
    String id = httpsessionevent.getSession().getId();
    long session_expires = httpsessionevent.getSession().getCreationTime() / 1000 + httpsessionevent.getSession().getMaxInactiveInterval();

    httpSession.put(id, httpsessionevent.getSession());

    SessionModel sessionModel = new SessionModel().set("session_id", id).set("ip_address", ip).set("user_agent", agent);
    sessionModel.set("ctime", OjConfig.timeStamp).set("last_activity", OjConfig.timeStamp).set("session_expires", session_expires);
    sessionModel.save();
    
    accessLog.put(id, sessionModel);

    log.info("sessionCreated: " + id + ", ip: " + ip + ", total sessions: " + httpSession.size());
  }

  /**
   * When session is destroyed, delete the session info from DB.
   */
  public void sessionDestroyed(HttpSessionEvent httpsessionevent)
  {
    String id = httpsessionevent.getSession().getId();
    
    httpSession.remove(id);
    accessLog.remove(id);
    SessionModel.dao.deleteSession(id);
    
    log.info("sessionDestroyed: " + id + ", total sessions: " + httpSession.size());
  }

  /**
   * When session attribute with UserModel is added(user login), update session
   * info and user info in DB.
   */
  public void attributeAdded(HttpSessionBindingEvent httpsessionbindingevent)
  {
    int uid = 0;
    String name = "";
    HttpSession session = httpsessionbindingevent.getSession();
    Object Added = httpsessionbindingevent.getValue();

    if (Added.getClass().getName().equals("com.power.oj.user.UserModel"))
    {
      String id = session.getId();
      UserModel userModel = (UserModel) Added;
      uid = userModel.getUid();
      name = userModel.getName();

      List<SessionModel> sessions = SessionModel.dao.find("SELECT session_id, ip_address, user_agent, last_activity, session_expires FROM session WHERE uid=?", uid);
      for (SessionModel sessionRecord : sessions)
      {
        String session_id = sessionRecord.getStr("session_id");
        HttpSession prevSession = httpSession.get(session_id);
        if (prevSession != null)
        {
          prevSession.invalidate();
          log.warn("Session " + session_id + " invalidate.");
        } else
        {
          SessionModel.dao.deleteById(session_id);
          log.warn("Session " + session_id + " deleted.");
        }
      }

      SessionModel sessionModel = SessionModel.dao.findById(id);
      sessionModel.set("uid", uid).set("name", name).update();
      accessLog.put(id, sessionModel);

      log.info("attributeAdded: uid=" + uid + ", name=" + name + ", session=" + id);
      /*
       * String title = s + " login repeatedly"; String content = "Old  IP: " +
       * ip_address + "\nNew IP: " + ip + "\n"; Tool.sendMail(connection,
       * "System", s, title,
       * "此消息也会发给管理员，请不要在比赛过程中重复登录。\n如果非本人登录，请修改密码以保证安全。\n"+content, 0); content
       * += "Old  Agent: "+resultset.getString("user_agent")+"\nNew Agent: "
       * +agent; Tool.sendMail(connection, "System", "root;power721", title,
       * content, 0);
       */
    }
  }

  public void attributeRemoved(HttpSessionBindingEvent httpsessionbindingevent)
  {
  }

  public void attributeReplaced(HttpSessionBindingEvent httpsessionbindingevent)
  {
  }

  /**
   * When Servlet Request is destroyed.
   */
  public void requestDestroyed(ServletRequestEvent event)
  {
    request = null;
  }

  /**
   * When Servlet Request is initialized.
   */
  public void requestInitialized(ServletRequestEvent event)
  {
    request = (HttpServletRequest) event.getServletRequest();
    log.debug("requestInitialized.");
  }

  public static SessionModel update(HttpSession session, String url)
  {
    String id = session.getId();
    SessionModel sessionModel = get(id);
    if (sessionModel == null)
    {
      sessionModel = new SessionModel().set("session_id", id);
    }
          
    sessionModel.set("last_activity", OjConfig.timeStamp).set("uri", url);
    return accessLog.put(id, sessionModel);
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
