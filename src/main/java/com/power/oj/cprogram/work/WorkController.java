package com.power.oj.cprogram.work;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.cprogram.CProgramConstants;
import com.power.oj.cprogram.CProgramInterceptor;
import com.power.oj.cprogram.CProgramService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by w7037 on 2017/6/14.
 */
@Before(CProgramInterceptor.class)
public class WorkController extends OjController{
    public void index() {
        int pageNumber = getParaToInt(0, 1);
        int pageSize = CProgramConstants.PageSize;

        Page<Record> workList = WorkService.GetWorksList(pageNumber, pageSize);

        setAttr("workList", workList);
        setAttr("pageSize", CProgramConstants.PageSize);
    }

    @RequiresPermissions("teacher")
    public void add() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long ctime = OjConfig.startInterceptorTime + 3600000;
        setAttr("startDateTime", sdf.format(new Date(ctime)));
        setAttr("endDateTime", sdf.format(new Date(ctime + 7 * 24 * 3600 * 1000)));
    }

    @Before(POST.class)
    @RequiresPermissions("teacher")
    public void save() {
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        String title = getPara("title");
        String time = getPara("time");
        int uid = CProgramService.GetUid();
        Record rd = new Record();
        rd.set("title", title);
        rd.set("uid", uid);
        rd.set("time", time);
        rd.set("startTime", startTime);
        rd.set("endTime", endTime);
        rd.set("type", 1);
        Db.save("cprogram_work", rd);
        redirect("/cprogram/work");
    }

    @RequiresPermissions("teacher")
    @Before(WorkInterceptor.class)
    public void manager() {
        Integer wid = getParaToInt(0);
        Record work = CProgramService.GetWork(wid);
        List<Record> problems = CProgramService.GetProblemList(wid);
        setAttr("work", work);
        setAttr("workProblems", problems);
    }

    @Before(POST.class)
    @RequiresPermissions("teacher")
    public void addProblem() {
        Integer wid = getParaToInt("wid");
        Integer pid = getParaToInt("pid");
        String title = getPara("title");
        int result = CProgramService.AddProblem(wid, pid, title);
        renderJson("result", result);
    }

    @Before(POST.class)
    @RequiresPermissions("teacher")
    public void removeProblem() {
        Integer wid = getParaToInt("wid");
        Integer pid = getParaToInt("pid");
        int result = CProgramService.removeProblem(wid, pid);
        if (result > 0) {
            renderJson("{\"success\":true}");
        } else if (result == -1) {
            renderJson("{\"success\":false, \"result\":\"Cannot delete problem.\"}");
        } else {
            renderJson("{\"success\":false, \"result\":\"Delete problem failed.\"}");
        }
    }
}
