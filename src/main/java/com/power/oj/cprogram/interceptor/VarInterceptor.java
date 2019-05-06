package com.power.oj.cprogram.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.power.oj.cprogram.CProgramConstants;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.shiro.ShiroKit;

public class VarInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        invocation.invoke();
        Controller con = invocation.getController();
        if (ShiroKit.hasPermission(CProgramConstants.teacherPermissionString)) {
            con.setAttr(CProgramConstants.teacherUserString, true);
        }
        con.setAttr(CProgramConstants.weeksMapString, CProgramConstants.weeksMap);
        con.setAttr(CProgramConstants.contestTypeMapString, CProgramConstants.contestTypeMap);
        con.setAttr(CProgramConstants.lectureMapString, CProgramConstants.lectureMap);
        con.setAttr("pageSize", CProgramConstants.PageSize);
        con.setAttr("teacherList", CProgramService.getTeacherList());
        con.setAttr("contestType", con.getPara("contestType"));
    }
}
