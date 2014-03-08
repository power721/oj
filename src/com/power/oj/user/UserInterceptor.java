package com.power.oj.user;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.shiro.ShiroKit;

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
    
    if (ShiroKit.isUser()) // if user is logined, set user information in controller
    {
      UserModel userModel = UserService.me().getCurrentUser();
      controller.setAttr(OjConstants.USER, userModel);
    }

    ai.invoke();
  }

}
