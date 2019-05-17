package com.power.oj.cprogram.admin.experiment;

import com.jfinal.aop.Before;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjController;
import com.power.oj.cprogram.admin.AdminService;
import com.power.oj.cprogram.interceptor.VarInterceptor;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.util.List;

@RequiresPermissions("teacher")
@Before(VarInterceptor.class)
public class ExperimentController extends OjController {
    public void index() {
        List<ContestModel> contestList = AdminService.getContestListForSelect("EXPERIMENT");
        setAttr("contestList", contestList);
        render("index.ftl");
    }
}
