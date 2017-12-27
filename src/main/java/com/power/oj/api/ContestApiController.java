package com.power.oj.api;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Page;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.ResultType;
import com.power.oj.judge.JudgeService;
import com.power.oj.judge.RejudgeTask;
import com.power.oj.judge.RejudgeType;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;


@Before(CheckGuestInterceptor.class)
public class ContestApiController extends OjController {
    @Clear
    public void problemStatus() {
        Integer cid = getParaToInt(0);
        char id = getPara(1, "A").toUpperCase().charAt(0);
        Integer num = id - 'A';

        int pageNumber = getParaToInt("p", 1);
        int pageSize = getParaToInt("s", OjConfig.statusPageSize);
        Integer language = getParaToInt("language", 0);

        Page<ContestSolutionModel> solutionList =
            solutionService.getProblemStatusPageForContest(pageNumber, pageSize, language, cid, num);

        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);
        setAttr("solutionList", solutionList);
        setAttr("language", language);
        setAttr("user", userService.getCurrentUser());
        setAttr("adminUser", userService.isAdmin());

        renderJson(new String[] {"user", "adminUser", "solutionList", "language", "program_languages"});
    }

    @RequiresPermissions("contest:addProblem")
    @Before(POST.class)
    public void addProblem() {
        Integer cid = getParaToInt("cid");
        Integer pid = getParaToInt("pid");
        String title = getPara("title");
        Integer maxSim = getParaToInt("maxSim", 100);
        maxSim = Integer.max(0, maxSim);
        maxSim = Integer.min(100, maxSim);
        int result = contestService.addProblem(cid, pid, title, maxSim);

        renderJson("result", result);
    }

    @RequiresPermissions("contest:removeProblem")
    @Before(POST.class)
    public void removeProblem() {
        Integer cid = getParaToInt("cid");
        Integer pid = getParaToInt("pid");

        int result = contestService.removeProblem(cid, pid);

        if (result > 0) {
            renderJson("{\"success\":true}");
        } else if (result == -1) {
            renderJson("{\"success\":false, \"result\":\"Cannot delete problem.\"}");
        } else {
            renderJson("{\"success\":false, \"result\":\"Delete problem failed.\"}");
        }
    }

    @RequiresPermissions("contest:addProblem")
    @Before(POST.class)
    public void reorderProblem() {
        Integer cid = getParaToInt("cid");
        String pid = getPara("pid");

        if (contestService.reorderProblem(cid, pid) > 0) {
            renderJson("{\"success\":true}");
        } else {
            renderJson("{\"success\":false, \"result\":\"Unknown error!\"}");
        }
    }

    @RequiresPermissions("contest:addUser")
    @Before(POST.class)
    public void addUser() {
        Integer cid = getParaToInt("cid");
        Integer uid = getParaToInt("uid");
        // String name = getPara("name");
        int result = contestService.addUser(cid, uid);

        renderJson("result", result);
    }

    @RequiresPermissions("contest:addUser")
    @Before(POST.class)
    public void updateUser() {
        Integer cid = getParaToInt("cid");
        Integer uid = getParaToInt("uid");
        String nick = getPara("nick");
        boolean result = contestService.updateUser(cid, uid, nick);

        renderJson("result", result);
    }

    @RequiresPermissions("contest:addUser")
    @Before(POST.class)
    public void setSpecial() {
        Integer cid = getParaToInt("cid");
        Integer uid = getParaToInt("uid");
        Boolean special = getParaToBoolean("special");

        renderJson(contestService.setSpecial(cid, uid, special));
    }

    @RequiresPermissions("contest:addUser")
    @Before(POST.class)
    public void removeUser() {
        Integer cid = getParaToInt("cid");
        Integer uid = getParaToInt("uid");

        int result = contestService.removeUser(cid, uid);

        renderJson("result", result);
    }

    @Before(POST.class)
    public void postQuestion() {
        Integer cid = getParaToInt("cid");
        Integer num = getParaToInt("num");
        String question = HtmlEncoder.text(getPara("question"));

        renderJson("success", contestService.addClarify(cid, num, question));
    }

    @RequiresPermissions("contest:edit")
    @Before(POST.class)
    public void postClarification() {
        Integer cid = getParaToInt("cid");
        Integer num = getParaToInt("num");
        String clarification = HtmlEncoder.text(getPara("clarification"));

        renderJson("success", contestService.addClarification(cid, num, clarification));
    }

    @RequiresPermissions("contest:edit")
    @Before(POST.class)
    public void updateClarify() {
        // Integer cid = getParaToInt("cid");
        Integer id = getParaToInt("id");
        boolean isPublic = getParaToBoolean("ispublic");
        String reply = getPara("reply");

        renderJson("success", contestService.updateClarify(id, reply, isPublic));
    }

    public void code() {
        // TODO check permission
        Integer sid = getParaToInt("sid");
        boolean isAdmin = userService.isAdmin();
        ContestSolutionModel solutionModel = solutionService.findContestSolution4Json(sid);

        if (solutionModel == null) {
            renderJson("{\"success\":false,\"result\":\"Cannot find code.\"}");
            return;
        }
        ResultType resultType = OjConfig.resultType.get(solutionModel.getResult());
        Integer cid = solutionModel.getCid();
        Integer uid = solutionModel.getUid();
        Integer loginUid = userService.getCurrentUid();

        if (!uid.equals(loginUid) && !isAdmin) {
            renderJson("{\"success\":false,\"result\":\"Permission denied.\"}");
            return;
        }

        if (!isAdmin) {
            String error = solutionModel.getError();
            if (error != null) {
                solutionModel
                    .setError(error.replace(StringUtil.replace(OjConfig.getString("workPath"), "\\", "\\\\"), ""));
                // TODO replace "/"
            }
        }
        solutionModel.setSource(HtmlEncoder.text(solutionModel.getSource()));

        int num = solutionModel.getNum();
        setAttr("success", true);
        setAttr("language", OjConfig.languageName.get(solutionModel.getLanguage()));
        setAttr("alpha", (char) (num + 'A'));
        setAttr("problemTitle", contestService.getProblemTitle(cid, num));
        setAttr("resultLongName", resultType.getLongName());
        setAttr("resultName", resultType.getName());
        setAttr("solution", solutionModel);

        String brush = getAttrForStr("language").toLowerCase();
        if(brush.contains("gcc") || brush.contains("g++"))
            brush = "cc";
        if(brush.contains("java") || brush.contains("kotlin"))
            brush = "java";
        if (brush.contains("python"))
            brush = "py";
        setAttr("brush", brush);

        renderJson(
            new String[] {"success", "alpha", "problemTitle", "language", "resultLongName", "resultName", "solution",
                "brush"});
    }

    @Clear
    public void recent() {
        renderJson(contestService.getRecentContest());
    }

    public void evictRecent() {
        contestService.evictRecentContest();
        renderNull();
    }

    @Clear
    public void getResult() {
        Integer cid = getParaToInt("cid", 0);
        Integer sid = getParaToInt("sid", 0);

        renderJson(solutionService.getContestSolutionResult(cid, sid));
    }

    @Clear
    public void rejudgeStatus() {
        Integer cid = getParaToInt(0);
        Integer id = getParaToInt(1);
        RejudgeTask rejudgeTask;

        if (id == null) {
            rejudgeTask = JudgeService.me().getRejudgeTask(RejudgeType.CONTEST.getKey(cid));
        } else {
            rejudgeTask = JudgeService.me().getRejudgeTask(RejudgeType.CONTEST_PROBLEM.getKey(cid, id));
        }

        if (rejudgeTask == null) {
            renderJson("{\"count\": 1, \"total\": 0}");
        } else {
            renderJson(rejudgeTask);
        }
    }

}
