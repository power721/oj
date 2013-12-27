package com.power.oj.user.validator;

import com.jfinal.core.Controller;
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
      addError("errorMsg", "This account is not registered with this email!");
    }
  }

  @Override
  protected void handleError(Controller c)
  {
    c.setAttr(OjConstants.PAGE_TITLE, "Account Recovery");
    c.render("forget.html");
  }

}
