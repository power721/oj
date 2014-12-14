package com.power.oj.core.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;

public class SessionAttrInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		HttpSession session = controller.getSession();
		Enumeration<String> sessionKeys = session.getAttributeNames();
		while (sessionKeys.hasMoreElements()) {
			String key = sessionKeys.nextElement();
			Object value = session.getAttribute(key);
			controller.setAttr(key, value);
		}

		if (controller.getSessionAttr(OjConstants.MSG) != null) {
			controller.removeSessionAttr(OjConstants.MSG);
			controller.removeSessionAttr(OjConstants.MSG_TYPE);
			controller.removeSessionAttr(OjConstants.MSG_TITLE);
		}

		ai.invoke();
	}
}
