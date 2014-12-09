package com.power.oj.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.SessionView;
import com.power.oj.core.interceptor.AccessLogInterceptor;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import com.power.oj.util.Tool;

public final class SessionService {
  private static final Logger log = Logger.getLogger(SessionService.class);
  private static final SessionService me = new SessionService();
  private Map<String, Session> sessions = new HashMap<>();
  private Map<String, String> hosts = new HashMap<>();
  private Map<String, String> userAgents = new HashMap<>();

  private SessionService() {

  }

  public static SessionService me() {
    return me;
  }

  /**
   * @see AccessLogInterceptor
   */
  public String getLastAccessURL() {
    String lastAccessURL = (String) getCurrentSession().getAttribute(OjConstants.LAST_ACCESS_URL);
    if (StringUtil.isBlank(lastAccessURL)) {
      lastAccessURL = "/";
    }

    if (OjConfig.isDevMode()) {
      log.info(lastAccessURL);
    }
    return lastAccessURL;
  }

  public void setLastAccessURL(final String lastAccessURL) {
    getCurrentSession().setAttribute(OjConstants.LAST_ACCESS_URL, lastAccessURL);
  }

  public void setHost(HttpServletRequest request) {
	  String key = (String) getCurrentSession().getId();
	  String ip = Tool.getIpAddr(request);
	  hosts.put(key, ip);
  }

  public String getHost() {
	  String key = (String) getCurrentSession().getId();
	  return getHost(key);
  }

  public String getHost(String key) {
	  return hosts.get(key);
  }

  public void setUserAgent(HttpServletRequest request) {
	  String key = (String) getCurrentSession().getId();
	  String ip = Tool.getUserAgent(request);
	  userAgents.put(key, ip);
  }

  public String getUserAgent() {
	  String key = (String) getCurrentSession().getId();
	  return getUserAgent(key);
  }

  public String getUserAgent(String key) {
	  return userAgents.get(key);
  }

  public void updateLogin() {
    final Session session = getCurrentSession();
    final UserModel userModel = UserService.me().getCurrentUser();
    final String id = (String) session.getId();
    final String name = userModel.getName();

    session.setAttribute(OjConstants.USER_NAME, name);
    sessions.put(id, session);
  }

  public void updateOnline(final Integer uid, final String name) {
    final Session session = getCurrentSession();

    session.setAttribute(OjConstants.USER_NAME, name);
  }

  public Session saveSession(final Session session) {
    final String id = (String) session.getId();

    sessions.put(id, session);

    return session;
  }
  
  public boolean checkAndSetSession()
  {
	  final Session session = getCurrentSession();
	  final String id = (String) session.getId();
	  
	  if (!sessions.containsKey(id))
	  {
		  sessions.put(id, session);
		  return true;
	  }
	  else
	  {
		  return false;
	  }
  }

  public Session deleteSession(final Session session) {
    return sessions.remove(session.getId());
  }

  public List<SessionView> getAccessLog() {
    List<SessionView> list = new ArrayList<SessionView>();

    for (Session session : sessions.values()) {
      SessionView sessionView = new SessionView();
      String id = (String) session.getId();
      sessionView.setId(id);
      sessionView.setName((String) session.getAttribute(OjConstants.USER_NAME));
      sessionView.setUri((String) session.getAttribute(OjConstants.LAST_ACCESS_URL));
      sessionView.setIpAddress(getHost(id));
      sessionView.setUserAgent(getUserAgent(id));
      sessionView.setCtime(session.getStartTimestamp());
      sessionView.setLastActivity(session.getLastAccessTime());

      list.add(sessionView);
    }

    return list;
  }

  public long getUserNumber() {
    long count = 0L;

    for (Session session : sessions.values()) {
      if (session.getAttribute(OjConstants.USER_NAME) != null) {
        ++count;
      }
    }

    return count;
  }

  public Session getCurrentSession() {
    return SecurityUtils.getSubject().getSession();
  }

  /*
   * private Session getSession(String id) { return sessions.get(id); }
   */
}
