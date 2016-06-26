package com.power.oj.core.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;
import com.power.oj.notice.NoticeService;

/**
 * Global interceptor<br>
 * Print the Action invoking time<br>
 *
 * @author power
 */
public class GlobalInterceptor implements Interceptor {

    public void intercept(Invocation ai) {
        Controller controller = ai.getController();

        String controllerKey = ai.getControllerKey();
        controller.setAttr(OjConstants.CONTROLLER_KEY, controllerKey.replaceFirst("/", ""));

        String actionKey = ai.getActionKey();
        controller.setAttr(OjConstants.ACTION_KEY, actionKey.replaceFirst("/", ""));

        String methodName = ai.getMethodName();
        controller.setAttr(OjConstants.METHOD_NAME, methodName);

        ai.invoke();

    }

}
