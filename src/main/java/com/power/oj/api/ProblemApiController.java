package com.power.oj.api;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.ResultType;
import com.power.oj.user.UserService;

@Before(CheckGuestInterceptor.class)
public class ProblemApiController extends OjController {
	@ClearInterceptor(ClearLayer.ALL)
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

		renderJson(new String[] { "userInfo", "language_name", "result_type" });
	}

	@ClearInterceptor(ClearLayer.ALL)
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
			renderJson(new String[] { "userResult", "acType" });
			return;
		}

		Record userResult = problemService.getUserResult(pid, uid);
		if (userResult != null && userResult.getInt("result") != null) {
			userResult.set("result", OjConfig.resultType.get(userResult.getInt("result")));
		}

		renderJson(userResult);
	}

	@ClearInterceptor(ClearLayer.ALL)
	public void status() {
		/*
		 * if (!isParaExists(0)) { forwardAction("/contest/problem_status");
		 * return; }
		 */

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
		renderJson(new String[] { "user", "adminUser", "pageSize", "language", "program_languages", "solutionList" });
	}

	@ClearInterceptor(ClearLayer.ALL)
	public void getField() {
		Integer pid = getParaToInt("pid");
		String name = getPara("name");

		renderJson("result", problemService.getProblemField(pid, name));
	}

	@ClearInterceptor(ClearLayer.ALL)
	public void getResult() {
		Integer sid = getParaToInt("sid", 0);

		renderJson(solutionService.getSolutionResult(sid));
	}

}
