package com.power.oj.cprogram.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.power.oj.cprogram.CProgramService;

public class SignUpInterceptor implements Interceptor {
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
        ai.invoke();
    }
}
