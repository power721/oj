package com.power.oj.cprogram.admin;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.cprogram.CProgramConstants;
import com.power.oj.cprogram.CProgramService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by w7037 on 2017/6/14.
 */
@RequiresPermissions("teacher")
public class AdminController extends OjController{
    public void index() {
        Integer type = getParaToInt("type");
        if(type == null) {
            type = 0;
        }
        setAttr("type", type);
        if(type != 0) {
            List<Record> contestList = AdminService.GetContestListForSelect(type);
            setAttr("contestList", contestList);
        }
        else {
            setAttrs(com.power.oj.admin.AdminService.me().getSystemInfo());
            return;
        }
        String action = getPara("action","");
        setAttr("action", action);
        if(action.equals("add")) {
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
        setAttr("weeks", CProgramConstants.weeks);
        setAttr("lectures", CProgramConstants.lecture);
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
        query += "&action=manager";
        redirect("/cprogram/admin/?type=" + type + query);
    }
    @Before(POST.class)
    public void search() {
        String query = new String();
        Integer type = getParaToInt("type", ContestModel.TYPE_WORK);
        Integer cid = getParaToInt("cid");
        if(cid != null) {
            query += "&cid=" + cid;
        }
        Integer week = getParaToInt("week");
        if(week != null) {
            query += "&week=" + week;
        }
        Integer lecture = getParaToInt("lecture");
        if(lecture != null) {
            query += "&lecture=" + lecture;
        }
        query += "&action=score";
        redirect("/cprogram/admin/?type=" + type + query);
    }
}
