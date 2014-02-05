package com.power.oj.api;

import org.apache.shiro.authz.annotation.RequiresUser;

import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.ext.plugin.shiro.ClearShiro;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class UserApiController extends OjController
{
  private static final UserService userService = UserService.me();

  @RequiresUser
  public void splash()
  {
    UserModel userModel = getAttr(OjConstants.USER);
    setAttr(OjConstants.USER, userService.getLevel(userModel));
    
    render("ajax/splash.html");
  }

  public void profile()
  {
    if (ShiroKit.isGuest())
    {
      renderJson("{\"success\":false, \"result\":\"User does not login.\"}");
      return;
    }
    
    UserModel userModel = getAttr(OjConstants.USER);
    userModel.put("success", true);
    userModel.remove("token").remove("pass").remove("data");
    renderJson(userModel);
  }

  public void info()
  {
    int uid = 0;
    String name = "";
    UserModel userModel = null;

    if (isParaExists("uid"))
    {
      uid = getParaToInt("uid");
      userModel = userService.getUserInfoByUid(uid);
    } else if (isParaExists("name"))
    {
      name = getPara("name");
      userModel = userService.getUserInfoByName(name);
    }

    if (userModel == null)
    {
      renderJson("{result:'User does not exist!'}");
    } else
    {
      userModel.put("success", true);
      userModel.remove("token").remove("pass").remove("realname").remove("phone").remove("data");
      renderJson(userModel);
    }
  }
  
  public void checkin()
  {
    if (ShiroKit.isGuest())
    {
      renderJson("{\"success\":false, \"result\":\"User does not login.\"}");
      return;
    }
    
    int incExp = 0;
    UserModel userModel = getAttr(OjConstants.USER);
    if ((incExp = userService.checkin(userModel)) > 0)
    {
      renderJson("{\"success\":true, \"incexp\":" + incExp +"}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"User already checkin.\"}");
    }
  }
  
  public void signSubmit()
  {
    if (ShiroKit.isGuest())
    {
      renderJson("{\"success\":false, \"result\":\"User does not login.\"}");
      return;
    }
    
    String sign = HtmlEncoder.text(getPara("sign", "").trim());
    UserModel userModel = getAttr(OjConstants.USER);
    if (userModel.set("sign", sign).update())
    {
      renderJson("{\"success\":true}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Update sign failed.\"}");
    }
  }
  
  public void pwdSubmit()
  {
    if (ShiroKit.isGuest())
    {
      renderJson("{\"success\":false, \"result\":\"User does not login.\"}");
      return;
    }
    
    UserModel userModel = getAttr(OjConstants.USER);
    String origPwd = getPara("origPwd");
    String newPwd = getPara("newPwd");
    
    if (userService.checkPassword(userModel.getUid(), origPwd))
    {
      newPwd = BCrypt.hashpw(newPwd, BCrypt.gensalt());
      if (userModel.set("pass", newPwd).update())
      {
        renderJson("{\"success\":true}");
      }
      else
      {
        renderJson("{\"success\":false, \"result\":\"Update password failed.\"}");
      }
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Password is incorrect.\"}");
    }
  }

  @Before(POST.class)
  @ClearShiro
  public void signin()
  {
    String name = getPara("name").trim();
    String password = getPara("password");
    boolean rememberMe = getParaToBoolean("rememberMe", false);

    if (userService.login(name, password, rememberMe))
    {
      UserModel userModel = userService.getCurrentUser();
      setCookie("auth_key", String.valueOf(userModel.getUid()), OjConstants.COOKIE_AGE);
      setCookie("oj_username", name, OjConstants.COOKIE_AGE);
      renderJson("{\"success\":true}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"User signin failed.\"}");
    }
  }

}
