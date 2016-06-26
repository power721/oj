package com.power.oj.api;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.ResultType;
import com.power.oj.judge.JudgeService;
import com.power.oj.judge.RejudgeTask;
import com.power.oj.judge.RejudgeType;
import com.power.oj.user.UserService;

import java.util.List;

@Before(CheckGuestInterceptor.class)
public class ProblemApiController extends OjController {
    @Clear
    public void userInfo() {
        Integer pid = getParaToInt("pid");
        Integer uid = UserService.me().getCurrentUid();

        if (uid == null) {
            renderNull();
            return;
        }

        setAttr(OjConstants.LANGUAGE_NAME, OjConfig.languageName);
        setAttr(OjConstants.RESULT_TYPE, OjConfig.resultType);
        setAttr("userInfo", problemService.getUserInfo(pid, uid));

        renderJson(new String[] {"userInfo", "language_name", "result_type"});
    }

    @Clear
    public void userResult() {
        Integer pid = getParaToInt("pid");
        Integer uid = UserService.me().getCurrentUid();

        if (uid == null) {
            renderNull();
            return;
        }

        if (pid == null) {
            List<Record> userResult = problemService.getUserProblemResult(uid);
            setAttr("userResult", userResult);
            setAttr("acType", ResultType.AC);
            renderJson(new String[] {"userResult", "acType"});
            return;
        }

        Record userResult = problemService.getUserResult(pid, uid);
        if (userResult != null && userResult.getInt("result") != null) {
            userResult.set("result", OjConfig.resultType.get(userResult.getInt("result")));
        }

        renderJson(userResult);
    }

    @Clear
    public void status() {
    /*if (!isParaExists(0))
    {
      forwardAction("/contest/problem_status");
      return;
    }*/

        Integer pid = getParaToInt(0);
        int pageNumber = getParaToInt("p", 1);
        int pageSize = getParaToInt("s", OjConfig.statusPageSize);
        Integer language = getParaToInt("language", 0);

        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);
        setAttr("pageSize", OjConfig.statusPageSize);
        setAttr("language", language);
        setAttr("user", userService.getCurrentUser());
        setAttr("adminUser", userService.isAdmin());

        setAttr("solutionList", solutionService.getProblemStatusPage(pageNumber, pageSize, language, pid));
        renderJson(new String[] {"user", "adminUser", "pageSize", "language", "program_languages", "solutionList"});
    }

    @Clear
    public void getField() {
        Integer pid = getParaToInt("pid");
        String name = getPara("name");

        renderJson("result", problemService.getProblemField(pid, name));
    }

    @Clear
    public void getResult() {
        Integer sid = getParaToInt("sid", 0);

        renderJson(solutionService.getSolutionResult(sid));
    }

    @Clear
    public void rejudgeStatus() {
        Integer pid = getParaToInt(0);
        RejudgeTask rejudgeTask = JudgeService.me().getRejudgeTask(RejudgeType.PROBLEM.getKey(pid));
        if (rejudgeTask == null) {
            renderJson("{\"count\": 1, \"total\": 0}");
        } else {
            renderJson(rejudgeTask);
        }
    }

}
