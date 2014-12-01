package com.power.oj.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public final class SessionService {
  private static final Logger log = Logger.getLogger(SessionService.class);
  private static final SessionService me = new SessionService();
  private Map<String, Session> sessions = new HashMap<>();

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

  public Session deleteSession(final Session session) {
    return sessions.remove(session.getId());
  }

  public List<SessionView> getAccessLog() {
    List<SessionView> list = new ArrayList<SessionView>();

    for (Session session : sessions.values()) {
      SessionView sessionView = new SessionView();
      sessionView.setId((String) session.getId());
      sessionView.setName((String) session.getAttribute(OjConstants.USER_NAME));
      sessionView.setUri((String) session.getAttribute(OjConstants.LAST_ACCESS_URL));
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
