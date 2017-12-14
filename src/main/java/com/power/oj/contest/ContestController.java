package com.power.oj.contest;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.FileRender;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.ErrorFlashMessage;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.ProgramLanguageModel;
import com.power.oj.core.service.SessionService;
import com.power.oj.problem.ProblemModel;
import com.power.oj.util.CryptUtils;
import jodd.util.ArraysUtil;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Before({ContestInterceptor.class})
public class ContestController extends OjController {

    @Clear({ContestInterceptor.class})
    public void index() {
        int pageNumber = getParaToInt(0, 1);
        int pageSize = getParaToInt(1, OjConfig.contestPageSize);
        Integer type = getParaToInt("type", -1);
        Integer status = getParaToInt("status", -1);

        setAttr("contestList", contestService.getContestList(pageNumber, pageSize, type, status));
        setAttr("pageSize", OjConfig.contestPageSize);
        setAttr("status", status);

        setTitle(getText("contest.index.title"));
    }

    @Before(ClarificationsInterceptor.class)
    public void show() {
        Integer cid = getParaToInt(0);
        Integer uid = userService.getCurrentUid();

        ContestModel contestModel = contestService.getContest(cid);

        long serverTime = OjConfig.timeStamp;
        int startTime = contestModel.getStartTime();
        int endTime = contestModel.getEndTime();
        String status = getText("contest.status.running");

        if (startTime > serverTime)
            status = getText("contest.status.pending");
        else if (endTime < serverTime)
            status = getText("contest.status.finished");

        setAttr("contest", contestModel);
        setAttr("isRejudging", contestService.isRejudging(cid));
        setAttr("contestProblems", contestService.getContestProblems(cid, uid));
        setAttr("status", status);

        setTitle(getText("contest.show.title") + cid);
    }

    @Before(ClarificationsInterceptor.class)
    public void problem() {
        Integer cid = getParaToInt(0);
        String problemId = getPara(1);
        if (problemId == null) {
            forwardAction("/contest/allProblems/" + cid);
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
            redirect("/contest/show/" + cid, msg);
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
        setAttr("isRejudging", contestService.isRejudging(cid, num));
        setAttr("contestProblems", contestProblems);
        setAttr("problem", problemModel);

        setTitle(cid + "-" + id + ": " + problemModel.getTitle());
    }

    public void allProblems() {
        Integer cid = getParaToInt(0);
        setAttr("cid", cid);
        setAttr("contestProblems", contestService.getContestProblems(cid));
        setTitle("Contest " + cid);
    }

    // public void pdf() {
    // Integer cid = getParaToInt(0);
    // File pdf = contestService.renderProblems2pdf(cid);
    // if (pdf != null) {
    // renderFile(pdf);
    // } else {
    // renderNull();
    // }
    // }

    public void submit() {
        Integer cid = getParaToInt(0);
        String problemId = getPara(1, "A");
        char id = problemId.toUpperCase().charAt(0);
        Integer num = id - 'A';
        boolean ajax = getParaToBoolean("ajax", false);

        if (contestService.isContestFinished(cid)) {
            FlashMessage msg =
                    new FlashMessage(getText("contest.submit.finished"), MessageType.WARN, getText("message.warn.title"));
            redirect("/contest/show/" + cid, msg);
            return;
        }

        ProblemModel problemModel = contestService.getProblem(cid, num);
        if (problemModel == null) {
            FlashMessage msg =
                    new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
            redirect("/contest/show/" + cid, msg);
            return;
        }

        if (isParaExists("s")) {
            setAttr("solution", contestService.getContestSolution(cid, getParaToInt("s", 0)));
        }

        setAttr("problem", problemModel);
        setAttr(OjConstants.PROGRAM_LANGUAGES, contestService.getLanguages(cid));

        setTitle(getText("contest.problem.title") + cid + "-" + id + ": " + problemModel.getTitle());
        if (ajax)
            render("ajax/submit.html");
        else
            render("submit.html");
    }

    @Clear({ContestInterceptor.class})
    @Before(POST.class)
    public void submitSolution() {
        ContestSolutionModel contestSolution = getModel(ContestSolutionModel.class, "solution");
        Integer cid = contestSolution.getCid();
        String name = userService.getCurrentUserName();
        String url = "/contest/status/" + cid + "?name=" + name;
        ContestModel contestModle = contestService.getContest(cid);
        if (contestModle.getLanguages() != null) {
            String[] languages = StringUtils.split(contestModle.getLanguages(), ",");
            if (!ArraysUtil.contains(languages, String.valueOf(contestSolution.getLanguage()))) {
                setFlashMessage(
                        new FlashMessage("invalid language", MessageType.ERROR, getText("message.error.title")));
                redirect(url);
                return;
            }
        }

        int result = contestService.submitSolution(contestSolution);

        if (result == -1) {
            setFlashMessage(
                    new FlashMessage(getText("solution.save.null"), MessageType.ERROR, getText("message.error.title")));
        } else if (result == -2) {
            setFlashMessage(
                    new FlashMessage(getText("solution.save.error"), MessageType.ERROR, getText("message.error.title")));
        }

        redirect(url);
    }

    @Before(ClarificationsInterceptor.class)
    public void rank() {
        Integer cid = getParaToInt(0);
        int pageNumber = getParaToInt(1, 1);
        int pageSize = getParaToInt(2, OjConfig.contestRankPageSize);

        setAttr("pageSize", OjConfig.contestRankPageSize);
        setAttr("contestRank", contestService.getContestRank(pageNumber, pageSize, cid));
        setAttr("contestProblems", contestService.getContestProblems(cid, 0));
        setAttr("cstatus", contestService.getContestStatus(cid));
        setAttr("isLocked", contestService.checkFreezeBoard4Rank(cid));

        setTitle(getText("contest.rank.title") + cid);
    }

    @Before(POST.class)
    @RequiresPermissions("contest:edit")
    public void info() {
        Integer cid = getParaToInt(0);
        int grand = getParaToInt("grand", 1);
        int first = getParaToInt("first", 2);
        int second = getParaToInt("second", 5);
        int third = getParaToInt("third", 10);

        try {
            File file = contestService.getContextXML(cid, grand, first, second, third);
            renderFile(file);
        } catch (IOException e) {
            renderError(500);
        }
    }

    @Before(ClarificationsInterceptor.class)
    public void status() {
        Integer cid = getParaToInt(0);
        int pageNumber = getParaToInt(1, 1);
        int pageSize = getParaToInt(2, OjConfig.statusPageSize);
        Integer result = getParaToInt("result", -1);
        Integer language = getParaToInt("language", 0);
        Integer num = -1;

        if (StringUtil.isNotBlank(getPara("pid"))) {
            try {
                num = getParaToInt("pid");
            } catch (Exception e) {
                num = getPara("pid").toUpperCase().charAt(0) - 'A';
            }
        }
        String userName = getPara("name");
        ContestModel contestModel = contestService.getContest(cid);
        if (!userService.isAdmin() && contestModel.getType() > ContestModel.TYPE_PASSWORD
                && contestModel.getStartTime() <= OjConfig.timeStamp && contestModel.getEndTime() >= OjConfig.timeStamp) {
            userName = userService.getCurrentUserName();
        }
        StringBuilder query = new StringBuilder().append("?cid=").append(cid);

        if (result > -1) {
            query.append("&result=").append(result);
        }
        if (language > 0) {
            query.append("&language=").append(language);
        }
        if (num > -1) {
            query.append("&pid=").append(getPara("pid"));
        }
        if (StringUtil.isNotBlank(userName)) {
            query.append("&name=").append(userName);
        }

        // setAttr("contest", contestService.getContestById(cid));
        setAttr("contestProblems", contestService.getContestProblems(cid, 0));
        setAttr("solutionList",
                solutionService.getPageForContest(pageNumber, pageSize, result, language, cid, num, userName));
        setAttr(OjConstants.PROGRAM_LANGUAGES, contestService.getLanguages(cid));
        setAttr(OjConstants.JUDGE_RESULT, OjConfig.judgeResult);
        setAttr("result", result);
        setAttr("language", language);
        setAttr("pid", getPara("pid"));
        setAttr("name", userName);
        setAttr("query", query.toString());

        setTitle(getText("contest.status.title", cid));
    }

    @Before(ClarificationsInterceptor.class)
    public void problemStatus() {
        Integer cid = getParaToInt(0);
        char id = getPara(1, "A").toUpperCase().charAt(0);
        Integer num = id - 'A';
        ProblemModel problemModel = contestService.getProblem(cid, num);

        if (problemModel == null) {
            FlashMessage msg =
                    new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
            redirect("/contest/show/" + cid, msg);
            return;
        }

        setAttr(OjConstants.PROGRAM_LANGUAGES, contestService.getLanguages(cid));
        setAttr("language", getParaToInt("language"));
        setAttr("pageSize", OjConfig.statusPageSize);
        setAttr("resultList", solutionService.getProblemStatusForContest(cid, num));
        setAttr("problem", problemModel);
        setAttr("id", id);

        setTitle(getText("contest.status.title", cid, id));
    }

    @Before(ClarificationsInterceptor.class)
    public void code() {
        Integer cid = getParaToInt(0);
        Integer sid = getParaToInt(1);
        boolean isAdmin = userService.isAdmin();
        ContestSolutionModel solutionModel = solutionService.findContestSolution(sid);
        ResultType resultType = OjConfig.resultType.get(solutionModel.getResult());
        Integer uid = solutionModel.getUid();

        if (!isAdmin) {
            String error = solutionModel.getError();
            if (error != null) {
                solutionModel
                        .set("error", error.replace(StringUtil.replace(OjConfig.getString("workPath"), "\\", "\\\\"), ""));
                // TODO replace "/"
            }
        }

        String problemTitle = "";
        int num = solutionModel.getInt("num");
        problemTitle = contestService.getProblemTitle(cid, num);

        setAttr("alpha", (char) (num + 'A'));
        setAttr("cid", cid);
        setAttr("running", contestService.isContestRunning(cid));
        setAttr("problemTitle", problemTitle);

        try {
            setAttr("submitUser", userService.getUserByUid(uid));
        } catch (NullPointerException e) {
            log.warn(e.getLocalizedMessage());
        }

        ProgramLanguageModel language = OjConfig.languageType.get(solutionModel.getLanguage());
        setAttr("language", language.getName());
        setAttr("resultLongName", resultType.getLongName());
        setAttr("resultName", resultType.getName());
        setAttr("solution", solutionModel);

        String brush = getAttrForStr("language").toLowerCase();
        if (brush.contains("g++") || brush.contains("gcc")) brush = "cpp";
        if (brush.contains("python")) brush = "python";
        if (brush.contains("kotlin") || brush.contains("java")) brush = "java";
        setAttr("brush", brush);

        setTitle(getText("solution.show.title"));
    }

    @Before(ClarificationsInterceptor.class)
    public void statistics() {
        Integer cid = getParaToInt(0);
        List<String> resultName = new ArrayList<>();

        for (ResultType resultType : OjConfig.judgeResult) {
            if (resultType.getId() > ResultType.RF)
                break;
            resultName.add(resultType.getName());
        }
        resultName.add("Others");

        setAttr("resultName", resultName);
        setAttr("languageList", contestService.getLanguageList(cid)); // need ext
        setAttr("statistics", contestService.getContestStatistics(cid));

        setTitle(getText("contest.statistics.title") + cid);
    }

    public void clarify() {
        Integer cid = getParaToInt(0);
        Integer num = null;
        Integer uid = userService.getCurrentUid();
        if (isParaExists(1)) {
            char id = getPara(1, "A").toUpperCase().charAt(0);
            num = id - 'A';
            setAttr("pid", num);
        }

        setAttr("contestProblems", contestService.getContestProblems(cid, 0));
        if (userService.isAdmin()) {
            setAttr("clarifyList", contestService.getClarifyList(cid, num));
            render("adminClarify.html");
        } else {
            List<Record> privateClarifyList = contestService.getPrivateClarifyList(cid, num, uid);
            List<Record> publicClarifyList = contestService.getPublicClarifyList(cid, num);

            if (privateClarifyList.size() + publicClarifyList.size() > 0) {
                setCookie("clarify-" + cid, String.valueOf(OjConfig.timeStamp), OjConstants.COOKIE_AGE);
            }

            setAttr("privateClarifyList", privateClarifyList);
            setAttr("publicClarifyList", publicClarifyList);
            render("clarify.html");
        }
    }

    @Before(ClarificationsInterceptor.class)
    public void report() {

    }

    @Clear({ContestInterceptor.class})
    public void recent() {
        setTitle(getText("contest.recent.title"));
    }

    @Clear({ContestInterceptor.class})
    @Before(POST.class)
    public void password() {
        Integer cid = getParaToInt("cid");
        String password = getPara("password");

        if (contestService.checkContestPassword(cid, password)) {
            String tokenName = "cid-" + String.format("%04d", cid);
            String tokenToken = CryptUtils.encrypt(password, tokenName);
            setSessionAttr(tokenName, tokenToken);
            redirect(SessionService.me().getLastAccessURL());
            return;
        }

        keepPara("cid");
        keepPara("title");

        FlashMessage msg =
                new FlashMessage(getText("contest.password.error"), MessageType.ERROR, getText("message.error.title"));
        redirect(SessionService.me().getLastAccessURL(), msg);
    }

    @RequiresAuthentication
    @RequiresPermissions("contest:edit")
    @Before(ClarificationsInterceptor.class)
    public void edit() {
        boolean ajax = getParaToBoolean("ajax", false);

        Integer cid = getParaToInt(0);
        setAttr("contest_languages", contestService.getLanguages(cid));
        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);
        setTitle(getText("contest.edit.title"));
        render(ajax ? "ajax/edit.html" : "edit.html");
    }

    @Clear({ContestInterceptor.class})
    @Before(POST.class)
    @RequiresAuthentication
    @RequiresPermissions("contest:edit")
    public void update() {
        boolean ajax = getParaToBoolean("ajax", false);
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        ContestModel contestModel = getModel(ContestModel.class, "contest");

        contestModel.setLanguages(StringUtils.join(getParaMap().get("languages"), ","));
        contestService.updateContest(contestModel, startTime, endTime);

        if (ajax) {
            renderNull();
        } else {
            redirect("/contest/show/" + contestModel.getInt("cid"));
        }
    }

    @Clear({ContestInterceptor.class})
    @RequiresAuthentication
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

    @Clear({ContestInterceptor.class})
    @RequiresAuthentication
    @RequiresPermissions("contest:add")
    public void copy() {
        Integer cid = getParaToInt(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long ctime = OjConfig.startInterceptorTime + 3600000;
        setAttr("startDateTime", sdf.format(new Date(ctime)));
        setAttr("endDateTime", sdf.format(new Date(ctime + 18000000)));
        setAttr("contest_languages", contestService.getLanguages(cid));
        setAttr("contest", contestService.getContest(cid));
        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);

        setTitle(getText("contest.copy.title", cid));
    }

    @Clear({ContestInterceptor.class})
    @Before(POST.class)
    @RequiresAuthentication
    @RequiresPermissions("contest:add")
    public void save() {
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        Boolean includeProblems = getParaToBoolean("includeProblems");
        Boolean includeUsers = getParaToBoolean("includeUsers");
        ContestModel contestModel = getModel(ContestModel.class, "contest");

        contestModel.setLanguages(StringUtils.join(getParaMap().get("languages"), ","));
        if (contestModel.getCid() != null) {
            contestModel.put("includeProblems", includeProblems);
            contestModel.put("includeUsers", includeUsers);
            contestService.copyContest(contestModel, startTime, endTime);
        } else {
            contestService.addContest(contestModel, startTime, endTime);
        }

        redirect("/contest/admin/" + contestModel.getInt("cid"));
    }

    @RequiresAuthentication
    @RequiresPermissions("problem:edit")
    public void editProblem() {
        Integer cid = getParaToInt(0);
        String problemId = getPara(1, "A");
        char id = problemId.toUpperCase().charAt(0);
        Integer num = id - 'A';
        boolean ajax = getParaToBoolean("ajax", false);

        setAttr("cid", cid);
        setAttr("id", id);
        setAttr("num", num);
        setAttr("problem", contestService.getProblem(cid, num));
        setTitle(getText("problem.edit.title") + cid + "-" + num);

        render(ajax ? "ajax/editProblem.html" : "editProblem.html");
    }

    @Clear({ContestInterceptor.class})
    @Before(POST.class)
    @RequiresAuthentication
    @RequiresPermissions("problem:edit")
    public void updateProblem() {
        Integer cid = getParaToInt("cid");
        Integer num = getParaToInt("num");
        String title = getPara("title");
        ProblemModel problemModel = getModel(ProblemModel.class, "problem");
        contestService.updateProblem(problemModel, cid, num, title);

        String redirectURL = "/contest/problem/" + cid + "-" + (char) (num + 'A');
        redirect(redirectURL, new FlashMessage(getText("problem.update.success")));
    }

    @RequiresAuthentication
    @RequiresPermissions("contest:addProblem")
    @Before(ClarificationsInterceptor.class)
    public void admin() {
        Integer cid = getParaToInt(0);

        ContestModel contestModel = contestService.getContest(cid);

        long serverTime = OjConfig.timeStamp;
        int startTime = contestModel.getInt("startTime");
        int endTime = contestModel.getInt("endTime");
        String status = getText("contest.status.running");

        if (startTime > serverTime)
            status = getText("contest.status.pending");
        else if (endTime < serverTime)
            status = getText("contest.status.finished");

        setAttr("contest", contestModel);
        setAttr("contestProblems", contestService.getContestProblems(cid, null));
        setAttr("status", status);

        setTitle("Manage Contest " + cid);
    }

    @RequiresAuthentication
    @RequiresPermissions("contest:addUser")
    @Before(ClarificationsInterceptor.class)
    public void attendees() {
        Integer cid = getParaToInt(0);

        ContestModel contestModel = contestService.getContest(cid);
        if (contestModel.getType() < ContestModel.TYPE_PRIVATE) {
            redirect("/contest/show/" + cid, new ErrorFlashMessage("This is not a private contest!"));
            return;
        }

        long serverTime = OjConfig.timeStamp;
        int startTime = contestModel.getInt("startTime");
        int endTime = contestModel.getInt("endTime");
        String status = getText("contest.status.running");

        if (startTime > serverTime)
            status = getText("contest.status.pending");
        else if (endTime < serverTime)
            status = getText("contest.status.finished");

        setAttr("contest", contestModel);
        setAttr("contestUsers", contestService.getContestUsers(cid));
        setAttr("status", status);

        setTitle(getText("contest.attendees.title", cid));
    }

    @RequiresPermissions("contest:rejudge")
    @RequiresAuthentication
    public void rejudge() {
        Integer cid = getParaToInt(0);
        if (contestService.getContestStatus(cid) == ContestModel.PENDING) {
            redirect("/contest/show/" + cid,
                    new FlashMessage("This contest not start yet!", MessageType.ERROR, "Rejudge Contest Error"));
            return;
        }

        FlashMessage msg;

        if (judgeService.rejudgeContest(cid)) {
            msg = new FlashMessage("Server accept your request.");
        } else {
            msg =
                    new FlashMessage("Server reject your request since rejudge this contest is ongoing.", MessageType.ERROR,
                            "Rejudge Error");
        }

        redirect("/contest/show/" + cid, msg);
    }

    @RequiresPermissions("contest:rejudge")
    @RequiresAuthentication
    public void rejudgeProblem() {
        Integer cid = getParaToInt(0);
        char id = getPara(1, "A").toUpperCase().charAt(0);
        if (contestService.getContestStatus(cid) == ContestModel.PENDING) {
            redirect("/contest/problem/" + cid + "-" + id,
                    new FlashMessage("This contest not start yet!", MessageType.ERROR, "Rejudge Contest Error"));
            return;
        }

        Integer num = id - 'A';
        ProblemModel problemModel = contestService.getProblem(cid, num);

        if (problemModel == null) {
            FlashMessage msg =
                    new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
            redirect("/contest/show/" + cid, msg);
            return;
        }

        FlashMessage msg;

        if (judgeService.rejudgeContestProblem(cid, problemModel.getPid(), num)) {
            msg = new FlashMessage("Server accept your request.");
        } else {
            msg = new FlashMessage("Server reject your request since rejudge this contest or problem is ongoing.",
                    MessageType.ERROR, "Rejudge Error");
        }

        redirect("/contest/problem/" + cid + "-" + id, msg);
    }

    @RequiresPermissions("code:rejudge")
    @RequiresAuthentication
    public void rejudgeCode() {
        Integer cid = getParaToInt(0);
        Integer sid = getParaToInt(1);
        judgeService.rejudgeContestSolution(sid);

        redirect("/contest/code/" + cid + "-" + sid, new FlashMessage("Server got your rejudge request."));
    }

    @Clear({ContestInterceptor.class})
    @RequiresAuthentication
    @RequiresPermissions("contest:build")
    public void build() {
        Integer cid = getParaToInt(0);
        contestService.build(cid);

        redirect("/contest/rank/" + cid, new FlashMessage(getText("contest.buildRank.success")));
    }

    @RequiresAuthentication
    @RequiresPermissions("problem:edit")
    public void lockBoard() {
        Integer cid = getParaToInt(0);
        if (contestService.lockBoard(cid)) {
            redirect("/contest/admin/" + cid);
        }
    }

    @RequiresAuthentication
    @RequiresPermissions("problem:edit")
    public void unlockBoard() {
        Integer cid = getParaToInt(0);
        if (contestService.unlockBoard(cid)) {
            redirect("/contest/admin/" + cid);
        } else {
            redirect("/contest/admin/" + cid,
                    new FlashMessage("Cannot unlock board before contest finished!", MessageType.ERROR,
                            "Unlock Board Error"));
        }
    }

    @RequiresAuthentication
    @RequiresPermissions("problem:edit")
    public void lockReport() {
        Integer cid = getParaToInt(0);
        if (contestService.lockReport(cid)) {
            redirect("/contest/admin/" + cid);
        }
    }

    @RequiresAuthentication
    @RequiresPermissions("problem:edit")
    public void unlockReport() {
        Integer cid = getParaToInt(0);
        if (contestService.unlockReport(cid)) {
            redirect("/contest/admin/" + cid);
        } else {
            redirect("/contest/admin/" + cid,
                    new FlashMessage("Cannot unlock report before contest finished!", MessageType.ERROR,
                            "Unlock Report Error"));
        }
    }

}
