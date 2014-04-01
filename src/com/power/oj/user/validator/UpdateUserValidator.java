package com.power.oj.user.validator;

import jodd.util.StringUtil;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class UpdateUserValidator extends Validator
{

  @Override
  protected void validate(Controller c)
  {
    validateEmail("user.email", "emailMsg", c.getText("validate.email.error"));

    Integer uid = UserService.me().getCurrentUid();
    /*
     * String email = c.getPara("user.email"); if (StringUtil.isNotBlank(email)
     * && UserModel.dao.containEmailExceptThis(uid, email)) {
     * addError("emailMsg", "This Email already registered!"); }
     */

    String password = c.getPara("user.password");
    if (StringUtil.isNotBlank(password))
    {
      validateEqualField("user.password", "repass", "confirmMsg", c.getText("validate.password.confirm"));
    }

    password = c.getPara("oldpass");
    if (!UserService.me().checkPassword(uid, password))
    {
      addError("passwordMsg", c.getText("validate.password.error"));
    }
  }

  @Override
  protected void handleError(Controller c)
  {
    c.keepModel(UserModel.class, "user");
    c.setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    c.setAttr(OjConstants.PAGE_TITLE, c.getText("user.edit.title"));

    c.render("edit.html");
  }

}
