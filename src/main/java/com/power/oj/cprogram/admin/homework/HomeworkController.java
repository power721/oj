package com.power.oj.cprogram.admin.homework;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.cprogram.admin.AdminService;
import com.power.oj.cprogram.interceptor.CProgramContestInterceptor;
import com.power.oj.cprogram.interceptor.VarInterceptor;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
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

    @Before(POST.class)
    public void save() {
        String title = getPara("title");
        Integer uid = getParaToInt("uid");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Integer cid = CProgramService.saveContest(title, uid, startTime, endTime, "HOMEWORK", week, lecture, null);
        redirect("/cprogram/admin/homework/manager/" + cid);
    }

    @Before(CProgramContestInterceptor.class)
    public void manager() {
        Integer cid = getParaToInt(0);
        List<Record> problems = ContestService.me().getContestProblems(cid, UserService.me().getCurrentUid());
        setAttr("problems", problems);
        List<ContestModel> contestList = AdminService.getContestListForSelect("HOMEWORK");
        setAttr("contestList", contestList);
        render("manager.ftl");
    }

    @Before({CProgramContestInterceptor.class})
    public void edit() {
        setAttr("teacherList", CProgramService.getTeacherList());
        List<ContestModel> contestList = AdminService.getContestListForSelect("HOMEWORK");
        setAttr("contestList", contestList);
        render("edit.ftl");
    }

    @Before(POST.class)
    public void update() {
        Integer cid = getParaToInt(0);
        String title = getPara("title");
        Integer uid = getParaToInt("uid");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        CProgramService.updateContest(cid, title, uid, startTime, endTime, week, lecture, null);
        redirect("/cprogram/admin/homework/manager/" + cid);
    }

    @Before({CProgramContestInterceptor.class})
    public void score() {
        Integer cid = getParaToInt(0);
        List<Record> scoreList = AdminService.getContestScoreList(cid);
        setAttr("scoreList", scoreList);
        List<ContestModel> contestList = AdminService.getContestListForSelect("HOMEWORK");
        setAttr("contestList", contestList);
        render("score.ftl");
    }

    public void all() {
        Integer tid = getParaToInt("tid");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        if(!ShiroKit.hasPermission("root")) {
            tid = UserService.me().getCurrentUid();
        }
    }
}
