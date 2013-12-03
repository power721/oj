package com.power.oj.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * Get and Set flash message.<br>
 * The message stored in session before redirect.
 * @author power
 *
 */
public class MessageInterceptor implements Interceptor
{

	@Override
	public void intercept(ActionInvocation ai)
	{
		Controller controller = ai.getController();
		if(controller.getSessionAttr("msg") != null)
		{
			controller.setAttr("msg", controller.getSessionAttr("msg"));
			controller.setAttr("msgType", controller.getSessionAttr("msgType"));
			controller.setAttr("msgTitle", controller.getSessionAttr("msgTitle"));
			
			controller.removeSessionAttr("msg");
			controller.removeSessionAttr("msgType");
			controller.removeSessionAttr("msgTitle");
		}
		
		ai.invoke();
	}

}
