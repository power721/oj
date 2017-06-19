package com.power.oj.cprogram;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;

/**
 * Created by w7037 on 2017/6/14.
 */
public class CProgramInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation ai) {

        if(CProgramService.isTeacher()) {
            ai.getController().setAttr(CProgramConstants.TeacherUser, true);
        }
        Integer cid = ai.getController().getParaToInt(0);
        if(cid != null) {
            ContestModel contest = ContestService.me().getContest(cid);
            if(contest == null) {
                ai.getController().renderError(404);
                return;
            }
            ai.getController().setAttr("contest", contest);
        }
        ai.invoke();
    }


}
