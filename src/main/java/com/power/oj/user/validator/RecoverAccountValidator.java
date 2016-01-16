package com.power.oj.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.i18n.I18n;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserService;

public class RecoverAccountValidator extends Validator {

    @Override
    protected void validate(Controller c) {
        validateEmail("email", "errorMsg", I18n.use().get("validate.email.error"));

        String name = c.getPara("name");
        String email = c.getPara("email");
        if (UserService.me().getUserByNameAndEmail(name, email) == null) {
            addError("emailMsg", I18n.use().get("recover.account.validate.error"));
        }

        String captcha = c.getPara("captcha").toUpperCase();
        if (!CaptchaRender.validate(c, captcha, OjConstants.RANDOM_CODE_KEY)) {
            addError("captchaMsg", I18n.use().get("validate.captcha.error"));
        }
    }

    @Override
    protected void handleError(Controller c) {
        c.keepPara("name");
        if (c.getAttr("emailMsg") == null)
            c.keepPara("email");
        c.setAttr(OjConstants.PAGE_TITLE, I18n.use().get("user.forget.title"));
        c.render("forget.html");
    }

}
