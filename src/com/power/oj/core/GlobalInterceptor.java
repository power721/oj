package com.power.oj.core;

import javax.servlet.http.HttpSession;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.power.oj.user.UserModel;

public class GlobalInterceptor implements Interceptor
{
	public void intercept(ActionInvocation ai)
	{
		OjConstants.startGlobalInterceptorTime = System.currentTimeMillis();
		Controller controller = ai.getController();

		//controller.setAttr("baseUrl", OjConstants.baseUrl); // move to UrlFiterHandler
		//controller.setAttr("siteTitle", OjConstants.siteTitle);

		String actionKey = ai.getActionKey();
		controller.setAttr("actionKey", actionKey.replace("/", ""));
		String controllerKey = ai.getControllerKey();
		controller.setAttr("controllerKey", controllerKey.replace("/", ""));
		String methodName = ai.getMethodName();
		controller.setAttr("methodName", methodName);

		HttpSession session = controller.getSession(true);
		UserModel user = controller.getSessionAttr("user");
		if (user == null)
		{
			String name = controller.getCookie("name");
			String token = controller.getCookie("token");
			if (StringUtil.isNotBlank(name) && StringUtil.isNotBlank(token))
			{
				user = UserModel.dao.autoLogin(name, token);
				if (user != null)
				{
					int uid = user.getInt("uid");
					if (user.isAdmin(uid))
						controller.setSessionAttr("adminUser", uid);
					controller.setSessionAttr("user", user);
				}
			}
		}

		if (user != null)
		{
			int uid = user.getInt("uid");
			controller.setAttr("userID", uid);
			controller.setAttr("userName", user.getStr("name"));
			controller.setAttr("userEmail", user.getStr("email"));
			if (controller.getSessionAttr("adminUser") != null)
				controller.setAttr("adminUser", uid);
		}

		if(controller.getSessionAttr("msg") != null)
		{
			controller.setAttr("msg", controller.getSessionAttr("msg"));
			controller.setAttr("msgType", controller.getSessionAttr("msgType"));
			controller.setAttr("msgTitle", controller.getSessionAttr("msgTitle"));
			controller.removeSessionAttr("msg");
			controller.removeSessionAttr("msgType");
			controller.removeSessionAttr("msgTitle");
		}
		
		String skipActions[] = {"/logout", "/captcha"};
		if (StringUtil.equalsOne(actionKey, skipActions) == -1)
		{
			StringBand rsb = new StringBand(actionKey);
			if (controller.getPara() != null)
				rsb.append("/").append(controller.getPara());
			StringBand sb = new StringBand(controller.getRequest().getRequestURI());
			String query = controller.getRequest().getQueryString();
			if (query != null)
			{
				sb.append("?").append(query);
				rsb.append("?").append(query);
			}

			if (!actionKey.equals("/login"))
				controller.setAttr("redirectURI", uriEncode(rsb.toString()));
			controller.setAttr("uri", uriEncode(sb.toString()));
			Db.update("UPDATE session SET last_activity=UNIX_TIMESTAMP(),uri=? WHERE session_id=?", sb.toString(),
					session.getId());
		}

		ai.invoke();
		
		System.out.println(new StringBand(4).append(actionKey).append(" Action Invoking Time: ").
				append(System.currentTimeMillis()-OjConstants.startGlobalInterceptorTime).append(" milliseconds").toString());
	}

	public static String uriEncode(String uri)
	{
		if (uri == null)
			return "&nbsp;";
		StringBand sb = new StringBand();
		for (int i = 0; i < uri.length(); i++)
		{
			char c = uri.charAt(i);
			if (c == '+')
				sb.append("%2B");
			else if (c == '%')
				sb.append("%25");
			else if (c == '=')
				sb.append("%3D");
			else if (c == '?')
				sb.append("%3F");
			else if (c == '&')
				sb.append("%26");
			else if (c == '#')
				sb.append("%23");
			else if (c == ' ')
				sb.append("+");
			else
				sb.append(c);
		}
		return sb.toString();
	}
}
