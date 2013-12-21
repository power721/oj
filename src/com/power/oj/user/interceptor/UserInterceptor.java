package com.power.oj.user.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

/**
 * Get and Set user session and controller attributes, auto login.
 * 
 * @author power
 * 
 */
public class UserInterceptor implements Interceptor
{
  protected static final Logger log = Logger.getLogger(UserInterceptor.class);

  @Override
  public void intercept(ActionInvocation ai)
  {
    OjController controller = (OjController) ai.getController();
    UserModel userModel = UserService.getPrincipal();
    
    if (UserService.isUser()) // if user is logined, set user information in controller
    {
      int uid = userModel.getUid();
      controller.setAttr(OjConstants.USER_ID, uid);
      controller.setAttr(OjConstants.USER_NAME, userModel.getStr("name"));
      controller.setAttr(OjConstants.USER_EMAIL, userModel.getStr("email"));
      if (controller.getSessionAttr(OjConstants.ADMIN_USER) != null)
        controller.setAttr(OjConstants.ADMIN_USER, uid);
    }

    ai.invoke();
  }

}
