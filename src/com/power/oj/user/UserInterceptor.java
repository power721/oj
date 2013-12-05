package com.power.oj.user;

import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConstants;

/**
 * Get and Set user session and controller attributes, auto login.
 * @author power
 *
 */
public class UserInterceptor implements Interceptor
{
	protected final Logger log = Logger.getLogger(getClass());
	
	@Override
	public void intercept(ActionInvocation ai)
	{
		Controller controller = ai.getController();
		UserModel user = controller.getSessionAttr(OjConstants.USER);
		
		if (user == null) // user does not login
		{
			String name = controller.getCookie(OjConstants.TOKEN_NAME);
			String token = controller.getCookie(OjConstants.TOKEN_TOKEN);
			if (StringUtil.isNotBlank(name) && StringUtil.isNotBlank(token)) // try to auto login with cookie
			{
				user = UserModel.dao.autoLogin(name, token);
				if (user != null)
				{
					controller.setSessionAttr(OjConstants.USER, user);
					
					int uid = user.getInt("uid");
					if (user.isAdmin(uid)) // current user has admin role
						controller.setSessionAttr(OjConstants.ADMIN_USER, uid);
					
					log.info("User " + name + " login automatically.");
				}
				else
				{
					log.warn("Auto login for user " + name + " failed!");
				}
			}
		}

		if (user != null) // if user is logined, set user information in controller
		{
			int uid = user.getInt("uid");
			controller.setAttr(OjConstants.USER_ID, uid);
			controller.setAttr(OjConstants.USER_NAME, user.getStr("name"));
			controller.setAttr(OjConstants.USER_EMAIL, user.getStr("email"));
			if (controller.getSessionAttr(OjConstants.ADMIN_USER) != null)
				controller.setAttr(OjConstants.ADMIN_USER, uid);
		}
		
		ai.invoke();
	}

}
