package com.power.oj.admin;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class AdminInterceptor implements Interceptor
{
	public void intercept(ActionInvocation ai)
	{
		Controller controller = ai.getController();
		if (controller.getSessionAttr("adminUser") != null)
		{
			ai.invoke();
		} else
		{

			if (controller.getSessionAttr("user") == null)
			{
				controller.setAttr("msgType", "error");
				controller.setAttr("msgTitle", "Error!");
				controller.setAttr("msg", "Please login.");
				controller.render("../user/login.html");
			} else
			{
				controller.setSessionAttr("msgType", "error");
				controller.setSessionAttr("msgTitle", "Error!");
				controller.setSessionAttr("msg", "Permission Denied.");
				controller.redirect("/");
			}
		}
	}
}
