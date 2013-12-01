package com.power.oj.user;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class LoginInterceptor implements Interceptor
{
	@Override
	public void intercept(ActionInvocation ai)
	{
		Controller controller = ai.getController();
		if (controller.getSessionAttr("user") != null)
		{
			ai.invoke();
		} else
		{
			controller.keepPara("pageTitle");
			controller.keepPara("redirectURI");

			controller.setAttr("msgType", "error");
			controller.setAttr("msgTitle", "Error!");
			controller.setAttr("msg", "Please login.");

			controller.render("../user/login.html");
		}
	}
}
