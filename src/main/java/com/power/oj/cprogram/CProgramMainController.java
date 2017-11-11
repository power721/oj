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
import com.power.oj.problem.ProblemModel;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by w703710691d on 2017/6/14.
 */
@RequiresAuthentication
@Before(CProgramInterceptor.class)
public class CProgramMainController extends OjController {
    public void index() {

    }

    public void list() {
        Integer type = getParaToInt("type", ContestModel.TYPE_WORK);
        int pageNumber = getParaToInt(0, 1);
        int pageSize = CProgramConstants.PageSize;
        Page<ContestModel> page = CProgramService.getContestList(pageNumber, pageSize, type);
        setAttr("contestList", page);
        setAttr("pageSize", CProgramConstants.PageSize);
        setAttr("type", type);
        render("list.html");
    }

    @RequiresPermissions("teacher")
    public void add() {
        Integer type = getParaToInt("type", ContestModel.TYPE_WORK);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long ctime = OjConfig.startInterceptorTime + 3600000;
        setAttr("type", type);
        setAttr("startTime", sdf.format(new Date(ctime)));
        if(type == ContestModel.TYPE_WORK) {
            setAttr("endTime", sdf.format(new Date(ctime + CProgramConstants.oneWeek)));
        }
        else {
            setAttr("endTime", sdf.format(new Date(ctime + CProgramConstants.twoHours)));
        }
        setAttr("techerList", CProgramService.getTeacherList());
    }

    @Before(POST.class)
    @RequiresPermissions("teacher")
    public void save() {
        Integer type = getParaToInt("type", ContestModel.TYPE_WORK);
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        ContestModel contestModel = getModel(ContestModel.class, "contest");
        contestModel.put("type", type);
        contestService.addContest(contestModel, startTime, endTime);
        Integer cid = contestModel.getCid();
        redirect("/cprogram/manager/" + cid);
    }

    @Before({WaitingInterceptor.class, ExamInterceptor.class})
    public void show() {
        Integer cid = getParaToInt(0);
        List<Record> problems = ContestService.me().getContestProblems(cid, UserService.me().getCurrentUid());
        setAttr("problems", problems);
    }

    @RequiresPermissions("teacher")
    public void manager() {
        show();
        setAttr("number", 50);
    }

    @Before({WaitingInterceptor.class, ExamInterceptor.class})
    public void problem() {
        Integer cid = getParaToInt(0);
        String problemId = getPara(1);
        if(problemId == null) {
            redirect("/cprogram/show/" + cid);
            return;
        }

        char id = problemId.toUpperCase().charAt(0);
        Integer num = id - 'A';
        int status = contestService.getContestStatus(cid);
        setAttr("cstatus", status);
        ProblemModel problemModel = contestService.getProblem4Show(cid, num, status);
        if (problemModel == null) {
            FlashMessage msg =
                    new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
            redirect("/cprogram/show/" + cid, msg);
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
    }

    @RequiresAuthentication
    @Before({WaitingInterceptor.class, ExamInterceptor.class})
    public void submit() {
        Integer cid = getParaToInt(0);
        String problemId = getPara(1, "A");
        char id = problemId.toUpperCase().charAt(0);
        Integer num = id - 'A';

        if (contestService.isContestFinished(cid)) {
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
        render("ajax/submit.html");
    }

    @Before({WaitingInterceptor.class, ExamInterceptor.class})
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
        if(!CProgramService.isTeacher()) {
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
            query.append("&num=").append((char)('A' + num));
        }
        if (StringUtil.isNotBlank(userName)) {
            query.append("&name=").append(userName);
        }

        if(!CProgramService.isTeacher() && userName == null) {
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
    }

    @RequiresAuthentication
    @Before({WaitingInterceptor.class, ExamInterceptor.class})
    public void code() {
        Integer cid = getParaToInt("cid");
        Integer sid = getParaToInt("sid");
        ContestSolutionModel solution = CProgramService.getSolution(cid, sid);
        if(solution == null) {
            renderJson("{\"success\":false,\"result\":\"Cannot find code.\"}");
            return;
        }
        Integer uid = userService.getCurrentUid();
        if(uid == null || !CProgramService.isTeacher() && !uid.equals(solution.getUid())) {
            renderJson("{\"success\":false,\"result\":\"Permission denied.\"}");
            return;
        }
        Integer num = solution.getNum();
        ResultType resultType = OjConfig.resultType.get(solution.getResult());
        solution.setSource(HtmlEncoder.text(solution.getSource()));
        setAttr("success", true);
        setAttr("language", OjConfig.languageName.get(solution.getLanguage()));
        setAttr("id", (char)('A' + num));
        setAttr("problemTitle", contestService.getProblemTitle(cid, num));
        setAttr("resultLongName", resultType.getLongName());
        setAttr("resultName", resultType.getName());
        setAttr("solution", solution);
        String brush = getAttrForStr("language").toLowerCase();
        if(StringUtil.isBlank(brush)) brush = "c";
        if(brush.contains("GCC")) brush = "c";
        else if(brush.contains("G++")) brush = "cc";
        else if(brush.contains("Python")) brush = "py";
        else if(brush.contains("Java")) brush = "java";
        setAttr("brush", brush);
        renderJson(
                new String[] {"success", "letter", "problemTitle", "language", "resultLongName", "resultName", "solution",
                        "brush"});
    }
    @RequiresPermissions("teacher")
    public void edit() {
        setAttr("techerList", CProgramService.getTeacherList());
    }

    @RequiresPermissions("teacher")
    @Before(POST.class)
    public void update() {
        Integer cid = getParaToInt(0);
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        ContestModel contestModel = contestService.getContest(cid);
        ContestModel newContestModel = getModel(ContestModel.class, "contest");
        contestModel.put(newContestModel);
        contestService.updateContest(contestModel, startTime, endTime);
        redirect("/cprogram/manager/" + cid);
    }

    @RequiresAuthentication
    @Before({WaitingInterceptor.class, POST.class, ExamInterceptor.class})
    public void submitSolution() {
        ContestSolutionModel solution = getModel(ContestSolutionModel.class, "solution");
        int result = contestService.submitSolution(solution);
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

    @Before({WaitingInterceptor.class, ExamInterceptor.class})
    public void score() {
        Integer cid = getParaToInt(0);
        ContestModel contestModel = ContestService.me().getContest(cid);
        List<Record> user = CProgramService.getScoreList(cid, contestModel.getType());
        List<Record> teacherList = CProgramService.getTeacherList();
        HashMap<Integer, String> teacherMap = new HashMap<>();
        for(Record teacher: teacherList)
            teacherMap.put(teacher.getInt("uid"), teacher.getStr("realName"));
        for(Record u : user)
            u.set("teacher", teacherMap.get(u.getInt("tid")));
        setAttr("scoreList", user);
    }

    @Before(POST.class)
    @RequiresPermissions("teacher")
    public void updateFinalScore() {
        String userid = getPara("name");
        if(userid.startsWith("user")) {
            Integer uid = Integer.parseInt(userid.substring(4));
            Integer score;
            try {
                score = getParaToInt("value");
            }
            catch (Exception ex) {
                renderJson("修改失败");
                return ;
            }
            if(score != null && uid != null && score >=0 && score <=100) {
                CProgramService.updateFinalScore(getParaToInt(0), uid, score);
                renderNull();
                return;
            }
        }
        renderJson("修改失败");
    }

    @RequiresAuthentication
    public void password() {
        Integer cid = getParaToInt(0);
        if(cid == null) redirect("/cprogram");
        if(ExamInterceptor.CanAccess(cid)) {
            redirect("/cprogram/show/" + cid);
        }
    }

    @RequiresAuthentication
    @Before(POST.class)
    public void checkPassword() {
        Integer cid = getParaToInt(0);
        if(ExamInterceptor.CanAccess(cid)) {
            redirect("/cprogram/show" + "cid");
        }
        String password = getPara("password");
        Record record = Db.findFirst("select * from cprogram_password where cid = ? and password = ? and uid = 0", cid, password);
        if(record == null) {
            FlashMessage msg =
                    new FlashMessage("密码错误", MessageType.ERROR, getText("message.error.title"));
            redirect("/cprogram/password/" + cid, msg);
            return;
        }
        Integer uid = UserService.me().getCurrentUid();
        record.set("uid", uid);
        Db.update("cprogram_password", record);
        ContestService.me().addUser(cid, uid);
        redirect("/cprogram/show/" + cid);
    }
    @Before(ExamInterceptor.class)
    public void pending() {
        Integer cid = getParaToInt(0);
        if(ContestService.me().getContestStatus(cid) != ContestModel.PENDING) {
            redirect("/cprogram/show" + cid);
        }
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
        if(CProgramService.isRegister()) {
            redirect("/cprogram");
            return;
        }
        setAttr("techerList", CProgramService.getTeacherList());
    }

    @Before(POST.class)
    @Clear(CProgramInterceptor.class)
    public void signupUser() {
        if(CProgramService.isRegister()) {
            redirect("/cprogram");
            return;
        }
        Integer uid = UserService.me().getCurrentUid();
        UserModel user = UserService.me().getUser(uid);
        user.setRealName(getPara("realName"));
        user.setPhone(getPara("phone"));
        user.update();

        Record userext = new Record();
        userext.set("uid", uid);
        userext.set("class", getPara("class"));
        userext.set("stuid", getPara("stuid"));
        userext.set("tid", getParaToInt("tid"));
        userext.set("class_week", getParaToInt("class_week"));
        userext.set("class_lecture", getParaToInt("class_lecture"));
        userext.set("ctime", OjConfig.timeStamp);
        Db.save("cprogram_user_info", "uid", userext);

        redirect("/cprogram");
    }

    public void resignup() {
        /*
        if(!CProgramService.needReSignUp()) {
            redirect("/cprogram");
            return;
        }
        */
        Integer uid = UserService.me().getCurrentUid();
        Record User = Db.findFirst("select " +
                "user.uid, " +
                "user.realName, " +
                "user.phone, " +
                "cprogram_user_info.class as Class, " +
                "cprogram_user_info.class_week, " +
                "cprogram_user_info.class_lecture, " +
                "cprogram_user_info.tid, " +
                "cprogram_user_info.stuid " +
                "from user " +
                "inner join cprogram_user_info on user.uid = cprogram_user_info.uid " +
                "where user.uid = ?", uid);
        setAttr("techerList", CProgramService.getTeacherList());
        setAttr("User", User);
    }

    @Before(POST.class)
    @Clear(CProgramInterceptor.class)
    public void reSignupUser() {
        /*
        if(!CProgramService.needReSignUp()) {
            redirect("/cprogram");
            return;
        }*/
        Integer uid = UserService.me().getCurrentUid();
        UserModel user = UserService.me().getUser(uid);
        user.setRealName(getPara("realName"));
        user.setPhone(getPara("phone"));
        user.update();

        Record userext = Db.findById("cprogram_user_info", "uid", uid);
        userext.set("class", getPara("class"));
        userext.set("stuid", getPara("stuid"));
        userext.set("tid", getParaToInt("tid"));
        userext.set("class_week", getParaToInt("class_week"));
        userext.set("class_lecture", getParaToInt("class_lecture"));
        userext.set("ctime", OjConfig.timeStamp);
        Db.update("cprogram_user_info", "uid", userext);
        redirect("/cprogram");
    }
}
