package com.power.oj.api;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.power.oj.core.OjController;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserService;

public class AdminInterceptor implements Interceptor
{

  @Override
  public void intercept(ActionInvocation ai)
  {
    OjController controller = (OjController) ai.getController();
    
    if (ShiroKit.isGuest())
    {
      controller.renderJson("{\"success\":false, \"result\":\"User does not login.\"}");
      return;
    }
    else if (!UserService.me().isAdmin())
    {
      controller.renderJson("{\"success\":false, \"result\":\"User has no permission.\"}");
      return;
    }

    ai.invoke();
  }

}
