package com.power.oj.contest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class ClarificationsInterceptor implements Interceptor {

    private static final ContestService contestService = ContestService.me();

    @Override
    public void intercept(Invocation ai) {
        Controller controller = ai.getController();
        Integer cid = 0;

        if (controller.getParaToInt("cid") != null) {
            cid = controller.getParaToInt("cid");
        } else if (controller.getParaToInt(0) != null) {
            cid = controller.getParaToInt(0);
        }

        Long timestamp = controller.getCookieToLong("clarify-" + cid, 0L);
        controller.setAttr("clarifications", contestService.getUnreadClarifications(cid, timestamp));

        ai.invoke();
    }
}
