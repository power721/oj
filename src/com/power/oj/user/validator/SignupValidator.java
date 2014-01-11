package com.power.oj.user.validator;

import jodd.util.StringUtil;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class SignupValidator extends Validator
{
  private int minLnegth = 6;
  private int maxLength= 20;
  
  @Override
  protected void validate(Controller c)
  {
    validateEmail("user.email", "emailMsg", c.getText("validate.email.error"));
    validateRegex("user.pass", ".{6,20}", "passwordMsg", c.getText("validate.password.length").replaceAll("_min_", String.valueOf(minLnegth)).replaceAll("_max_", String.valueOf(maxLength)));
    validateEqualField("user.pass", "repass", "confirmMsg", c.getText("validate.password.confirm"));

    String email = c.getPara("user.email");
    if (StringUtil.isNotBlank(email) && UserService.me().containEmail(email))
    {
      addError("emailMsg", c.getText("validate.email.exist"));
    }

    String username = c.getPara("user.name");
    if (StringUtil.isNotBlank(username) && UserService.me().containUsername(username))
    {
      addError("nameMsg", c.getText("validate.name.exist"));
    } else if (StringUtil.isNotBlank(username) && !checkReservedName(username))
    {
      addError("nameMsg", c.getText("validate.name.reserved"));
    } else
    {
      validateRegex("user.name", "[a-zA-Z0-9_]{5,15}", "nameMsg", c.getText("validate.name.error"));
    }

    String captcha = c.getPara("captcha").toUpperCase();
    if (!CaptchaRender.validate(c, captcha, OjConstants.randomCodeKey))
    {
      addError("captchaMsg", c.getText("validate.captcha.error"));
    }
  }

  @Override
  protected void handleError(Controller c)
  {
    c.keepModel(UserModel.class, "user");
    c.setAttr(OjConstants.PAGE_TITLE, c.getText("user.signup.title"));

    c.render("signup.html");
  }

  public boolean checkReservedName(String name)
  {
    name = name.toLowerCase();

    String reservedNames[] =
    { "admin", "root", "system", "default", "avatar" };
    if (StringUtil.equalsOne(name, reservedNames) != -1)
      return false;

    if (name.startsWith("team"))
      return false;

    return true;
  }
  
}
