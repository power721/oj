package com.power.oj.admin;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresPermissions("admin")
@RequiresAuthentication
public class ContestAdminController extends OjController {
    public void index() {
        int pageNumber = getParaToInt(0, 1);
        int pageSize = getParaToInt(1, OjConfig.contestPageSize);
        Integer type = getParaToInt("type", -1);
        Integer status = getParaToInt("status", -1);

        setAttr("contestList", contestService.getContestList(pageNumber, pageSize, type, status));
        setAttr("pageSize", OjConfig.contestPageSize);

        setTitle(getText("contest.index.title"));
    }

    @RequiresPermissions("contest:add")
    public void add() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long ctime = OjConfig.startInterceptorTime + 3600000;
        setAttr("startDateTime", sdf.format(new Date(ctime)));
        setAttr("endDateTime", sdf.format(new Date(ctime + 18000000)));
        setAttr("contest_languages", OjConfig.languageName);
        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);

        setTitle(getText("contest.add.title"));
    }

    @Before(POST.class)
    @RequiresPermissions("contest:add")
    public void save() {
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        ContestModel contestModel = getModel(ContestModel.class, "contest");

        contestService.addContest(contestModel, startTime, endTime);

        redirect(new StringBuilder(2).append("/contest/show/").append(contestModel.getCid()).toString());
    }

    @RequiresPermissions("contest:edit")
    public void edit() {
        Integer cid = getParaToInt(0);
        ContestModel contestModle = contestService.getContest(cid);

        setAttr("cid", cid);
        setAttr("contest_languages", contestService.getLanguages(cid));
        setAttr("contest", contestModle);

        setTitle(getText("contest.edit.title"));
    }

    @Before(POST.class)
    @RequiresPermissions("contest:edit")
    public void update() {
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        ContestModel contestModel = getModel(ContestModel.class, "contest");

        contestService.updateContest(contestModel, startTime, endTime);

        redirect(new StringBuilder(2).append("/contest/show/").append(contestModel.getCid()).toString());
    }

    @RequiresPermissions("contest:build")
    public void build() {
        Integer cid = getParaToInt(0);
        contestService.build(cid);

        redirect(new StringBuilder(2).append("/contest/rank/").append(cid).toString(),
            new FlashMessage(getText("contest.buildRank.success")));
    }

}
