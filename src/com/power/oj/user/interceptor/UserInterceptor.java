package com.power.oj.user.interceptor;

import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
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
    OjController controller = (OjController) ai.getController();
    UserModel userModel = controller.getPrincipal();

    if (userModel == null) // user does not login
    {
      String name = controller.getCookie(OjConstants.TOKEN_NAME);
      String token = controller.getCookie(OjConstants.TOKEN_TOKEN);
      if (StringUtil.isNotBlank(name) && StringUtil.isNotBlank(token)) // try to auto login with cookie
      {
        try
        {
          userModel = UserModel.dao.autoLogin(name, token);
 
          //controller.setSessionAttr(OjConstants.USER, userModel);

          // TODO: use Shiro role
          int uid = userModel.getUid();
          if (userModel.isAdmin(uid)) // current user has admin role
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

    if (userModel != null) // if user is logined, set user information in controller
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
