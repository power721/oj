package com.power.oj.contest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.admin.AdminInterceptor;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.ResultType;
import com.power.oj.problem.ProblemModel;
import com.power.oj.solution.SolutionModel;

public class ContestController extends OjController
{
	private static final Logger log = Logger.getLogger(ContestController.class);

	public void index()
	{
		int pageNumber = getParaToInt("p", 1);
		int pageSize = getParaToInt("s", 20);
		int type = getParaToInt("type", -1);
		int status = getParaToInt("status", -1);

		Page<ContestModel> contestList = ContestModel.dao.getPage(pageNumber, pageSize, type, status);
		setAttr("pageTitle", "Contest List");
		setAttr("contestList", contestList);

		render("index.html");
	}

	public void show()
	{
		int cid = getParaToInt(0);
		int uid = 0;
		if (getAttr("userID") != null)
			uid = getAttrForInt("userID");
		ContestModel contestModle = ContestModel.dao.getContest(cid);
		if (contestModle == null)
		{
			log.warn(new StringBand(2).append("Cannot find this contest: ").append(cid).toString());
			redirect("/contest", "Cannot find this contest!", "error", "Error!");
			return;
		}
		List<Record> contestProblems = ContestModel.dao.getContestProblems(cid, uid);

		long ctime = System.currentTimeMillis() / 1000;
		int start_time = contestModle.getInt("start_time");
		int end_time = contestModle.getInt("end_time");
		String status = "Running";

		if (start_time > ctime)
			status = "Pending";
		else if (end_time < ctime)
			status = "Finished";

		setAttr("pageTitle", new StringBand(2).append("Contest ").append(cid).toString());
		setAttr("cid", cid);
		setAttr("contest", contestModle);
		setAttr("contestProblems", contestProblems);
		setAttr("status", status);
		setAttr("ctime", ctime * 1000L);

		render("show.html");
	}

	public void problem()
	{
		int cid = getParaToInt(0);
		String problem_id = getPara(1);
		char id = problem_id.charAt(0);
		int num = id - 'A';

		ProblemModel problemModel = ContestModel.dao.getProblem(cid, num);
		if (problemModel == null)
		{
			log.warn(new StringBand(4).append("Cannot find this contest problem: ").append(cid).append("-").append(id).toString());
			redirect(new StringBand(2).append("/contest/show/").append(cid).toString(), "Cannot find this contest problem!", "error", "Error!");
			return;
		}

		problemModel.put("sample_input_rows", StringUtil.count(problemModel.getStr("sample_input"), '\n') + 1);
		problemModel.put("sample_output_rows", StringUtil.count(problemModel.getStr("sample_output"), '\n') + 1);

		setAttr("pageTitle", new StringBand(5).append(cid).append("-").append(id).append(": ").append(problemModel.getStr("title")).toString());
		setAttr("problem", problemModel);
		setAttr("cid", cid);
		setAttr("cstatus", ContestModel.dao.getContestStatus(cid));

		List<Record> contestProblems = ContestModel.dao.getContestProblems(cid, 0);
		setAttr("contestProblems", contestProblems);

		render("problem.html");
	}

	public void submit()
	{
		int cid = getParaToInt(0);
		String problem_id = getPara(1);
		char id = problem_id.charAt(0);
		int num = id - 'A';
		boolean ajax = getParaToBoolean("ajax", false);

		if (ContestModel.dao.isContestFinished(cid))
		{
			// log.info(new
			// StringBand(4).append("Cannot find this contest problem: ").append(cid).append("-").append(id).toString());
			redirect(new StringBand(2).append("/contest/show/").append(cid).toString(), "This contest has finished!", "warn", "Warnning!");
			return;
		}

		ProblemModel problemModel = ContestModel.dao.getProblem(cid, num);
		if (problemModel == null)
		{
			log.warn(new StringBand(4).append("Cannot find this contest problem: ").append(cid).append("-").append(id).toString());
			redirect(new StringBand(2).append("/contest/show/").append(cid).toString(), "Cannot find this contest problem!", "error", "Error!");
			return;
		}

		setAttr("pageTitle", new StringBand(6).append("Submit Problem ").append(cid).append("-").append(id).append(": ").append(problemModel.getStr("title"))
				.toString());
		setAttr("problem", problemModel);
		setAttr("user", getSessionAttr("user"));
		setAttr("program_languages", OjConstants.program_languages);
		setAttr("cid", cid);

		if (ajax)
			render("ajax/submit.html");
		else
			render("submit.html");
	}

	public void rank()
	{
		int cid = getParaToInt(0);
		int pageNumber = getParaToInt("p", 1);
		int pageSize = getParaToInt("s", 50);

		String sql = "FROM board b LEFT JOIN user u ON u.uid=b.uid WHERE b.cid=? ORDER BY accepts DESC,penalty";
		Page<Record> userRank = Db.paginate(pageNumber, pageSize, "SELECT b.*,u.name,u.nick,u.realname", sql, cid);
		setAttr("userRank", userRank);

		List<Record> contestProblems = Db.find("SELECT * FROM contest_problem WHERE cid=? ORDER BY num", cid);
		setAttr("Problems", contestProblems);
		setAttr("pageTitle", new StringBand(2).append("Contest Standing ").append(cid).toString());
		setAttr("cid", cid);
		setAttr("cstatus", ContestModel.dao.getContestStatus(cid));

		int problemCount = (int) ContestModel.dao.getProblemCount(cid);
		char problemIDs[] = new char[problemCount];
		for (int i = 0; i < problemCount; ++i)
		{
			problemIDs[i] = (char) (i + 'A');
		}
		setAttr("problemIDs", problemIDs);

		render("rank.html");
	}

	public void status()
	{
		int cid = getParaToInt(0);
		ContestModel contestModle = ContestModel.dao.findById(cid);
		setAttr("pageTitle", new StringBand(3).append("Contest ").append(cid).append(" Status").toString());
		setAttr("cid", cid);
		setAttr("contest", contestModle);

		int pageNumber = getParaToInt("p", 1);
		int pageSize = getParaToInt("s", 20);
		int result = getParaToInt("result", -1);
		int language = getParaToInt("language", -1);
		int num = -1;

		if (StringUtil.isNotBlank(getPara("id")))
		{
			try
			{
				num = getParaToInt("id");
			} catch (Exception e)
			{
				num = getPara("id").charAt(0) - 'A';
			}
		}
		String userName = getPara("name");
		StringBand query = new StringBand();

		if (result > -1)
		{
			query.append("&result=").append(result);
		}
		if (language > -1)
		{
			query.append("&language=").append(language);
		}
		if (num > -1)
		{
			query.append("&id=").append(getPara("id"));
		}
		if (StringUtil.isNotBlank(userName))
		{
			query.append("&name=").append(userName);
		}

		setAttr("pageTitle", "Status");
		setAttr("solutionList", SolutionModel.dao.getPageForContest(pageNumber, pageSize, result, language, cid, num, userName));
		setAttr("program_languages", OjConstants.program_languages);
		setAttr("judge_result", OjConstants.judge_result);
		setAttr("result", result);
		setAttr("language", language);
		setAttr("id", getPara("id"));
		setAttr("name", userName);
		setAttr("query", query.toString());

		render("status.html");
	}

	public void problem_status()
	{
		int cid = getParaToInt("cid");
		char id = getPara("id").charAt(0);
		int num = id - 'A';
		ProblemModel problemModel = ContestModel.dao.getProblem(cid, num);
		boolean ajax = getParaToBoolean("ajax", false);

		if (!ajax)
		{
			if (problemModel == null)
			{
				redirect(new StringBand(2).append("/contest/show/").append(cid).toString(), "Permission Denied.", "error", "Error!");
				return;
			}

			List<SolutionModel> resultList = SolutionModel.dao.find("SELECT result,COUNT(*) AS count FROM solution WHERE cid=? AND num=? GROUP BY result", cid,
					num);
			for (SolutionModel record : resultList)
			{
				ResultType resultType = (ResultType) OjConstants.result_type.get(record.getInt("result"));
				record.put("longName", resultType.getLongName());
				record.put("name", resultType.getName());
			}
			setAttr("resultList", resultList);
			setAttr("problem", problemModel);
		}

		int pageNumber = getParaToInt("p", 1);
		int pageSize = getParaToInt("s", 20);
		int language = getParaToInt("language", -1);
		StringBand query = new StringBand();
		if (language > -1)
		{
			query.append("&language=").append(language);
		}
		Page<SolutionModel> solutionList = SolutionModel.dao.getProblemStatusPageForContest(pageNumber, pageSize, language, cid, num);

		setAttr("pageTitle", new StringBand(5).append("Contest Problem ").append(cid).append("-").append(id).append(" Status").toString());
		setAttr("program_languages", OjConstants.program_languages);
		setAttr("solutionList", solutionList);
		setAttr("language", language);
		setAttr("query", query.toString());
		setAttr("cid", cid);
		setAttr("id", id);

		if (ajax)
			render("ajax/problem_status.html");
		else
			render("problem_status.html");
	}

	public void statistics()
	{
		int cid = getParaToInt(0);
		setAttr("pageTitle", new StringBand(3).append("Contest Statistics ").append(cid).toString());
		setAttr("cid", cid);
		List<Record> statistics = ContestModel.dao.getContestStatistics(cid);
		setAttr("statistics", statistics);

		List<String> resultName = new ArrayList<String>();
		for (ResultType resultType : OjConstants.judge_result)
		{
			if (resultType.getId() > 9)
				break;
			resultName.add(resultType.getName());
		}
		resultName.add("Others");
		setAttr("resultName", resultName);
		setAttr("languageList", OjConstants.program_languages);

		render("statistics.html");
	}

	public void recent_contest()
	{
		String json = getSessionAttr("contests.json");
		if (json == null)
		{
			List<ContestTask> contests = new ArrayList<ContestTask>();
			String html = getHtmlByUrl("http://acm.nankai.edu.cn/contests.json");
			if (html == null)
			{
				html = getHtmlByUrl("http://contests.acmicpc.info/contests.json");
			}
			if (html == null)
			{
				renderJson("{\"error\":network error}");
				return;
			}
			//System.out.println(html);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long timeStamp = 0;

			JSONArray jsonArray = JSON.parseArray(html);
			for (int i = 0; i < jsonArray.size(); ++i)
			{
				JSONObject data = jsonArray.getJSONObject(i);
				ContestTask contest = new ContestTask();
				try
				{
					timeStamp = sdf.parse(data.getString("start_time")).getTime();
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String start = "/Date(" + timeStamp + ")/";
				String end = "/Date(" + (timeStamp + 18000000) + ")/";
				String link = data.getString("link");
				String title = data.getString("name");

				contest.setTaskId(data.getString("id"));
				contest.setOj(data.getString("oj"));
				contest.setTitle(title);
				contest.setDescription(link);
				contest.setStart(start);
				contest.setEnd(end);

				contests.add(contest);
			}
			json = JsonKit.listToJson(contests, 2);
			setSessionAttr("contests.json", json);
		}

		renderJson(json);
	}

	public void recent()
	{
		setAttr("pageTitle", "Recent Contests on Other OJs");
	}

	@Before(AdminInterceptor.class)
	public void edit()
	{
		renderText("TODO");
	}

	@Before(AdminInterceptor.class)
	public void update()
	{
		renderText("TODO");
	}

	@Before(AdminInterceptor.class)
	public void add()
	{
		setAttr("pageTitle", "Create a contest");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long ctime = System.currentTimeMillis() + 3600000;
		setAttr("start_time", sdf.format(new Date(ctime)));
		setAttr("end_time", sdf.format(new Date(ctime + 18000000)));
		System.out.println(getAttr("end_time"));

		render("add.html");
	}

	@Before(
	{ AdminInterceptor.class, AddContestValidator.class })
	public void save()
	{
		String start_time = getPara("start_time");
		String end_time = getPara("end_time");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		ContestModel contestModel = getModel(ContestModel.class, "contest");
		contestModel.set("uid", getAttr("userID"));
		try
		{
			contestModel.set("start_time", sdf.parse(start_time).getTime() / 1000);
			contestModel.set("end_time", sdf.parse(end_time).getTime() / 1000);
			System.out.println(contestModel.get("end_time"));
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contestModel.saveContest();

		redirect(new StringBand(2).append("/contest/show/").append(contestModel.getInt("cid")).toString());
	}

	@Before(AdminInterceptor.class)
	public void buildRank()
	{
		int cid = getParaToInt(0);
		ContestModel.dao.buildRank(cid);

		redirect(new StringBand(2).append("/contest/rank/").append(cid).toString(), "The contest rank build success!");
	}

	public static String getHtmlByUrl(String url)
	{
		String html = null;

		HttpClient httpclient = new DefaultHttpClient();
		try
		{
			HttpGet httpget = new HttpGet(url);
			System.out.println("executing request " + httpget.getURI());

			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			html = httpclient.execute(httpget, responseHandler);

		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}

		return html;
	}
}
