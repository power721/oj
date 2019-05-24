package com.power.oj.cprogram.admin.experiment;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.cprogram.admin.AdminService;
import com.power.oj.cprogram.admin.homework.HomeworkService;
import com.power.oj.cprogram.interceptor.CProgramContestInterceptor;
import com.power.oj.cprogram.interceptor.VarInterceptor;
import com.power.oj.cprogram.model.ScoreModel;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserService;
import jxl.write.WriteException;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiresPermissions("teacher")
@Before(VarInterceptor.class)
public class ExperimentController extends OjController {
    public void index() {
        List<ContestModel> contestList = AdminService.getContestListForSelect("EXPERIMENT");
        setAttr("contestList", contestList);
        render("index.ftl");
    }

    public void add() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long ctime = OjConfig.startInterceptorTime + 3600000;
        setAttr("startTime", sdf.format(new Date(ctime)));
        setAttr("endTime", sdf.format(new Date(ctime + 7 * 24 * 3600 * 1000)));
        render("add.ftl");
    }

    @Before(POST.class)
    public void save() {
        String title = getPara("title");
        String commit = getPara("commit");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer cid = CProgramService.saveContest(title, UserService.me().getCurrentUid(), startTime, endTime,
                "EXPERIMENT", null, null, commit);
        redirect("/cprogram/admin/experiment/manager/" + cid);
    }

    @Before(CProgramContestInterceptor.class)
    public void manager() {
        Integer cid = getParaToInt(0);
        List<Record> problems = ContestService.me().getContestProblems(cid, UserService.me().getCurrentUid());
        setAttr("problems", problems);
        List<ContestModel> contestList = AdminService.getContestListForSelect("EXPERIMENT");
        setAttr("contestList", contestList);
        render("manager.ftl");
    }

    @Before({CProgramContestInterceptor.class})
    public void edit() {
        List<ContestModel> contestList = AdminService.getContestListForSelect("EXPERIMENT");
        setAttr("contestList", contestList);
        render("edit.ftl");
    }

    @Before(POST.class)
    public void update() {
        Integer cid = getParaToInt(0);
        String title = getPara("title");
        String commit = getPara("commit");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        CProgramService.updateContest(cid, title, UserService.me().getCurrentUid(), startTime, endTime, null, null, commit);
        redirect("/cprogram/admin/experiment/manager/" + cid);
    }

    @Before({CProgramContestInterceptor.class})
    public void score() {
        Integer cid = getParaToInt(0);
        Integer tid = getParaToInt("tid");
        if (!ShiroKit.hasPermission("root")) {
            tid = UserService.me().getCurrentUid();
        }
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        setAttr("week", week);
        setAttr("lecture", lecture);
        setAttr("tid", tid);
        List<ScoreModel> scoreList = AdminService.getExprimentScoreList(cid, tid, week, lecture);
        setAttr("scoreList", scoreList);
        List<ContestModel> contestList = AdminService.getContestListForSelect("EXPERIMENT");
        setAttr("contestList", contestList);
        render("score.ftl");
    }

    public void all() {
        Integer tid = getParaToInt("tid");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        if (!ShiroKit.hasPermission("root")) {
            tid = UserService.me().getCurrentUid();
        }
        List<Map<String, Object>> result = ExperimentService.getAllScore(tid, week, lecture);
        setAttr("allScore", result);
        setAttr("week", week);
        setAttr("lecture", lecture);
        setAttr("tid", tid);
        setAttr("experimentList", ExperimentService.getExperimentWorkList());
        List<ContestModel> contestList = AdminService.getContestListForSelect("EXPERIMENT");
        setAttr("contestList", contestList);
        render("all.ftl");
    }

    public void getxls() {
        Integer tid = getParaToInt("tid");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        if (!ShiroKit.hasPermission("root")) {
            tid = UserService.me().getCurrentUid();
        }
        try {
            File xls = ExperimentService.getExperimentXls(tid, week, lecture);
            renderFile(xls);
        } catch (IOException | WriteException e) {
            e.printStackTrace();
            renderNull();
        }
    }
}
