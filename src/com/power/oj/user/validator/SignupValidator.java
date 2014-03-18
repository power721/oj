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
  private static final int usernameMinLength = OjConstants.USERNAME_MIN_LENGTH;
  private static final int usernameMaxLength= OjConstants.USERNAME_MAX_LENGTH;
  private static final int passwordMinLength = OjConstants.PASSWORD_MIN_LENGTH;
  private static final int passwordMaxLength= OjConstants.PASSWORD_MAX_LENGTH;
  
  @Override
  protected void validate(Controller c)
  {
    String regExpression = String.format(".{%d,%d}", passwordMinLength, passwordMaxLength);
    String passwordMsg = String.format(c.getText("validate.password.length"), passwordMinLength, passwordMaxLength);
    
    validateEmail("user.email", "emailMsg", c.getText("validate.email.error"));
    validateRegex("user.password", regExpression, "passwordMsg", passwordMsg);
    validateEqualField("user.password", "repass", "confirmMsg", c.getText("validate.password.confirm"));

    String email = c.getPara("user.email");
    if (StringUtil.isNotBlank(email) && UserService.me().containsEmail(email))
    {
      addError("emailMsg", c.getText("validate.email.exist"));
    }

    String username = c.getPara("user.name");
    if (StringUtil.isNotBlank(username) && UserService.me().containsUsername(username))
    {
      addError("nameMsg", c.getText("validate.name.exist"));
    } else if (StringUtil.isNotBlank(username) && !checkReservedName(username))
    {
      addError("nameMsg", c.getText("validate.name.reserved"));
    } else
    {
      regExpression = String.format("[a-zA-Z0-9_]{5,15}", usernameMinLength, usernameMaxLength);
      passwordMsg = String.format(c.getText("validate.name.error"), usernameMinLength, usernameMaxLength);
      validateRegex("user.name", regExpression, "nameMsg", passwordMsg);
    }

    String captcha = c.getPara("captcha").toUpperCase();
    if (!CaptchaRender.validate(c, captcha, OjConstants.RANDOM_CODE_KEY))
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

    if (name.startsWith("team") || name.startsWith("qq") || name.startsWith("sina"))
      return false;

    return true;
  }
  
}
