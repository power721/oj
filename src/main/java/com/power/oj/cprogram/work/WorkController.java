package com.power.oj.cprogram.work;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.cprogram.CProgramConstants;
import com.power.oj.cprogram.CProgramInterceptor;
import com.power.oj.cprogram.CProgramService;
import com.power.oj.problem.ProblemModel;
import com.power.oj.problem.ProblemService;
import jodd.util.StringUtil;
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
        long wid = rd.getLong("id");
        redirect("/cprogram/work/manager/" + wid);
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

    @Before(WorkInterceptor.class)
    public void show() {
        Integer wid = getParaToInt(0);
        Record work = CProgramService.GetWork(wid);
        List<Record> problems = CProgramService.GetProblemList(wid);
        setAttr("work", work);
        setAttr("workProblems", problems);
    }

    @Before(POST.class)
    @RequiresPermissions("teacher")
    public void reorderProblem() {
        Integer wid = getParaToInt("wid");
        String pid = getPara("pid");
        if (CProgramService.reorderProblem(wid, pid) > 0) {
            renderJson("{\"success\":true}");
        } else {
            renderJson("{\"success\":false, \"result\":\"Unknown error!\"}");
        }
    }

    @Before(WorkInterceptor.class)
    public void problem() {
        Integer wid = getParaToInt(0);
        Integer letter = Integer.valueOf(getPara(1).charAt(0)) - 'A';
        if (letter == null) {
            forwardAction("/cprogram/work/show/" + wid);
            return;
        }
        Record problem = CProgramService.GetProblem(wid, letter);
        if(problem == null) {
            FlashMessage msg =
                    new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
            redirect("/cprogram/work/show/" + wid, msg);
            return;
        }
        Integer uid = CProgramService.GetUid();
        Integer pid = problem.getInt("pid");
        if(uid != null) {
            Boolean result = CProgramService.HaveAccepted(wid, uid, pid);
            setAttr("userResult", result);
        }

        List<Record> workProblems = CProgramService.GetProblemList(wid);
        Integer prevPid = letter;
        if (letter > 0) {
            prevPid = letter - 1;
        }
        Integer nextPid = letter;
        if (letter + 1 < workProblems.size()) {
            nextPid = letter + 1;
        }
        setAttr("prevPid", prevPid);
        setAttr("nextPid", nextPid );
        setAttr("spj", problemService.checkSpj(pid));
        setAttr("problem", problem);
        setAttr("workProblems", workProblems);
        setAttr("work", CProgramService.GetWork(wid));
        boolean status = !CProgramService.isWorkFinished(wid) && !CProgramService.isWorkPending(wid);
        setAttr("cstatus", status);
    }

    @Before(WorkInterceptor.class)
    public void submit() {
        Integer wid = getParaToInt(0);
        Integer letter = Integer.valueOf(getPara(1).charAt(0)) - 'A';
        boolean ajax = getParaToBoolean("ajax", false);
        if(ajax == false) {
            FlashMessage msg =
                    new FlashMessage("请不要使用老版本浏览器", MessageType.WARN, getText("message.warn.title"));
            redirect("/cprogram/work/show/" + wid, msg);
            return;
        }
        Record problem = CProgramService.GetProblem(wid, letter);
        setAttr("problem", problem);
        setAttr("wid", wid);
        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);
        render("ajax/submit.html");
    }

    @Before(WorkInterceptor.class)
    @RequiresPermissions("teacher")
    public void edit() {
        Integer wid = getParaToInt(0);
        Record work = CProgramService.GetWork(wid);
        setAttr("work", work);
    }
    @Before({WorkInterceptor.class, POST.class})
    @RequiresPermissions("teacher")
    public void update() {
        Integer wid = getParaToInt(0);
        Record work = Db.findById("cprogram_work", wid);
        work.set("title", getPara("title"));
        work.set("time", getPara("time"));
        work.set("startTime", getPara("startTime"));
        work.set("endTime", getPara("endTime"));
        Db.update("cprogram_work", work);
        redirect("/cprogram/work/manager/" + wid);
    }
    @Before(WorkInterceptor.class)
    public void status() {
        Integer wid = getParaToInt(0);
        int pageNumber = getParaToInt(1, 1);
        int pageSize = getParaToInt(2, OjConfig.statusPageSize);
        Integer result = getParaToInt("result", -1);
        Integer language = getParaToInt("language", 0);
        Integer letter = -1;
        if (StringUtil.isNotBlank(getPara("letter"))) {
            try {
                letter = getParaToInt("letter");
            } catch (Exception e) {
                letter = getPara("letter").toUpperCase().charAt(0) - 'A';
            }
        }
        String userName = getPara("name");

        if(!CProgramService.isTeacher()) {
            userName = userService.getCurrentUserName();

        }
        setAttr("name", userName);

        StringBuilder query = new StringBuilder().append("?");
        if (result > -1) {
            query.append("&result=").append(result);
        }
        if (language > 0) {
            query.append("&language=").append(language);
        }
        if (letter > -1) {
            query.append("&letter=").append((char)('A' + letter));
        }
        if (StringUtil.isNotBlank(userName)) {
            query.append("&name=").append(userName);
        }
        setAttr("workProblems", CProgramService.GetProblemList(wid));
        setAttr("solutionList",
                CProgramService.GetSolutionPage(pageNumber, pageSize, result, language, wid, letter, userName));
        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);
        setAttr(OjConstants.JUDGE_RESULT, OjConfig.judgeResult);
        setAttr("result", result);
        setAttr("language", language);
        setAttr("letter", letter);


        setAttr("query", query.toString());
        setAttr("work", CProgramService.GetWork(wid));
    }
}
