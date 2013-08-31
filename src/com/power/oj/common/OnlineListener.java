package com.power.oj.common;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.user.UserModel;

public class OnlineListener implements ServletContextListener, ServletContextAttributeListener, HttpSessionListener,
		HttpSessionAttributeListener, ServletRequestListener
{

	HttpServletRequest request;
	private static HashMap<String, HttpSession> map = new HashMap<String, HttpSession>();

	public OnlineListener()
	{
		request = null;
		// Db.update("DELETE FROM session WHERE session_expires <= UNIX_TIMESTAMP()");
	}

	public void contextInitialized(ServletContextEvent servletcontextevent)
	{
		servletcontextevent.getServletContext();
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent)
	{
	}

	public void attributeAdded(ServletContextAttributeEvent servletcontextattributeevent)
	{
	}

	public void attributeRemoved(ServletContextAttributeEvent servletcontextattributeevent)
	{
	}

	public void attributeReplaced(ServletContextAttributeEvent servletcontextattributeevent)
	{
	}

	public void sessionCreated(HttpSessionEvent httpsessionevent)
	{
		String ip = getRemortIP(request);
		String agent = request.getHeader("User-Agent");
		String id = httpsessionevent.getSession().getId();
		long session_expires = httpsessionevent.getSession().getCreationTime() / 1000
				+ httpsessionevent.getSession().getMaxInactiveInterval();

		map.put(id, httpsessionevent.getSession());
		System.out.println("sessionCreated: " + map.size());

		Record session = new Record().set("session_id", id).set("ip_address", ip).set("user_agent", agent);
		session.set("last_activity", System.currentTimeMillis() / 1000).set("session_expires", session_expires);
		Db.save("session", session);
	}

	public void sessionDestroyed(HttpSessionEvent httpsessionevent)
	{
		String id = httpsessionevent.getSession().getId();
		map.remove(id);
		Db.update("DELETE FROM session WHERE session_id=? OR session_expires <= UNIX_TIMESTAMP()", id);
	}

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
			uid = userModel.getInt("uid");
			name = userModel.getStr("name");

			List<Record> sessions = Db
					.find(
							"SELECT session_id, ip_address, user_agent, last_activity, session_expires FROM session WHERE uid=?",
							uid);
			for (Record sessionRecord : sessions)
			{
				String session_id = sessionRecord.getStr("session_id");
				HttpSession prevSession = map.get(session_id);
				if (prevSession != null)
				{
					prevSession.invalidate();
					System.out.println(session_id + ": invalidate");
				} else
				{
					Db.deleteById("session", "session_id", session_id);
					System.out.println(session_id + ": deleted");
				}
			}

			Db.update("UPDATE session SET uid=?,name=? WHERE session_id=?", uid, name, id);
			/*
			 * String title = s + " login repeatedly"; String content =
			 * "Old  IP: " + ip_address + "\nNew IP: " + ip + "\n";
			 * Tool.sendMail(connection, "System", s, title,
			 * "此消息也会发给管理员，请不要在比赛过程中重复登录。\n如果非本人登录，请修改密码以保证安全。\n"+content, 0);
			 * content +=
			 * "Old  Agent: "+resultset.getString("user_agent")+"\nNew Agent: "
			 * +agent; Tool.sendMail(connection, "System", "root;power721",
			 * title, content, 0);
			 */
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent httpsessionbindingevent)
	{
	}

	public void attributeReplaced(HttpSessionBindingEvent httpsessionbindingevent)
	{
	}

	public void requestDestroyed(ServletRequestEvent event)
	{
	}

	public void requestInitialized(ServletRequestEvent event)
	{
		request = (HttpServletRequest) event.getServletRequest();
	}

	public String getRemortIP(HttpServletRequest request)
	{
		if (request.getHeader("x-forwarded-for") == null)
		{
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
}
