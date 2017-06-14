package com.power.oj.cprogram;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * Created by w7037 on 2017/6/14.
 */
public class CProgramInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation ai) {
        if(CProgramService.isTeacher()) {
            ai.getController().setAttr(CProgramConstants.TeacherUser, true);
        }
        ai.invoke();
    }

}
