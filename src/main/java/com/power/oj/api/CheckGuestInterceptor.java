package com.power.oj.api;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.power.oj.core.OjController;
import com.power.oj.shiro.ShiroKit;

public class CheckGuestInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation ai) {
        OjController controller = (OjController) ai.getController();

        if (ShiroKit.isGuest()) {
            controller.renderJson("{\"success\":false, \"result\":\"User does not login.\"}");
            return;
        }

        ai.invoke();
    }

}
