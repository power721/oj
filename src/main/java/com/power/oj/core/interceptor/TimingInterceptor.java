package com.power.oj.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConfig;

public class TimingInterceptor implements Interceptor
{

  @Override
  public void intercept(ActionInvocation ai)
  {
    OjConfig.startInterceptorTime = System.currentTimeMillis();
    OjConfig.timeStamp = (int) (OjConfig.startInterceptorTime / 1000);
    
    Controller controller = ai.getController();
    String actionKey = ai.getActionKey();
    
    ai.invoke();

    long invokeTime = System.currentTimeMillis() - OjConfig.startInterceptorTime;
    controller.setAttr("invokeTime", invokeTime);
    controller.setAttr("serverTime", OjConfig.timeStamp);
    
    System.out.println(new StringBuilder(4).append(actionKey).append(" Action Invoke Time: ")
        .append(invokeTime).append(" milliseconds").toString());
  }

}
