package com.power.oj.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.Message;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.service.SessionService;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;

public class ResetPasswordValidator extends Validator
{

  @Override
  protected void validate(Controller c)
  {
    validateRegex("pass", ".{6,20}", "passwordMsg", "Password length is between 6 and 20.");
    validateEqualField("pass", "repass", "confirmMsg", "The password not equal!");
    
    String name = c.getPara("name");
    String token = c.getPara("token");
    UserModel userModel = UserService.me().getUserByName(name);

    if (userModel != null && token != null && token.equals(userModel.getStr("token")))
    {
      return;
    }

    addError("tokenMsg", "Forbidden!");
  }

  @Override
  protected void handleError(Controller c)
  {
    if (c.getAttr("tokenMsg") != null)
    {
      Message msg = new Message("Validate token failed!", MessageType.ERROR, "Error!");
      ((OjController) c).redirect(SessionService.me().getLastAccessURL(), msg);
      return;
    }
    
    c.keepPara("name", "token", OjConstants.PAGE_TITLE);
    c.render("reset.html");
  }

}
