package com.power.oj.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;

/**
 * Get and Set flash message.<br>
 * The message stored in session before redirect.
 * 
 * @author power
 * 
 */
public class MessageInterceptor implements Interceptor
{

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    if (controller.getSessionAttr(OjConstants.MSG) != null)
    {
      controller.setAttr(OjConstants.MSG, controller.getSessionAttr(OjConstants.MSG));
      controller.setAttr(OjConstants.MSG_TYPE, controller.getSessionAttr(OjConstants.MSG_TYPE));
      controller.setAttr(OjConstants.MSG_TITLE, controller.getSessionAttr(OjConstants.MSG_TITLE));

      controller.removeSessionAttr(OjConstants.MSG);
      controller.removeSessionAttr(OjConstants.MSG_TYPE);
      controller.removeSessionAttr(OjConstants.MSG_TITLE);
    }

    ai.invoke();
  }

}
