package com.power.oj.user;

import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * Get and Set user session and controller attributes, auto login.
 * @author power
 *
 */
public class UserInterceptor implements Interceptor
{

	@Override
	public void intercept(ActionInvocation ai)
	{
		Controller controller = ai.getController();
		UserModel user = controller.getSessionAttr("user");
		
		if (user == null) // user does not login
		{
			String name = controller.getCookie("name");
			String token = controller.getCookie("token");
			if (StringUtil.isNotBlank(name) && StringUtil.isNotBlank(token)) // try to auto login with cookie
			{
				user = UserModel.dao.autoLogin(name, token);
				if (user != null)
				{
					controller.setSessionAttr("user", user);
					
					int uid = user.getInt("uid");
					if (user.isAdmin(uid)) // current user has admin role
						controller.setSessionAttr("adminUser", uid);
				}
			}
		}

		if (user != null) // if user is logined, set user information in controller
		{
			int uid = user.getInt("uid");
			controller.setAttr("userID", uid);
			controller.setAttr("userName", user.getStr("name"));
			controller.setAttr("userEmail", user.getStr("email"));
			if (controller.getSessionAttr("adminUser") != null)
				controller.setAttr("adminUser", uid);
		}
		
		ai.invoke();
	}

}
