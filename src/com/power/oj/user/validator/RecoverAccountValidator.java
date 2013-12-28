package com.power.oj.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserModel;

public class RecoverAccountValidator extends Validator
{

  @Override
  protected void validate(Controller c)
  {
    validateEmail("email", "errorMsg", "Invalid Email address!");
    
    String name = c.getPara("name");
    String email = c.getPara("email");
    if (UserModel.dao.getUserByNameAndEmail(name, email) == null)
    {
      addError("emailMsg", "This account is not registered with this email!");
    }

    String captcha = c.getPara("captcha").toUpperCase();
    if (!CaptchaRender.validate(c, captcha, OjConstants.randomCodeKey))
    {
      addError("captchaMsg", "The captcha is incorrect!");
    }

  }

  @Override
  protected void handleError(Controller c)
  {
    c.keepPara("name");
    if (c.getAttr("emailMsg") == null)
      c.keepPara("email");
    c.setAttr(OjConstants.PAGE_TITLE, "Account Recovery");
    c.render("forget.html");
  }

}
