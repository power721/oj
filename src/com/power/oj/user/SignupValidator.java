package com.power.oj.user;

import jodd.util.StringUtil;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.validate.Validator;
import com.power.oj.common.OjConstants;

public class SignupValidator extends Validator
{

	@Override
	protected void validate(Controller c)
	{
		validateEmail("user.email", "emailMsg", "Invalid Email address!");
		validateRegex("user.pass", ".{6,20}", "passwordMsg", "Password length is between 6 and 20.");
		// validateEqualField("user.pass", "repass", "confirmMsg",
		// "The password not equal!");

		String email = c.getPara("user.email");
		if (StringUtil.isNotBlank(email) && UserModel.dao.containEmail(email))
		{
			addError("emailMsg", "This Email already registered!");
		}

		String username = c.getPara("user.name");
		if (StringUtil.isNotBlank(username) && UserModel.dao.containUsername(username))
		{
			addError("nameMsg", "This user name already registered!");
		} else if (StringUtil.isNotBlank(username) && !validateName(username))
		{
			addError("nameMsg", "This user name is reserved!");
		} else
		{
			validateRegex("user.name", "[a-zA-Z0-9_]{5,15}", "nameMsg",
					"Username only contins 5~15 letters, numbers and underscores.");
		}
		
		String captcha = c.getPara("captcha").toUpperCase();
		if(!CaptchaRender.validate(c, captcha, OjConstants.randomCodeKey))
		{
			addError("captchaMsg", "The captcha is incorrect!");
		}
		
	}

	@Override
	protected void handleError(Controller c)
	{
		c.keepModel(UserModel.class, "user");
		c.setAttr("pageTitle", "Signup");

		c.render("signup.html");
	}

	public boolean validateName(String name)
	{
		name = name.toLowerCase();

		String reverseNames[] = { "admin", "system", "default", "avatar" };
		if (StringUtil.equalsOne(name, reverseNames) != -1)
			return false;

		if (name.startsWith("team"))
			return false;

		return true;
	}
}
