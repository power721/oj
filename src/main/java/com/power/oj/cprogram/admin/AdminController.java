package com.power.oj.cprogram.admin;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.cprogram.CProgramConstants;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.user.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by w7037 on 2017/6/14.
 */
@RequiresPermissions("teacher")
public class AdminController extends OjController{
    private Integer GetType() {
        Integer type = getParaToInt("type");
        if(type == null) {
            type = 0;
        }
        setAttr("type", type);
        return type;
    }
    private String buildQuery() {
        String query = new String();
        Integer cid = getParaToInt("cid");
        if(cid != null) {
            query += "&cid=" + cid;
            setAttr("cid", cid);
        }
        Integer week = getParaToInt("week");
        if(week != null) {
            query += "&week=" + week;
            setAttr("WEEK", week);
        }
        Integer lecture = getParaToInt("lecture");
        if(lecture != null) {
            query += "&lecture=" + lecture;
            setAttr("LECTURE", lecture);
        }
        return query;
    }
    private void main() {
        setAttrs(com.power.oj.admin.AdminService.me().getSystemInfo());
    }
    private void add(int type) {
        setAttr("teacherList", CProgramService.GetTeacherList());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long ctime = OjConfig.startInterceptorTime + 3600000;
        setAttr("startTime", sdf.format(new Date(ctime)));
        if(type == ContestModel.TYPE_WORK) {
            setAttr("endTime", sdf.format(new Date(ctime + 7 * 24 * 3600 * 1000)));
        }
        else {
            setAttr("endTime", sdf.format(new Date(ctime + 2 * 3600 * 1000)));
        }
    }
    private void edit() {
        setAttr("techerList", CProgramService.GetTeacherList());
    }
    private void manager() {
        Integer cid = getParaToInt("cid");
        List<Record> problems = ContestService.me().getContestProblems(cid, UserService.me().getCurrentUid());
        setAttr("problems", problems);
        setAttr("number", 50);
    }
    private void score() {
        Integer type = getParaToInt("type");
        Integer cid = getParaToInt("cid");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        if(cid == -1) {

            return;
        }
        setAttr("scoreList", AdminService.GetScoreList(type, cid, week, lecture));
    }
    public void index() {
        Integer type = GetType();
        String action = getPara("action","");
        if(type == 0) {
            main();
            return;
        }
        List<Record> contestList = AdminService.GetContestListForSelect(type);
        setAttr("contestList", contestList);
        setAttr("weeks", CProgramConstants.weeks);
        setAttr("lectures", CProgramConstants.lecture);
        setAttr("action", action);
        String query = buildQuery();
        setAttr("query", query);
        if(action.equals("add")) {
            add(type);
        }
        else {
            Integer cid = getParaToInt("cid");
            if(cid == null) return;
            if(cid != -1) {
                ContestModel contest = ContestService.me().getContest(cid);
                setAttr("contest", contest);
            }
            if(action.equals("edit")) {
                edit();
            }
            if(action.equals("manager")) {
                manager();
            }
            if(action.equals("score")) {
                score();
            }
        }
    }

    @Before(POST.class)
    public void save() {
        Integer type = getParaToInt("type", ContestModel.TYPE_WORK);
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        ContestModel contestModel = getModel(ContestModel.class, "contest");
        contestModel.put("type", type);
        contestService.addContest(contestModel, startTime, endTime);
        Integer cid = contestModel.getCid();
        String query = "&cid=" + cid;
        if(type != ContestModel.TYPE_EXPERIMENT) {
            query += "&week=" + contestModel.getLockBoardTime();
            query += "&lecture=" + contestModel.getUnlockBoardTime();
        }
        String action = "&action=manager";
        redirect("/cprogram/admin/?type=" + type + query + action);
    }
    @Before(POST.class)
    public void update() {
        Integer cid = getParaToInt("cid");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        ContestModel contestModel = contestService.getContest(cid);
        ContestModel newContestModel = getModel(ContestModel.class, "contest");
        contestModel.put(newContestModel);
        contestService.updateContest(contestModel, startTime, endTime);
        String query = buildQuery();
        Integer type = GetType();
        String action = "&action=manager";
        redirect("/cprogram/admin/?type=" + type + query + action);
    }
    @Before(POST.class)
    public void search() {
        String query = buildQuery();
        Integer type = GetType();
        String action = "&action=score";
        redirect("/cprogram/admin/?type=" + type + query + action);
    }
}
