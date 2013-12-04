package com.power.oj.admin;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;

public class AdminInterceptor implements Interceptor
{
	public void intercept(ActionInvocation ai)
	{
		Controller controller = ai.getController();
		if (controller.getSessionAttr(OjConstants.ADMIN_USER) != null)
		{
			ai.invoke();
		} else
		{
			if (controller.getSessionAttr(OjConstants.USER) == null)
			{
				controller.setAttr(OjConstants.MSG_TYPE, "error");
				controller.setAttr(OjConstants.MSG_TITLE, "Error!");
				controller.setAttr(OjConstants.MSG, "Please login.");
				controller.render("../user/login.html");
			} else
			{
				controller.setSessionAttr(OjConstants.MSG_TYPE, "error");
				controller.setSessionAttr(OjConstants.MSG_TITLE, "Error!");
				controller.setSessionAttr(OjConstants.MSG, "Permission Denied.");
				controller.redirect("/");
			}
		}
	}
}
