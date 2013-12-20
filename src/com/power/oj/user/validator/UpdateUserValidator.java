package com.power.oj.user.validator;

import jodd.util.StringUtil;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserModel;

public class UpdateUserValidator extends Validator
{

  @Override
  protected void validate(Controller c)
  {
    validateEmail("user.email", "emailMsg", "Invalid Email address!");

    int uid = c.getParaToInt("user.uid");
    /*
     * String email = c.getPara("user.email"); if (StringUtil.isNotBlank(email)
     * && UserModel.dao.containEmailExceptThis(uid, email)) {
     * addError("emailMsg", "This Email already registered!"); }
     */

    String password = c.getPara("user.pass");
    if (StringUtil.isNotBlank(password))
    {
      validateEqualField("user.pass", "repass", "confirmMsg", "The password not equal!");
    }

    password = c.getPara("oldpass");
    if (!UserModel.dao.checkPass(uid, password))
    {
      addError("passwordMsg", "The password is incorrect!");
    }

  }

  @Override
  protected void handleError(Controller c)
  {
    c.keepModel(UserModel.class, "user");
    c.setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    c.setAttr(OjConstants.PAGE_TITLE, "Account");

    c.render("edit.html");
  }

}
