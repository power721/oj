package com.power.oj.cprogram.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.cprogram.model.CprogramInfoModel;

/**
 * Created by w703710691d on 17-6-21.
 */
public class WaitingInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        Integer cid = invocation.getController().getParaToInt(0);
        if (cid == null) cid = invocation.getController().getParaToInt("cid");
        Integer status = ContestService.me().getContestStatus(cid);
        if (cid != null && CprogramInfoModel.TYPE_EXPERIMENT.equals(CProgramService.getContest(cid).getType())) {
            invocation.invoke();
            return;
        }
        if (status == ContestModel.PENDING && !CProgramService.isTeacher()) {
            invocation.getController().redirect("/cprogram/pending/" + cid);
        } else {
            invocation.invoke();
        }

    }
}
