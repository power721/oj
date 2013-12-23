package com.power.oj.shiro;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class ShiroInViewInterceptor implements Interceptor
{

  @Override
  public void intercept(ActionInvocation ai)
  {
    OjController controller = (OjController) ai.getController();
    
    if (UserService.isUser()) // if user is logined, set user information in controller
    {
      UserModel userModel = UserService.getPrincipal();
      int uid = userModel.getUid();
      
      if (UserService.hasAnyRoles("admin,root"))
        controller.setAttr(OjConstants.ADMIN_USER, uid);
    }

    ai.invoke();
  }

}
