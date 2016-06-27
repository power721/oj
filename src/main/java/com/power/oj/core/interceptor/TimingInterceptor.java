package com.power.oj.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConfig;

public class TimingInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation ai) {
        OjConfig.startInterceptorTime = System.currentTimeMillis();
        OjConfig.timeStamp = (int) (OjConfig.startInterceptorTime / 1000);

        Controller controller = ai.getController();
        String actionKey = ai.getActionKey();

        ai.invoke();

        long invokeTime = System.currentTimeMillis() - OjConfig.startInterceptorTime;
        controller.setAttr("invokeTime", invokeTime);

        System.out.println(actionKey + " Action Invoke Time: " + invokeTime + " milliseconds");
        controller.setAttr("serverTime", System.currentTimeMillis() / 1000);
    }

}
