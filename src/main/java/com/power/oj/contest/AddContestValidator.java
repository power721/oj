package com.power.oj.contest;

import com.jfinal.core.Controller;
import com.jfinal.i18n.I18n;
import com.jfinal.validate.Validator;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddContestValidator extends Validator {

    @Override
    protected void validate(Controller c) {
        int type = c.getParaToInt("contest.type");
        if (type == ContestModel.TYPE_PASSWORD) {
            validateRequired("contest.password", "passMsg", I18n.use().get("contest.add.validate.pass"));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            long startTime = sdf.parse(c.getPara("startTime")).getTime();
            long endTime = sdf.parse(c.getPara("endTime")).getTime();
            if (startTime <= endTime) {
                return;
            }
        } catch (ParseException e) {
            if (OjConfig.isDevMode())
                e.printStackTrace();
        }
        addError("timeMsg", "Contest time is incorrect!");
    }

    @Override
    protected void handleError(Controller c) {
        c.keepModel(ContestModel.class, "contest");
        c.keepPara(OjConstants.PAGE_TITLE);

        c.render("add.html");
    }

}
