package com.power.oj.core.interceptor;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.model.SessionModel;

/**
 * Update session table with url and timestamp.
 * 
 * @author power
 * 
 */
public class AccessLogInterceptor implements Interceptor
{
  protected static final Logger log = Logger.getLogger(AccessLogInterceptor.class);
  
  private static ConcurrentHashMap<String, SessionModel> accessLog = new ConcurrentHashMap<String, SessionModel>();
  private String skipActions[] =
  { "/login", "/user/signin", "/logout", "/captcha", "/contest/password", "/problem/userResult" };

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    HttpSession session = controller.getSession(true);
    String actionKey = ai.getActionKey();

    if (StringUtil.equalsOne(actionKey, skipActions) == -1)
    {
      StringBand sb = new StringBand(actionKey);
      if (controller.getPara() != null)
        sb.append("/").append(controller.getPara());

      String query = controller.getRequest().getQueryString();
      if (query != null)
      {
        sb.append("?").append(query);
      }

      String url = sb.toString();
      if (url.indexOf("ajax=1") == -1)
        OjConfig.lastAccessURL = url;

      update(session, url);
      // TODO move all sql statements to model
      //SessionModel.dao.updateURL(url, session.getId());
    }

    ai.invoke();
  }
  
  public static SessionModel update(HttpSession session, String url)
  {
    String id = session.getId();
    SessionModel sessionModel = get(id);
    if (sessionModel == null)
    {
      sessionModel = new SessionModel().set("session_id", id);
    }
          
    sessionModel.set("uri", url);
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

  public static Enumeration<SessionModel> getAccessLog()
  {
    return accessLog.elements();
  }
  
  public static int size()
  {
    return accessLog.size();
  }
}
