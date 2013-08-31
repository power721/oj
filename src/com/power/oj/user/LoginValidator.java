package com.power.oj.user;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class LoginValidator extends Validator
{

	@Override
	protected void validate(Controller c)
	{
		// TODO Auto-generated method stub
		if (c.getRequest().getMethod() == "GET")
			return;
		if (c.getSessionAttr("user") != null)
		{
			c.redirect("/");
			return;
		}

		// validateEmail("mail", "msg", "邮箱格式不正确");
		validateString("password", 6, 12, "msg", "密码长度不正确");
	}

	@Override
	protected void handleError(Controller c)
	{
		// TODO Auto-generated method stub
		c.setAttr("pageTitle", "Login");
		c.keepPara("name");

		c.render("login.html");
	}

}
