package com.power.oj.cprogram.admin.experiment;

import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjController;
import com.power.oj.cprogram.admin.AdminService;

import java.util.List;

public class ExprimentController extends OjController {
    public void index() {
        List<ContestModel> contestList = AdminService.getContestListForSelect("EXPERIMENT");
        setAttr("contestList", contestList);
        render("index.ftl");
    }
}
