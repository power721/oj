package com.power.oj.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserService;

public class RecoverAccountValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateEmail("email", "errorMsg", c.getText("validate.email.error"));

		String name = c.getPara("name");
		String email = c.getPara("email");
		if (UserService.me().getUserByNameAndEmail(name, email) == null) {
			addError("emailMsg", c.getText("recover.account.validate.error"));
		}

		String captcha = c.getPara("captcha").toUpperCase();
		if (!CaptchaRender.validate(c, captcha, OjConstants.RANDOM_CODE_KEY)) {
			addError("captchaMsg", c.getText("validate.captcha.error"));
		}
	}

	@Override
	protected void handleError(Controller c) {
		c.keepPara("name");
		if (c.getAttr("emailMsg") == null)
			c.keepPara("email");
		c.setAttr(OjConstants.PAGE_TITLE, c.getText("user.forget.title"));
		c.render("forget.html");
	}

}
