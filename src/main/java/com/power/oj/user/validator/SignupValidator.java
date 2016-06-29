package com.power.oj.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.i18n.I18n;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import jodd.util.StringUtil;

public class SignupValidator extends Validator {
    private static final int usernameMinLength = OjConstants.USERNAME_MIN_LENGTH;
    private static final int usernameMaxLength = OjConstants.USERNAME_MAX_LENGTH;
    private static final int passwordMinLength = OjConstants.PASSWORD_MIN_LENGTH;
    private static final int passwordMaxLength = OjConstants.PASSWORD_MAX_LENGTH;

    @Override
    protected void validate(Controller c) {
        String regExpression = String.format(".{%d,%d}", passwordMinLength, passwordMaxLength);
        String passwordMsg =
            String.format(I18n.use().get("validate.password.length"), passwordMinLength, passwordMaxLength);

        validateEmail("user.email", "emailMsg", I18n.use().get("validate.email.error"));
        validateRegex("user.password", regExpression, "passwordMsg", passwordMsg);
        validateEqualField("user.password", "repass", "confirmMsg", I18n.use().get("validate.password.confirm"));

        String email = c.getPara("user.email");
        if (StringUtil.isNotBlank(email) && UserService.me().containsEmail(email)) {
            addError("emailMsg", I18n.use().get("validate.email.exist"));
        }

        String username = c.getPara("user.name");
        if (StringUtil.isNotBlank(username) && UserService.me().containsUsername(username)) {
            addError("nameMsg", I18n.use().get("validate.name.exist"));
        } else if (StringUtil.isNotBlank(username) && !checkReservedName(username)) {
            addError("nameMsg", I18n.use().get("validate.name.reserved"));
        } else {
            regExpression = String.format("[a-zA-Z0-9_]{%d,%d}", usernameMinLength, usernameMaxLength);
            passwordMsg = String.format(I18n.use().get("validate.name.error"), usernameMinLength, usernameMaxLength);
            validateRegex("user.name", regExpression, "nameMsg", passwordMsg);
        }

        if (!c.validateCaptcha("captcha")) {
            addError("captchaMsg", I18n.use().get("validate.captcha.error"));
        }
    }

    @Override
    protected void handleError(Controller c) {
        c.keepModel(UserModel.class, "user");
        c.setAttr(OjConstants.PAGE_TITLE, I18n.use().get("user.signup.title"));

        c.render("signup.html");
    }

    public boolean checkReservedName(String name) {
        name = name.toLowerCase();

        String reservedNames[] = {"admin", "root", "system", "default", "avatar"};
        if (StringUtil.equalsOne(name, reservedNames) != -1)
            return false;

        if (name.startsWith("team") || name.startsWith("qq") || name.startsWith("sina"))
            return false;

        return true;
    }

}
