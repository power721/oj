package com.power.oj.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

public class SessionAttrInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation ai) {
        Controller controller = ai.getController();
        HttpSession session = controller.getSession();
        Enumeration sessionKeys = session.getAttributeNames();
        while (sessionKeys.hasMoreElements()) {
            String key = (String) sessionKeys.nextElement();
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
