package com.power.oj.user;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;

public class LoginInterceptor implements Interceptor
{
	@Override
	public void intercept(ActionInvocation ai)
	{
		Controller controller = ai.getController();
		if (controller.getSessionAttr(OjConstants.USER) != null)
		{
			ai.invoke();
		} else
		{
			controller.keepPara(OjConstants.PAGE_TITLE);

			controller.setAttr(OjConstants.MSG_TYPE, "error");
			controller.setAttr(OjConstants.MSG_TITLE, "Error!");
			controller.setAttr(OjConstants.MSG, "Please login.");

			controller.render("../user/login.html");
		}
	}
}
