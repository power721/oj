package com.power.oj.cprogram.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.cprogram.model.CprogramInfoModel;

/**
 * Created by w70373710691d on 2017/6/14.
 */
public class CProgramContestInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation ai) {
        if (!CProgramService.isRegister() && !ai.getActionKey().equals("/cprogram/signup")) {
            ai.getController().redirect("/cprogram/signup");
            return;
        }
        if (CProgramService.needReSignUp() && !ai.getActionKey().equals("/cprogram/resignup")) {
            ai.getController().redirect("/cprogram/resignup");
            return;
        }
        Integer cid = ai.getController().getParaToInt(0);
        if (cid != null && !ai.getActionKey().equals("/cprogram/list")) {
            CprogramInfoModel contest = CProgramService.getContest(cid);
            if (contest == null) {
                ai.getController().renderError(404);
                return;
            }
            ai.getController().setAttr("contest", contest);
        }
        ai.invoke();
    }
}