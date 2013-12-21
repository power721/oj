package com.power.oj.user.validator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserModel;

public class LoginValidator extends Validator
{

  @Override
  protected void validate(Controller c)
  {
    if (c.getRequest().getMethod() == "GET")
      return;
    Subject currentUser = SecurityUtils.getSubject();
    UserModel userModel = (UserModel) currentUser.getPrincipal();
    if (userModel != null)
    {
      c.redirect("/");
      return;
    }

    // validateEmail("mail", "msg", "邮箱格式不正确");
    validateString("password", 6, 12, "msg", "密码长度不正确");
  }

  @Override
  protected void handleError(Controller c)
  {
    c.setAttr(OjConstants.PAGE_TITLE, "Login");
    c.keepPara("name");

    c.render("login.html");
  }

}
