package com.power.oj.cprogram;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;

/**
 * Created by w703710691d on 17-6-21.
 */
public class WaitingInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {

//        Integer cid = invocation.getController().getParaToInt(0);
//        if(cid == null) cid = invocation.getController().getParaToInt("cid");
//        Integer status = ContestService.me().getContestStatus(cid);
//        if(cid != null && ContestService.me().getContest(cid).getType() == ContestModel.TYPE_EXPERIMENT) {
//            invocation.invoke();
//            return;
//        }
//        if(status == ContestModel.PENDING && !CProgramService.isTeacher()) {
//            invocation.getController().redirect("/cprogram/pending/" + cid);
//        }
//        else {
//            invocation.invoke();
//        }

    }
}
