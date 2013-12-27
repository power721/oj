package com.power.oj.core.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;

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
    OjConfig.timeStamp = OjConfig.startGlobalInterceptorTime / 1000;
    Controller controller = ai.getController();

    String controllerKey = ai.getControllerKey();
    controller.setAttr(OjConstants.CONTROLLER_KEY, controllerKey.replace("/", ""));
    
    String actionKey = ai.getActionKey();
    controller.setAttr(OjConstants.ACTION_KEY, actionKey.replace("/", ""));
    
    String methodName = ai.getMethodName();
    controller.setAttr(OjConstants.METHOD_NAME, methodName);

    ai.invoke();

    System.out.println(new StringBuilder(4).append(actionKey).append(" Action Invoking Time: ")
        .append(System.currentTimeMillis() - OjConfig.startGlobalInterceptorTime).append(" milliseconds").toString());
  }
  
}
