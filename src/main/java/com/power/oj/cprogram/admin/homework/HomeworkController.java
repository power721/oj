package com.power.oj.cprogram.admin.homework;

import com.jfinal.aop.Before;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.cprogram.admin.AdminService;
import com.power.oj.cprogram.interceptor.VarInterceptor;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiresPermissions("teacher")
@Before(VarInterceptor.class)
public class HomeworkController extends OjController {
    public void index() {
        List<ContestModel> contestList = AdminService.getContestListForSelect("HOMEWORK");
        setAttr("contestList", contestList);
        render("index.ftl");
    }

    public void add() {
        setAttr("teacherList", CProgramService.getTeacherList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long ctime = OjConfig.startInterceptorTime + 3600000;
        setAttr("startTime", sdf.format(new Date(ctime)));
        setAttr("endTime", sdf.format(new Date(ctime + 7 * 24 * 3600 * 1000)));
        render("add.ftl");
    }

}
