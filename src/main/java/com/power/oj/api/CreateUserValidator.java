package com.power.oj.api;

import com.jfinal.core.Controller;
import com.jfinal.i18n.I18n;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserModel;
import com.power.oj.user.validator.SignupValidator;

public class CreateUserValidator extends SignupValidator {

    @Override
    protected void validate(Controller c) {
        validatePassword();

        validateEmail("User.email", "emailMsg", I18n.use().get("validate.email.error"));

        validateUserName(c);
    }

    @Override
    protected void handleError(Controller c) {
        c.keepModel(UserModel.class, "User");
        c.setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);

        c.render("user/create.html");
    }

}
