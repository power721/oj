package com.power.oj.user.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;

public class LoginInterceptor implements Interceptor
{
  @Override
  public void intercept(ActionInvocation ai)
  {
    OjController controller = (OjController) ai.getController();
    if (controller.getPrincipal() != null)
    {
      ai.invoke();
    } else
    {
      controller.keepPara(OjConstants.PAGE_TITLE);

      controller.setAttr(OjConstants.MSG_TYPE, "error");
      controller.setAttr(OjConstants.MSG_TITLE, "Error!");
      controller.setAttr(OjConstants.MSG, "Please login.");

      controller.render("../user/login.html");
    }
  }
}
