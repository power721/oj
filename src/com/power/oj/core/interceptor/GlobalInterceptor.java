package com.power.oj.core.interceptor;

import jodd.util.StringBand;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;

/**
 * Global interceptor<br>
 * Store access uri in DB<br>
 * Print the Action invoking time<br>
 * @author power
 *
 */
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

		ai.invoke();
		
		System.out.println(new StringBand(4).append(actionKey).append(" Action Invoking Time: ").
				append(System.currentTimeMillis()-OjConstants.startGlobalInterceptorTime).append(" milliseconds").toString());
	}

	/**
	 * Encode the uri.
	 * @param uri
	 * @return encoded uri
	 * @deprecated
	 */
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
