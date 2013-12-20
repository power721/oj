package com.power.oj.user.interceptor;

import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserModel;

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
    Controller controller = ai.getController();
    UserModel user = controller.getSessionAttr(OjConstants.USER);

    if (user == null) // user does not login
    {
      String name = controller.getCookie(OjConstants.TOKEN_NAME);
      String token = controller.getCookie(OjConstants.TOKEN_TOKEN);
      if (StringUtil.isNotBlank(name) && StringUtil.isNotBlank(token)) // try to auto login with cookie
      {
        try
        {
          user = UserModel.dao.autoLogin(name, token);
 
          controller.setSessionAttr(OjConstants.USER, user);

          int uid = user.getUid();
          if (user.isAdmin(uid)) // current user has admin role
            controller.setSessionAttr(OjConstants.ADMIN_USER, uid);

          log.info("User " + name + " login automatically.");
        } catch(AutoLoginException e)
        {
          controller.removeCookie(OjConstants.TOKEN_NAME);
          controller.removeCookie(OjConstants.TOKEN_TOKEN);
          log.warn(e.getMessage());
        }
      }
    }

    if (user != null) // if user is logined, set user information in controller
    {
      int uid = user.getUid();
      controller.setAttr(OjConstants.USER_ID, uid);
      controller.setAttr(OjConstants.USER_NAME, user.getStr("name"));
      controller.setAttr(OjConstants.USER_EMAIL, user.getStr("email"));
      if (controller.getSessionAttr(OjConstants.ADMIN_USER) != null)
        controller.setAttr(OjConstants.ADMIN_USER, uid);
    }

    ai.invoke();
  }

}
