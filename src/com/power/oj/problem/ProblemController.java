package com.power.oj.problem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jodd.io.FileUtil;
import jodd.util.HtmlEncoder;
import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.admin.AdminInterceptor;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.ResultType;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.LoginInterceptor;

public class ProblemController extends OjController
{
	private static final Logger log = Logger.getLogger(ProblemController.class);
	
	public void index()
	{
		setAttr("pageTitle", "Problem List");
		int pageNumber = 1;
		if (isParaExists("p"))
			pageNumber = getParaToInt("p", 1);
		else
			pageNumber = Integer.parseInt(getCookie("pageNumber", "1"));
		int pageSize = getParaToInt("s", 50);

		String sql = "SELECT pid,title,source,accept,submit,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime,status";
		StringBand sb = new StringBand("FROM problem");
		if (getAttr("adminUser") == null)
			sb.append(" WHERE status=1");
		sb.append(" ORDER BY pid");

		Page<ProblemModel> problemList = ProblemModel.dao.paginate(pageNumber, pageSize, sql, sb.toString());
		/*
		 * if(getAttr("userID") != null) { int uid = getAttrForInt("userID");
		 * for(ProblemModel problemModel: problemList.getList()) { Record record
		 * =Db.findFirst(
		 * "SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? LIMIT 1"
		 * , uid, problemModel.getInt("pid")); if(record != null) {
		 * problemModel.put("userResult", record.getInt("result")); } } }
		 */
		setAttr("problemList", problemList);
		setCookie("pageNumber", String.valueOf(pageNumber), 3600 * 24 * 7);

		render("index.html");
	}

	public void show()
	{
		if (!isParaExists(0))
		{
			redirect("/problem", "Please specify the problem ID.", "error", "Error!");
			return;
		}

		int pid = getParaToInt(0);
		boolean isAdmin = getAttr("adminUser") != null;
		ProblemModel problemModel = ProblemModel.dao.findByPid(pid, isAdmin);
		if (problemModel == null)
		{
			redirect("/problem", "Permission Denied.", "error", "Error!");
			return;
		}
		int uid = 0;
		if (getAttr("userID") != null)
		{
			uid = getAttrForInt("userID");
			Record record = Db.findFirst("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? LIMIT 1",
					uid, pid);
			if (record != null)
			{
				setAttr("userResult", record.getInt("result"));
			}
		}
		
		int sample_input_rows = 1;
		if(StringUtil.isNotBlank(problemModel.getStr("sample_input")))
			sample_input_rows = StringUtil.count(problemModel.getStr("sample_input"), '\n')+1;
		int sample_output_rows = 1;
		if(StringUtil.isNotBlank(problemModel.getStr("sample_output")))
			sample_output_rows = StringUtil.count(problemModel.getStr("sample_output"), '\n')+1;
		problemModel.put("sample_input_rows", sample_input_rows);
		problemModel.put("sample_output_rows", sample_output_rows);

		setAttr("pageTitle", new StringBand(3).append(pid).append(": ").append(problemModel.getStr("title")).toString());
		setAttr("prevPid", ProblemModel.dao.getPrevPid(pid, isAdmin));
		setAttr("nextPid", ProblemModel.dao.getNextPid(pid, isAdmin));
		setAttr("tagList", ProblemModel.dao.getTags(pid));
		setAttr("problem", problemModel);
		setCookie("pageNumber", String.valueOf(ProblemModel.dao.getPageNumber(pid, OjConfig.problemPageSize)),
				3600 * 24 * 7);

		problemModel.incView();
		render("show.html");
	}

	@Before(LoginInterceptor.class)
	public void submit()
	{
		int pid = getParaToInt(0);
		boolean isAdmin = getAttr("adminUser") != null;
		ProblemModel problemModel = ProblemModel.dao.findByPid(pid, isAdmin);
		setAttr("problem", problemModel);
		setAttr("user", getSessionAttr("user"));
		setAttr("program_languages", OjConfig.program_languages);
		boolean ajax = getParaToBoolean("ajax", false);
		int sid = 0;
		if (isParaExists("s"))
		{
			sid = getParaToInt("s", 0);
			StringBand sb = new StringBand("SELECT pid,uid,language,source FROM solution WHERE sid=? AND pid=?");
			if (!isAdmin)
				sb.append(" AND uid=").append(getAttrForInt("userID"));
			SolutionModel solutionModel = SolutionModel.dao.findFirst(sb.toString(), sid, pid);
			if (solutionModel != null)
			{
				setAttr("solution", solutionModel);
			}
		}

		if (ajax)
			render("ajax/submit.html");
		else
			render("submit.html");
	}

	@Before(AdminInterceptor.class)
	public void edit()
	{
		if (!isParaExists(0))
		{
			redirect("/problem", "Please specify the problem ID.", "error", "Error!");
			return;
		}

		int pid = getParaToInt(0);
		ProblemModel problemModel = ProblemModel.dao.findById(pid);
		setAttr("problem", problemModel);
		setAttr("pageTitle", new StringBand(2).append("Edit Problem ").append(pid).toString());
		boolean ajax = getParaToBoolean("ajax", false);

		if (ajax)
			render("ajax/edit.html");
		else
			render("edit.html");
	}

	@Before(AdminInterceptor.class)
	public void update()
	{
		ProblemModel problemModel = getModel(ProblemModel.class, "problem");
		problemModel.updateProblem();

		redirect(new StringBand(2).append("/problem/show/").append(problemModel.getInt("pid")).toString(),
				"The changes have been saved.");
	}

	@Before(AdminInterceptor.class)
	public void add()
	{
		setAttr("pageTitle", "Add a problem");
		render("add.html");
	}

	@Before(AdminInterceptor.class)
	public void save()
	{
		ProblemModel problemModel = getModel(ProblemModel.class, "problem");
		problemModel.set("uid", getAttr("userID"));
		problemModel.saveProblem();

		File dataDir = new File(new StringBand(3).append(OjConfig.get("data_path")).append("\\").append(
				problemModel.getInt("pid")).toString());
		if (dataDir.isDirectory())
		{
			redirect(new StringBand(2).append("/problem/show/").append(problemModel.getInt("pid")).toString(),
					"The data directory already exists.", "warning", "Warning!");
			return;
		}
		try
		{
			FileUtil.mkdirs(dataDir);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}

		redirect(new StringBand(2).append("/problem/show/").append(problemModel.getInt("pid")).toString());
	}

	@Before(AdminInterceptor.class)
	public void delete()
	{
		renderText("TODO");
	}

	public void status()
	{
		if(!isParaExists(0))
		{
			forwardAction("/contest/problem_status");
			return;
		}
		
		int pid = getParaToInt(0);
		boolean ajax = getParaToBoolean("ajax", false);

		if (!ajax)
		{
			boolean isAdmin = getAttr("adminUser") != null;
			ProblemModel problemModel = ProblemModel.dao.findByPid(pid, isAdmin);
			if (problemModel == null)
			{
				redirect("/problem", "Permission Denied.", "error", "Error!");
				return;
			}

			List<SolutionModel> resultList = SolutionModel.dao.find(
					"SELECT result,COUNT(*) AS count FROM solution WHERE pid=? GROUP BY result", pid);
			for (SolutionModel record : resultList)
			{
				ResultType resultType = (ResultType) OjConfig.result_type.get(record.getInt("result"));
				record.put("longName", resultType.getLongName());
				record.put("name", resultType.getName());
			}
			setAttr("resultList", resultList);
			setAttr("problem", problemModel);
			setAttr("prevPid", ProblemModel.dao.getPrevPid(pid, isAdmin));
			setAttr("nextPid", ProblemModel.dao.getNextPid(pid, isAdmin));
		}

		int pageNumber = getParaToInt("p", 1);
		int pageSize = getParaToInt("s", 20);
		int language = getParaToInt("language", -1);
		StringBand query = new StringBand();
		if (language > -1)
		{
			query.append("&language=").append(language);
		}
		Page<SolutionModel> solutionList = SolutionModel.dao.getProblemStatusPage(pageNumber, pageSize, language, pid);

		setAttr("pageTitle", new StringBand(3).append("Problem ").append(pid).append(" Status").toString());
		setAttr("program_languages", OjConfig.program_languages);
		setAttr("language", language);
		setAttr("query", query.toString());
		setAttr("pid", pid);
		setAttr("solutionList", solutionList);

		if (ajax)
		{
			renderJson(new String[]{"pid", "language", "query", "program_languages", "solutionList"});
			//render("ajax/status.html");
		}
		else
			render("status.html");
	}

	public void search()
	{
		int pid = 0;
		ProblemModel problemModel = null;
		try
		{
			pid = getParaToInt("word", 0);
		} catch (NumberFormatException e)
		{
			pid = 0;
		}
		if (pid != 0)
		{
			problemModel = ProblemModel.dao.findByPid(pid, getAttr("adminUser") != null);
			if (problemModel == null)
				pid = 0;
		} else if (isParaBlank("word"))
			pid = ProblemModel.dao.getRandomProblem();

		if (pid != 0)
		{
			redirect(new StringBand(2).append("/problem/show/").append(pid).toString());
			return;
		}

		String word = HtmlEncoder.text(getPara("word").trim());
		String scope = getPara("scope");
		setAttr("problemList", ProblemModel.dao.searchProblem(scope, word));
		setAttr("word", word);
		setAttr("scope", scope != null ? scope : "all");
		setAttr("pageTitle", new StringBand(2).append("Search problem: ").append(word).toString());

		render("search.html");
	}
	
	public void userInfo()
	{
		int pid = getParaToInt("pid");
		int uid = getAttrForInt("userID");
		List<Record> userInfo = null;
		
		if (uid > 0)
		{
			setAttr("language_name", OjConfig.language_name);
			setAttr("result_type", OjConfig.result_type);
			userInfo = ProblemModel.dao.getUserInfo(pid, uid);
			setAttr("userInfo", userInfo);
		}
		renderJson(new String[]{"userInfo", "language_name", "result_type"});
	}
	
	public void userResult()
	{
		int pid = getParaToInt("pid");
		int uid = getAttrForInt("userID");
		Record userResult = null;
		
		if (uid > 0)
		{
			userResult = ProblemModel.dao.getUserResult(pid, uid);
			if (userResult != null && userResult.getInt("result") != null)
				userResult.set("result", OjConfig.result_type.get(userResult.getInt("result")));
		}
		renderJson(userResult);
	}

	public void tag()
	{
		String op = getPara("op");
		String tag = HtmlEncoder.text(getPara("tag").trim());
		int pid = getParaToInt("pid");
		int uid = getAttrForInt("userID");
		if ("add".equals(op) && StringUtil.isNotBlank(tag))
			ProblemModel.dao.addTag(pid, uid, tag);

		redirect(new StringBand(3).append("/problem/show/").append(pid).append("#tag").toString(),
				"The changes have been saved.");
	}

	@Before(AdminInterceptor.class)
	public void build()
	{
		int pid = getParaToInt(0);
		ProblemModel problemModel = ProblemModel.dao.findById(pid);
		if (problemModel != null && !problemModel.build())
			System.out.println("No!");

		redirect(new StringBand(2).append("/problem/show/").append(pid).toString(),
				"The problem statistics have been updated.");
	}
}
