package com.power.oj.common;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StringKit;

public class BaseURLInterceptor implements Interceptor
{

	@Override
	public void intercept(ActionInvocation ai)
	{
		String baseUrl = OjConstants.baseUrl;
		Controller controller = ai.getController();
		
		if(StringKit.isBlank(baseUrl))
		{
			HttpServletRequest request = controller.getRequest();
	
			StringBuilder sb = new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName());
			if (request.getServerPort() != 80)
			{
				sb.append(":").append(request.getServerPort());
			}
			sb.append(request.getContextPath());
	
			baseUrl = sb.toString();
		}
		
		controller.setAttr("baseUrl", baseUrl);

		ai.invoke();
	}

}
