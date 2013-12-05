package com.power.oj.core.interceptor;

import jodd.util.StringBand;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConfig;

/**
 * Global interceptor<br>
 * Print the Action invoking time<br>
 * 
 * @author power
 * 
 */
public class GlobalInterceptor implements Interceptor
{
	public void intercept(ActionInvocation ai)
	{
		OjConfig.startGlobalInterceptorTime = System.currentTimeMillis();
		Controller controller = ai.getController();

		// controller.setAttr("baseUrl", OjConfig.baseUrl); // move to
		// UrlFiterHandler
		// controller.setAttr("siteTitle", OjConfig.siteTitle);

		String actionKey = ai.getActionKey();
		controller.setAttr("actionKey", actionKey.replace("/", ""));
		String controllerKey = ai.getControllerKey();
		controller.setAttr("controllerKey", controllerKey.replace("/", ""));
		String methodName = ai.getMethodName();
		controller.setAttr("methodName", methodName);

		ai.invoke();

		System.out.println(new StringBand(4).append(actionKey).append(" Action Invoking Time: ")
				.append(System.currentTimeMillis() - OjConfig.startGlobalInterceptorTime).append(" milliseconds").toString());
	}
}
