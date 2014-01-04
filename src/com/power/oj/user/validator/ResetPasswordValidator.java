package com.power.oj.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;

public class ResetPasswordValidator extends Validator
{
  private int minLnegth = 6;
  private int maxLength= 20;
  
  @Override
  protected void validate(Controller c)
  {
    validateRegex("pass", ".{6,20}", "passwordMsg", c.getText("validate.password.length").replaceAll("_min_", String.valueOf(minLnegth)).replaceAll("_max_", String.valueOf(maxLength)));
    validateEqualField("pass", "repass", "confirmMsg", c.getText("validate.password.confirm"));
  }

  @Override
  protected void handleError(Controller c)
  {
    c.setAttr(OjConstants.PAGE_TITLE, c.getText("user.reset.title"));
    c.render("reset.html");
  }

}
