package com.power.oj.cprogram;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.jfinal.plugin.activerecord.*;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.bean.ResultType;
import com.power.oj.cprogram.interceptor.*;
import com.power.oj.cprogram.model.CprogramInfoModel;
import com.power.oj.problem.ProblemModel;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by w703710691d on 2017/6/14.
 */
@RequiresUser
@Before({VarInterceptor.class, SignUpInterceptor.class})
public class CProgramController extends OjController {
    public void index() {
        render("index.ftl");
    }

    public void list() {
        String contestType = getPara("contestType", CprogramInfoModel.TYPE_HOMEWORK);
        int pageNumber = getParaToInt(0, 1);
        Page<ContestModel> page = CProgramService.getContestList(pageNumber, CProgramConstants.PageSize, contestType);
        setAttr("contestList", page);
        render("list.ftl");
    }

    @RequiresPermissions("teacher")
    public void add() {
        String contestType = getPara("contestType", CprogramInfoModel.TYPE_HOMEWORK);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long ctime = OjConfig.startInterceptorTime + 3600000;
        setAttr("startTime", sdf.format(new Date(ctime)));
        if (CprogramInfoModel.TYPE_HOMEWORK.equals(contestType) || CprogramInfoModel.TYPE_EXPERIMENT.equals(contestType)) {
            setAttr("endTime", sdf.format(new Date(ctime + CProgramConstants.oneWeek)));
        } else {
            setAttr("endTime", sdf.format(new Date(ctime + CProgramConstants.twoHours)));
        }

        render("add.ftl");
    }

    @Before(POST.class)
    @RequiresPermissions("teacher")
    public void save() {
        String contestType = getPara("contestType");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        String title = getPara("title");
        Integer uid = getParaToInt("uid");
        String commit = getPara("commit");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        Integer cid = CProgramService.saveContest(title, uid, startTime, endTime, contestType, week, lecture, commit);
        redirect("/cprogram/manager/" + cid);
    }

    @Before({CProgramContestInterceptor.class, WaitingInterceptor.class, ExamInterceptor.class})
    public void show() {
        Integer cid = getParaToInt(0);
        List<Record> problems = ContestService.me().getContestProblems(cid, UserService.me().getCurrentUid());
        setAttr("problems", problems);
        render("show.ftl");
    }

    @RequiresPermissions("teacher")
    @Before(CProgramContestInterceptor.class)
    public void manager() {
        Integer cid = getParaToInt(0);
        List<Record> problems = ContestService.me().getContestProblems(cid, UserService.me().getCurrentUid());
        setAttr("number", 50);
        setAttr("problems", problems);
        render("manager.ftl");
    }

    @Before({CProgramContestInterceptor.class, WaitingInterceptor.class, ExamInterceptor.class})
    public void problem() {
        Integer cid = getParaToInt(0);
        String problemId = getPara(1);
        if (problemId == null) {
            renderError(404);
            return;
        }
        char id = problemId.toUpperCase().charAt(0);
        Integer num = id - 'A';
        int status = contestService.getContestStatus(cid);
        setAttr("cstatus", status);
        ProblemModel problemModel = contestService.getProblem4Show(cid, num, status);
        if (problemModel == null) {
            renderError(404);
            return;
        }

        List<Record> contestProblems = contestService.getContestProblems(cid, null);
        Integer prevPid = num;
        if (num > 0) {
            prevPid = num - 1;
        }
        Integer nextPid = num;
        if (num + 1 < contestProblems.size()) {
            nextPid = num + 1;
        }

        setAttr("id", num);
        setAttr("prevPid", (char) (prevPid + 'A'));
        setAttr("nextPid", (char) (nextPid + 'A'));
        setAttr("spj", problemService.checkSpj(problemModel.getPid()));
        setAttr("userResult", contestService.getUserResult(cid, num));
        setAttr("Problems", contestProblems);
        setAttr("problem", problemModel);
        render("problem.ftl");
    }

    @Before({CProgramContestInterceptor.class, WaitingInterceptor.class, ExamInterceptor.class})
    public void submit() {
        Integer cid = getParaToInt(0);
        String problemId = getPara(1, "A");
        char id = problemId.toUpperCase().charAt(0);
        Integer num = id - 'A';

        if (!UserService.me().isAdmin() && contestService.isContestFinished(cid)) {
            FlashMessage msg =
                    new FlashMessage(getText("contest.submit.finished"), MessageType.WARN, getText("message.warn.title"));
            redirect("/cprogram/show/" + cid, msg);
            return;
        }

        ProblemModel problemModel = contestService.getProblem(cid, num);
        if (problemModel == null) {
            FlashMessage msg =
                    new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
            redirect("/cprogram/show/" + cid, msg);
            return;
        }

        if (isParaExists("s")) {
            setAttr("solution", contestService.getContestSolution(cid, getParaToInt("s", 0)));
        }

        setAttr("problem", problemModel);
        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);

        setTitle(getText("contest.problem.title") + cid + "-" + id + ": " + problemModel.getTitle());
        render("ajax/submit.ftl");
    }

    @Before({CProgramContestInterceptor.class, WaitingInterceptor.class, ExamInterceptor.class})
    public void status() {
        Integer cid = getParaToInt(0);
        int pageNumber = getParaToInt(1, 1);
        int pageSize = getParaToInt(2, OjConfig.statusPageSize);
        Integer result = getParaToInt("result", -1);
        Integer language = getParaToInt("language", 0);
        Integer num = -1;
        if (StringUtil.isNotBlank(getPara("num"))) {
            try {
                num = getParaToInt("num");
            } catch (Exception e) {
                num = getPara("num").toUpperCase().charAt(0) - 'A';
            }
        }
        String userName = getPara("name");
        if (!CProgramService.isTeacher()) {
            userName = CProgramService.getStuID();
        }
        setAttr("name", userName);

        StringBuilder query = new StringBuilder().append("?");
        if (result > -1) {
            query.append("&result=").append(result);
        }
        if (language > 0) {
            query.append("&language=").append(language);
        }
        if (num > -1) {
            query.append("&num=").append((char) ('A' + num));
        }
        if (StringUtil.isNotBlank(userName)) {
            query.append("&name=").append(userName);
        }

        if (!CProgramService.isTeacher() && userName == null) {
            userName = "NULL";
        }
        setAttr("Problems", contestService.getContestProblems(cid, 0));
        setAttr("solutionList",
                solutionService.getPageForContest(pageNumber, pageSize, result, language, cid, num, userName));
        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);
        setAttr(OjConstants.JUDGE_RESULT, OjConfig.judgeResult);
        setAttr("result", result);
        setAttr("language", language);
        setAttr("num", num);
        setAttr("query", query.toString());
        render("status.ftl");
    }

    @Before({WaitingInterceptor.class, ExamInterceptor.class})
    public void code() {
        Integer cid = getParaToInt("cid");
        Integer sid = getParaToInt("sid");
        Integer sim_id = getParaToInt("sim_id", 0);
        ContestSolutionModel solution = CProgramService.getSolution(cid, sid);
        ContestSolutionModel simSolution = CProgramService.getSolution(cid, sim_id);
        Integer uid = userService.getCurrentUid();
        Boolean isAdmin = CProgramService.isTeacher();
        if (solution == null || !isAdmin && !uid.equals(solution.getUid())) {
            renderJson("{\"success\":false,\"result\":\"Cannot find code.\"}");
            return;
        }
        if (simSolution != null && !solution.getSimID().equals(sim_id) && !isAdmin) {
            renderJson("{\"success\":false,\"result\":\"Cannot find code.\"}");
            return;
        }
        if (simSolution != null)
            solution = simSolution;
        Integer num = solution.getNum();
        ResultType resultType = OjConfig.resultType.get(solution.getResult());
        solution.setSource(HtmlEncoder.text(solution.getSource()));
        solution.setError(HtmlEncoder.text(solution.getError()));
        solution.setSystemError(HtmlEncoder.text(solution.getSystemError()));
        solution.setWrong(HtmlEncoder.text(solution.getWrong()));

        setAttr("success", true);
        setAttr("language", OjConfig.languageName.get(solution.getLanguage()));
        setAttr("id", (char) ('A' + num));
        setAttr("problemTitle", contestService.getProblemTitle(cid, num));
        setAttr("resultLongName", resultType.getLongName());
        String resultName = resultType.getName();
        setAttr("resultName", resultName);
        setAttr("solution", solution);

        if (resultName.equals("WA") ||
                resultName.equals("PE") ||
                resultName.equals("TLE") ||
                resultName.equals("MLE") ||
                resultName.equals("RE") ||
                resultName.equals("OLE") ||
                resultName.equals("RF")) {
            setAttr("inputData",
                    HtmlEncoder.text(solutionService.getInput(solution.getPid(), solution.getTest())));
        }

        String brush = getAttrForStr("language").toLowerCase();
        if (StringUtil.isBlank(brush)) brush = "c";
        if (brush.contains("GCC")) brush = "c";
        else if (brush.contains("G++")) brush = "cc";
        else if (brush.contains("Python")) brush = "py";
        else if (brush.contains("Java")) brush = "java";
        setAttr("brush", brush);
        renderJson(
                new String[]{"success", "letter", "problemTitle", "language", "resultLongName", "resultName", "solution",
                        "brush", "inputData"});
    }

    @RequiresPermissions("teacher")
    @Before({CProgramContestInterceptor.class})
    public void edit() {
        render("edit.ftl");
    }

    @RequiresPermissions("teacher")
    @Before(POST.class)
    public void update() {
        Integer cid = getParaToInt(0);
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        String title = getPara("title");
        Integer uid = getParaToInt("uid");
        String commit = getPara("commit");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        CProgramService.updateContest(cid, title, uid, startTime, endTime, week, lecture, commit);
        redirect("/cprogram/manager/" + cid);
    }

    @Before({WaitingInterceptor.class, POST.class, ExamInterceptor.class})
    public void submitSolution() {
        ContestSolutionModel solution = getModel(ContestSolutionModel.class, "solution");
        Boolean style = getParaToBoolean("style", false);
        int result = contestService.submitSolution(solution, style);
        if (result == -1) {
            setFlashMessage(
                    new FlashMessage(getText("solution.save.null"), MessageType.ERROR, getText("message.error.title")));
        } else if (result == -2) {
            setFlashMessage(
                    new FlashMessage(getText("solution.save.error"), MessageType.ERROR, getText("message.error.title")));
        }
        Integer cid = solution.getCid();
        redirect("/cprogram/status/" + cid);
    }


    @Before({WaitingInterceptor.class, ExamInterceptor.class, CProgramContestInterceptor.class})
    public void score() {
        Integer cid = getParaToInt(0);
        List<Record> scoreList = CProgramService.getScoreList(cid);
        setAttr("scoreList", scoreList);
        render("score.ftl");
    }

    @Before(POST.class)
    @RequiresPermissions("teacher")
    public void updateFinalScore() {
        String userid = getPara("name");
        if (userid.startsWith("user")) {
            Integer uid = Integer.parseInt(userid.substring(4));
            Integer score;
            try {
                score = getParaToInt("value");
            } catch (Exception ex) {
                renderJson("修改失败");
                return;
            }
            if (score != null && score >= 0 && score <= 100) {
                CProgramService.updateFinalScore(getParaToInt(0), uid, score);
                renderNull();
                return;
            }
        }
        renderJson("修改失败");
    }

    @Before(CProgramContestInterceptor.class)
    public void password() {
        Integer cid = getParaToInt(0);
        if (cid == null) redirect("/cprogram");
        if (CProgramService.checkAccessContest(cid)) {
            redirect("/cprogram/show/" + cid);
        }
        render("password.ftl");
    }

    @Before(POST.class)
    public void checkPassword() {
        Integer cid = getParaToInt(0);
        if (CProgramService.checkAccessContest(cid)) {
            redirect("/cprogram/show/" + cid);
        }
        String password = getPara("password");
        if (!CProgramService.checkContestPassword(cid, password)) {
            FlashMessage msg = new FlashMessage("密码错误", MessageType.ERROR, getText("message.error.title"));
            redirect("/cprogram/password/" + cid, msg);
            return;
        }
        redirect("/cprogram/show/" + cid);
    }

    @Before({CProgramContestInterceptor.class, ExamInterceptor.class})
    public void pending() {
        Integer cid = getParaToInt(0);
        if (ContestService.me().getContestStatus(cid) != ContestModel.PENDING) {
            redirect("/cprogram/show/" + cid);
        }
        render("pending.ftl");
    }


    @RequiresPermissions("teacher")
    @Before(POST.class)
    public void generate() {
        Integer cid = getParaToInt(0);
        Integer number = getParaToInt("number");
        File file = CProgramService.addPassword(cid, number);
        renderFile(file);
    }

    public void signup() {
        if (CProgramService.isRegister()) {
            redirect("/cprogram");
            return;
        }
        render("signup.ftl");
    }

    @Before(POST.class)
    @Clear(SignUpInterceptor.class)
    public void signupUser() {
        if (CProgramService.isRegister()) {
            redirect("/cprogram");
            return;
        }
        String userName = getPara("realName");
        String phone = getPara("phone");
        String classes = getPara("classes");
        Integer tid = getParaToInt("tid");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        String stuId = getPara("stuid");
        CProgramService.saveUser(userName, phone, classes, week, lecture, tid, stuId);
        redirect("/cprogram");
    }

    public void resignup() {
        UserModel user = CProgramService.getUserInfo();
        setAttr("User", user);
        render("resignup.ftl");
    }

    @Before(POST.class)
    @Clear(SignUpInterceptor.class)
    public void reSignupUser() {
        String userName = getPara("realName");
        String phone = getPara("phone");
        String classes = getPara("classes");
        Integer tid = getParaToInt("tid");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        String stuId = getPara("stuid");
        CProgramService.updateUser(userName, phone, classes, week, lecture, tid, stuId);
        redirect("/cprogram");
    }

    @Before({CProgramContestInterceptor.class})
    public void report() {
        Integer cid = getParaToInt(0);
        Integer uid = getParaToInt(1);
        if (!CProgramService.isTeacher()) {
            uid = UserService.me().getCurrentUid();
        }
        List<Record> problems = ContestService.me().getContestProblems(cid, UserService.me().getCurrentUid());
        setAttr("problems", problems);
        setAttr("report", CProgramService.getReportInfo(cid, uid));
        render("report.ftl");
    }

    public void updateReportInfo() {
        Integer cid = getParaToInt(0);
        Integer uid = UserService.me().getCurrentUid();
        String position = getPara("position");
        Integer machine = getParaToInt("machine");
        Integer times = getParaToInt("times");
        Integer week = getParaToInt("week");
        Integer lecture = getParaToInt("lecture");
        Integer res = CProgramService.updateReportInfo(cid, uid, position, machine, times, week, lecture);
        if (res == 0) {
            setAttr("success", true);
        } else {
            setAttr("success", false);
        }
        renderJson(new String[]{"success"});
    }
}
