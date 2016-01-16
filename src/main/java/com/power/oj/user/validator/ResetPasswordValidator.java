package com.power.oj.user.validator;

import com.jfinal.core.Controller;
import com.jfinal.i18n.I18n;
import com.jfinal.validate.Validator;
import com.power.oj.core.OjConstants;

public class ResetPasswordValidator extends Validator {
    private static final int passwordMinLength = OjConstants.PASSWORD_MIN_LENGTH;
    private static final int passwordMaxLength = OjConstants.PASSWORD_MAX_LENGTH;

    @Override
    protected void validate(Controller c) {
        String regExpression = String.format(".{%d,%d}", passwordMinLength, passwordMaxLength);
        String passwordMsg =
            String.format(I18n.use().get("validate.password.length"), passwordMinLength, passwordMaxLength);

        validateRegex("password", regExpression, "passwordMsg", passwordMsg);
        validateEqualField("password", "repass", "confirmMsg", I18n.use().get("validate.password.confirm"));
    }

    @Override
    protected void handleError(Controller c) {
        c.setAttr(OjConstants.PAGE_TITLE, I18n.use().get("user.reset.title"));
        c.render("reset.html");
    }

}
