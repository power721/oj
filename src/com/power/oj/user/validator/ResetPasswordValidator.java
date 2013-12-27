package com.power.oj.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;

public class ResetPasswordValidator extends Validator
{

  @Override
  protected void validate(Controller c)
  {
    validateRegex("pass", ".{6,20}", "passwordMsg", "Password length is between 6 and 20.");
    validateEqualField("pass", "repass", "confirmMsg", "The password not equal!");
  }

  @Override
  protected void handleError(Controller c)
  {
    c.setAttr(OjConstants.PAGE_TITLE, "Reset Password");
    c.render("reset.html");
  }

}
