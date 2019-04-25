package com.power.oj.cprogram.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.cprogram.model.CprogramInfoModel;
import com.power.oj.user.UserService;

/**
 * Created by w703710691d on 17-6-21.
 */
public class ExamInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation invocation) {
        Controller con = invocation.getController();
        Integer cid = con.getParaToInt(0);
        if (cid == null) {
            cid = con.getParaToInt("cid");
        }
        if (cid == null) {
            cid = con.getParaToInt("solution.cid");
        }
        if (CProgramService.checkAccessContest(cid)) {
            invocation.invoke();
        } else {
            con.redirect("/cprogram/password/" + cid);
        }
    }
}
