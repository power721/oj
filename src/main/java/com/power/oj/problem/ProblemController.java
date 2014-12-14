package com.power.oj.problem;

import java.io.IOException;

import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;

public class ProblemController extends OjController {
	public void index() {
		int pageNumber = 1;
		if (isParaExists(0))
			pageNumber = getParaToInt(0, 1);
		else
			pageNumber = getCookieToInt("pageNumber", 1);
		int pageSize = getParaToInt(1, OjConfig.problemPageSize);

		setAttr("problemList", problemService.getProblemPage(pageNumber, pageSize));
		setAttr("pageSize", OjConfig.problemPageSize);
		setCookie("pageNumber", String.valueOf(pageNumber), OjConstants.COOKIE_AGE);

		setTitle(getText("problem.index.title"));
	}

	public void show() {
		Integer pid = getParaToInt(0);
		ProblemModel problemModel = problemService.findProblemForShow(pid);
		if (problemModel == null) {
			FlashMessage msg = new FlashMessage(getText("problem.show.null"), MessageType.ERROR, getText("message.error.title"));
			redirect("/problem", msg);
			return;
		}

		setAttr("prevPid", problemService.getPrevPid(pid));
		setAttr("nextPid", problemService.getNextPid(pid));
		setAttr("tagList", problemService.getTags(pid));
		setAttr("userResult", problemService.getUserResult(pid));
		setAttr("problem", problemModel);
		setAttr("spj", problemService.checkSpj(pid));
		setCookie("pageNumber", String.valueOf(problemService.getPageNumber(pid, OjConfig.problemPageSize)),
				OjConstants.COOKIE_AGE);

		setTitle(new StringBuilder(3).append(pid).append(": ").append(problemModel.getStr("title")).toString());
	}

	public void status() {
		Integer pid = getParaToInt(0);
		ProblemModel problemModel = problemService.findProblem(pid);
		if (problemModel == null) {
			FlashMessage msg = new FlashMessage(getText("problem.status.null"), MessageType.ERROR,
					getText("message.error.title"));
			redirect("/problem", msg);
			return;
		}

		setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);
		setAttr("pageSize", OjConfig.statusPageSize);
		setAttr("language", getParaToInt("language"));
		setAttr("problem", problemModel);
		setAttr("resultList", problemService.getProblemStatus(pid));
		setAttr("prevPid", problemService.getPrevPid(pid));
		setAttr("nextPid", problemService.getNextPid(pid));

		setTitle(new StringBuilder(2).append(String.format(getText("problem.status.title"), pid)).toString());
	}

	public void random() {
		Integer pid = problemService.getRandomPid();
		redirect(new StringBuilder(2).append("/problem/show/").append(pid).toString());
	}

	public void search() {
		Integer pid = 0;
		ProblemModel problemModel = null;

		try {
			pid = getParaToInt("word", 0);
		} catch (NumberFormatException e) {
			pid = 0;
		}
		if (pid != 0) {
			problemModel = problemService.findProblem(pid);
			if (problemModel == null)
				pid = 0;
		} else if (isParaBlank("word")) {
			pid = problemService.getRandomPid();
		}

		if (pid != 0) {
			redirect(new StringBuilder(2).append("/problem/show/").append(pid).toString());
			return;
		}

		int pageNumber = getParaToInt(0, 1);
		int pageSize = getParaToInt("s", OjConfig.problemPageSize);
		String word = HtmlEncoder.text(getPara("word").trim());
		String scope = getPara("scope");
		setAttr("problemList", problemService.searchProblem(pageNumber, pageSize, scope, word));
		setAttr("pageSize", OjConfig.problemPageSize);
		setAttr("word", word);
		setAttr("scope", scope != null ? scope : "all");

		setTitle(new StringBuilder(2).append(getText("problem.search.title")).append(word).toString());
	}

	@RequiresPermissions("problem:submit")
	public void submit() {
		Integer pid = getParaToInt(0);
		boolean ajax = getParaToBoolean("ajax", false);
		ProblemModel problemModel = problemService.findProblem(pid);

		if (problemModel == null) {
			FlashMessage msg = new FlashMessage(getText("problem.show.null"), MessageType.ERROR, getText("message.error.title"));
			redirect("/problem", msg);
			return;
		}

		setAttr("problem", problemModel);
		setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);

		if (isParaExists("s")) {
			setAttr("solution", problemService.getSolution(pid, getParaToInt("s", 0)));
		}

		setTitle(getText("problem.submit.title"));
		if (ajax)
			render("ajax/submit.html");
		else
			render("submit.html");
	}

	@Before(POST.class)
	@RequiresPermissions("problem:addTag")
	public void tag() {
		String op = getPara("op");
		String tag = HtmlEncoder.text(getPara("tag").trim());
		Integer pid = getParaToInt("pid");

		if ("add".equals(op) && StringUtil.isNotBlank(tag)) {
			problemService.addTag(pid, tag);
		}

		String redirectURL = new StringBuilder(3).append("/problem/show/").append(pid).append("#tag").toString();
		redirect(redirectURL, new FlashMessage(getText("problem.tag.success")));
	}

	/******************** admin methods ********************/
	@RequiresPermissions("problem:edit")
	public void edit() {
		if (!isParaExists(0)) {
			FlashMessage msg = new FlashMessage(getText("problem.para.null"), MessageType.ERROR, getText("message.error.title"));
			redirect("/problem", msg);
			return;
		}

		Integer pid = getParaToInt(0);
		boolean ajax = getParaToBoolean("ajax", false);

		setAttr("problem", problemService.findProblem(pid));
		setTitle(new StringBuilder(2).append(getText("problem.edit.title")).append(pid).toString());

		render(ajax ? "ajax/edit.html" : "edit.html");
	}

	@RequiresPermissions("problem:edit")
	public void update() {
		ProblemModel problemModel = getModel(ProblemModel.class, "problem");
		problemService.updateProblem(problemModel);

		String redirectURL = new StringBuilder(2).append("/problem/show/").append(problemModel.getPid()).toString();
		redirect(redirectURL, new FlashMessage(getText("problem.update.success")));
	}

	@RequiresPermissions("problem:add")
	public void add() {
		setTitle(getText("problem.add.title"));
	}

	@RequiresPermissions("problem:add")
	public void save() {
		ProblemModel problemModel = getModel(ProblemModel.class, "problem");
		String redirectURL = "/problem";

		try {
			if (!problemService.addProblem(problemModel)) {
				FlashMessage msg = new FlashMessage(getText("problem.save.warn"), MessageType.WARN,
						getText("message.warn.title"));
				setFlashMessage(msg);
			}
			redirectURL = new StringBuilder(2).append("/problem/show/").append(problemModel.getPid()).toString();
		} catch (IOException e) {
			if (OjConfig.isDevMode())
				e.printStackTrace();
			log.error(e.getMessage());

			FlashMessage msg = new FlashMessage(getText("problem.save.error"), MessageType.ERROR, getText("message.error.title"));
			redirect(redirectURL, msg);
			return;
		}

		redirect(redirectURL);
	}

	@RequiresPermissions("problem:delete")
	public void delete() {
		renderText("TODO");
	}

	@RequiresPermissions("problem:build")
	public void rejudge() {
		Integer pid = getParaToInt(0);
		String redirectURL = new StringBuilder(2).append("/problem/show/").append(pid).toString();
		FlashMessage msg = new FlashMessage("Server got your request.");

		judgeService.rejudgeProblem(pid);
		redirect(redirectURL, msg);
	}

	@RequiresPermissions("problem:build")
	public void rejudgeWait() {
		Integer pid = getParaToInt(0);
		String redirectURL = new StringBuilder(2).append("/problem/show/").append(pid).toString();
		FlashMessage msg = new FlashMessage("Server got your request.");

		judgeService.rejudgeProblem4Wait(pid);
		redirect(redirectURL, msg);
	}

	@RequiresPermissions("problem:build")
	public void build() {
		Integer pid = getParaToInt(0);
		String redirectURL = new StringBuilder(2).append("/problem/show/").append(pid).toString();
		FlashMessage msg = new FlashMessage(getText("problem.build.success"));

		try {
			if (!problemService.build(pid)) {
				log.error(new StringBuilder(3).append("Build problem ").append(pid).append(" statistics failed!").toString());
				msg = new FlashMessage(getText("problem.build.error"), MessageType.ERROR, getText("message.error.title"));
			}
		} catch (ProblemException e) {
			log.error(e.getLocalizedMessage());
			msg = new FlashMessage(getText("problem.show.null"), MessageType.ERROR, getText("message.error.title"));
		}

		redirect(redirectURL, msg);
	}

}
