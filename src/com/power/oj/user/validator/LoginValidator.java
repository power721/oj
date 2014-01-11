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
    Subject currentUser = SecurityUtils.getSubject();
    UserModel userModel = (UserModel) currentUser.getPrincipal();
    if (userModel != null)
    {
      c.redirect("/");
      return;
    }

    // validateEmail("mail", "msg", "邮箱格式不正确");
    validateString("password", OjConstants.PASSWORD_MIN_LENGTH, OjConstants.PASSWORD_MAX_LENGTH, "msg", c.getText("validate.password.length"));
  }

  @Override
  protected void handleError(Controller c)
  {
    c.setAttr(OjConstants.PAGE_TITLE, c.getText("user.login.title"));
    c.keepPara("name");

    c.render("login.html");
  }

}
