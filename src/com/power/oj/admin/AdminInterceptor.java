package com.power.oj.admin;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.user.UserService;

public class AdminInterceptor implements Interceptor
{
  public void intercept(ActionInvocation ai)
  {
    OjController controller = (OjController) ai.getController();
    if (controller.getSessionAttr(OjConstants.ADMIN_USER) != null)
    {
      ai.invoke();
    } else
    {
      if (!UserService.isUser())
      {
        controller.setAttr(OjConstants.MSG_TYPE, "error");
        controller.setAttr(OjConstants.MSG_TITLE, "Error!");
        controller.setAttr(OjConstants.MSG, "Please login.");
        controller.render("../user/login.html");
      } else
      {
        controller.redirect("/", "Permission Denied.", "error", "Error!");
      }
    }
  }
}
