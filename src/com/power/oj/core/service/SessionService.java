package com.power.oj.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.shiro.session.Session;

import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.SessionView;
import com.power.oj.core.interceptor.AccessLogInterceptor;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import com.power.oj.util.Tool;

public final class SessionService {
	private static final Logger log = Logger.getLogger(SessionService.class);
	private static final SessionService me = new SessionService();
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
		String lastAccessURL = (String) ShiroKit.getCurrentSession().getAttribute(OjConstants.LAST_ACCESS_URL);
		if (StringUtil.isBlank(lastAccessURL)) {
			lastAccessURL = "/";
		}

		if (OjConfig.isDevMode()) {
			log.info(lastAccessURL);
		}
		return lastAccessURL;
	}

	public void setLastAccessURL(final String lastAccessURL) {
		ShiroKit.getCurrentSession().setAttribute(OjConstants.LAST_ACCESS_URL, lastAccessURL);
	}

	public void setHost(HttpServletRequest request) {
		String key = ShiroKit.getCurrentSessionId();
		String ip = Tool.getIpAddr(request);
		hosts.put(key, ip);
	}

	public String getHost() {
		String key = ShiroKit.getCurrentSessionId();
		return getHost(key);
	}

	public String getHost(String key) {
		return hosts.get(key);
	}

	public void setUserAgent(HttpServletRequest request) {
		String key = ShiroKit.getCurrentSessionId();
		String ip = Tool.getUserAgent(request);
		userAgents.put(key, ip);
	}

	public String getUserAgent() {
		String key = ShiroKit.getCurrentSessionId();
		return getUserAgent(key);
	}

	public String getUserAgent(String key) {
		return userAgents.get(key);
	}

	public void deleteSession(Session session) {
		String id = (String) session.getId();

		hosts.remove(id);
		userAgents.remove(id);
	}

	public void updateLogin() {
		final Session session = ShiroKit.getCurrentSession();
		final UserModel userModel = UserService.me().getCurrentUser();
		final String name = userModel.getName();

		session.setAttribute(OjConstants.USER_NAME, name);
	}

	public void updateOnline(final Integer uid, final String name) {
		final Session session = ShiroKit.getCurrentSession();

		session.setAttribute(OjConstants.USER_NAME, name);
	}

	public List<SessionView> getAccessLog() {
		List<SessionView> list = new ArrayList<SessionView>();

		for (Session session : ShiroKit.getActiveSessions()) {
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

		for (Session session : ShiroKit.getActiveSessions()) {
			if (session.getAttribute(OjConstants.USER_NAME) != null) {
				++count;
			}
		}

		return count;
	}

}
