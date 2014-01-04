package com.power.oj.contest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import jodd.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.service.SessionService;
import com.power.oj.problem.ProblemModel;
import com.power.oj.solution.SolutionModel;
import com.power.oj.util.CryptUtils;
import com.power.oj.util.Tool;

public class ContestController extends OjController
{
  private static final Logger log = Logger.getLogger(ContestController.class);

  public void index()
  {
    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.contestPageSize);
    int type = getParaToInt("type", -1);
    int status = getParaToInt("status", -1);

    Page<ContestModel> contestList = ContestModel.dao.getPage(pageNumber, pageSize, type, status);
    setAttr("contestList", contestList);

    setTitle(getText("contest.index.title"));
  }

  @Before(ContestPasswordInterceptor.class)
  public void show()
  {
    int cid = getParaToInt(0);
    int uid = 0;
    if (getAttrForInt(OjConstants.USER_ID) != null)
      uid = getAttrForInt(OjConstants.USER_ID);
    ContestModel contestModle = ContestModel.dao.getContest(cid);
    if (contestModle == null)
    {
      log.warn(new StringBuilder(2).append("Cannot find this contest: ").append(cid).toString());
      FlashMessage msg = new FlashMessage(getText("contest.show.null"), MessageType.ERROR, getText("message.error.title"));
      redirect("/contest", msg);
      return;
    }
    List<Record> contestProblems = ContestModel.dao.getContestProblems(cid, uid);

    long ctime = OjConfig.timeStamp;
    int start_time = contestModle.getInt("start_time");
    int end_time = contestModle.getInt("end_time");
    String status = getText("contest.status.running");

    if (start_time > ctime)
      status = getText("contest.status.pending");
    else if (end_time < ctime)
      status = getText("contest.status.finished");

    setAttr("cid", cid);
    setAttr("contest", contestModle);
    setAttr("contestProblems", contestProblems);
    setAttr("status", status);
    setAttr("ctime", ctime * 1000L);

    setTitle(new StringBuilder(2).append(getText("contest.show.title")).append(cid).toString());
  }

  @Before(ContestPasswordInterceptor.class)
  public void problem()
  {
    int cid = getParaToInt(0);
    String problem_id = getPara(1);
    char id = problem_id.charAt(0);
    int num = id - 'A';

    ProblemModel problemModel = ContestModel.dao.getProblem(cid, num);
    if (problemModel == null)
    {
      log.warn(new StringBuilder(4).append("Cannot find this contest problem: ").append(cid).append("-").append(id).toString());
      FlashMessage msg = new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
      redirect(new StringBuilder(2).append("/contest/show/").append(cid).toString(), msg);
      return;
    }

    problemModel.put("sample_input_rows", StringUtil.count(problemModel.getStr("sample_input"), '\n') + 1);
    problemModel.put("sample_output_rows", StringUtil.count(problemModel.getStr("sample_output"), '\n') + 1);

    setAttr("problem", problemModel);
    setAttr("cid", cid);
    setAttr("cstatus", ContestModel.dao.getContestStatus(cid));

    List<Record> contestProblems = ContestModel.dao.getContestProblems(cid, 0);
    setAttr("contestProblems", contestProblems);

    setTitle(new StringBuilder(5).append(cid).append("-").append(id).append(": ").append(problemModel.getStr("title")).toString());
  }

  @Before(ContestPasswordInterceptor.class)
  public void submit()
  {
    int cid = getParaToInt(0);
    String problem_id = getPara(1);
    char id = problem_id.charAt(0);
    int num = id - 'A';
    boolean ajax = getParaToBoolean("ajax", false);

    if (ContestModel.dao.isContestFinished(cid))
    {
      FlashMessage msg = new FlashMessage(getText("contest.submit.finished"), MessageType.WARN, getText("message.warn.title"));
      redirect(new StringBuilder(2).append("/contest/show/").append(cid).toString(), msg);
      return;
    }

    ProblemModel problemModel = ContestModel.dao.getProblem(cid, num);
    if (problemModel == null)
    {
      log.warn(new StringBuilder(4).append("Cannot find this contest problem: ").append(cid).append("-").append(id).toString());
      FlashMessage msg = new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
      redirect(new StringBuilder(2).append("/contest/show/").append(cid).toString(), msg);
      return;
    }

    setAttr("problem", problemModel);
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    setAttr("cid", cid);
    
    setTitle(new StringBuilder(6).append(getText("contest.problem.title")).append(cid).append("-").append(id).append(": ").append(problemModel.getStr("title")).toString());
    if (ajax)
      render("ajax/submit.html");
    else
      render("submit.html");
  }

  @Before(ContestPasswordInterceptor.class)
  public void rank()
  {
    int cid = getParaToInt(0);
    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.contestRankPageSize);
    
    setAttr("cid", cid);
    setAttr("contestRank", ContestModel.dao.getContestRank(pageNumber, pageSize, cid));
    setAttr("contestProblems", ContestModel.dao.getContestProblems(cid, 0));
    setAttr("cstatus", ContestModel.dao.getContestStatus(cid));

    setTitle(new StringBuilder(2).append(getText("contest.rank.title")).append(cid).toString());
  }

  @Before(ContestPasswordInterceptor.class)
  public void status()
  {
    int cid = getParaToInt(0);
    ContestModel contestModle = ContestModel.dao.findById(cid);
    
    setAttr("cid", cid);
    setAttr("contest", contestModle);

    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.statusPageSize);
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
    StringBuilder query = new StringBuilder();

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

    setAttr("solutionList", SolutionModel.dao.getPageForContest(pageNumber, pageSize, result, language, cid, num, userName));
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    setAttr(OjConstants.JUDGE_RESULT, OjConfig.judge_result);
    setAttr("result", result);
    setAttr("language", language);
    setAttr("id", getPara("id"));
    setAttr("name", userName);
    setAttr("query", query.toString());

    setTitle(new StringBuilder(2).append(getText("contest.status.title").replaceAll("_cid_", String.valueOf(cid))).toString());
  }

  @Before(ContestPasswordInterceptor.class)
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
        FlashMessage msg = new FlashMessage(getText("contest.problem.null"), MessageType.ERROR, getText("message.error.title"));
        redirect(new StringBuilder(2).append("/contest/show/").append(cid).toString(), msg);
        return;
      }

      List<SolutionModel> resultList = SolutionModel.dao.find("SELECT result,COUNT(*) AS count FROM contest_solution WHERE cid=? AND num=? GROUP BY result", cid, num);
      for (SolutionModel record : resultList)
      {
        ResultType resultType = (ResultType) OjConfig.result_type.get(record.getInt("result"));
        record.put("longName", resultType.getLongName());
        record.put("name", resultType.getName());
      }
      setAttr("resultList", resultList);
      setAttr("problem", problemModel);
    }

    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.statusPageSize);
    int language = getParaToInt("language", -1);
    StringBuilder query = new StringBuilder();
    if (language > -1)
    {
      query.append("&language=").append(language);
    }
    Page<SolutionModel> solutionList = SolutionModel.dao.getProblemStatusPageForContest(pageNumber, pageSize, language, cid, num);

    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    setAttr("solutionList", solutionList);
    setAttr("language", language);
    setAttr("query", query.toString());
    setAttr("cid", cid);
    setAttr("id", id);

    setTitle(new StringBuilder(2).append(getText("contest.status.title").replaceAll("_cid_", String.valueOf(cid)).replaceAll("_id_", String.valueOf(id))).toString());
    if (ajax)
      render("ajax/problem_status.html");
    else
      render("problem_status.html");
  }

  @Before(ContestPasswordInterceptor.class)
  public void statistics()
  {
    int cid = getParaToInt(0);
    
    setAttr("cid", cid);
    List<Record> statistics = ContestModel.dao.getContestStatistics(cid);
    setAttr("statistics", statistics);

    List<String> resultName = new ArrayList<String>();
    for (ResultType resultType : OjConfig.judge_result)
    {
      if (resultType.getId() > 9)
        break;
      resultName.add(resultType.getName());
    }
    resultName.add("Others");
    setAttr("resultName", resultName);
    setAttr("languageList", OjConfig.program_languages);

    setTitle(new StringBuilder(3).append(getText("contest.statistics.title")).append(cid).toString());
  }

  public void recent_contest()
  {
    String json = getSessionAttr("contests.json");
    if (json == null)
    {
      List<ContestkendoSchedulerTask> contests = new ArrayList<ContestkendoSchedulerTask>();
      String html = Tool.getHtmlByUrl("http://acm.nankai.edu.cn/contests.json");
      if (html == null)
      {
        html = Tool.getHtmlByUrl("http://contests.acmicpc.info/contests.json");
      }
      if (html == null)
      {
        renderJson("{\"error\":network error}");
        return;
      }

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      long timeStamp = 0;

      JSONArray jsonArray;
      try
      {
        jsonArray = JSON.parseArray(html);
      } catch (JSONException e)
      {
        html = Tool.getHtmlByUrl("http://contests.acmicpc.info/contests.json");
        jsonArray = JSON.parseArray(html);
      }

      for (int i = 0; i < jsonArray.size(); ++i)
      {
        JSONObject data = jsonArray.getJSONObject(i);
        ContestkendoSchedulerTask contest = new ContestkendoSchedulerTask();
        try
        {
          timeStamp = sdf.parse(data.getString("start_time")).getTime();
        } catch (ParseException e)
        {
          timeStamp = 0;
          log.warn(e.getLocalizedMessage());
        }
        String start = "/Date(" + timeStamp + ")/";
        String end = "/Date(" + (timeStamp + 18000000) + ")/";
        String link = data.getString("link");
        String title = data.getString("oj") + " -- " + data.getString("name");

        contest.setTaskId(data.getString("id"));
        contest.setOj(data.getString("oj"));
        contest.setTitle(title);
        contest.setUrl(link);
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
    setTitle(getText("contest.recent.title"));
  }

  @Before(POST.class)
  public void password()
  {
    int cid = getParaToInt("cid");
    String password = getPara("password");

    if (ContestModel.dao.checkContestPassword(cid, password))
    {
      String token_name = new StringBuilder("cid-").append(cid).toString();
      String token_token = CryptUtils.encrypt(password, token_name);
      log.info(token_token);
      setSessionAttr(token_name, token_token);
      redirect(SessionService.me().getLastAccessURL());
      return;
    }

    keepPara("cid");
    keepPara("title");
    
    FlashMessage msg = new FlashMessage(getText("contest.password.error"), MessageType.ERROR, getText("message.error.title"));
    redirect(SessionService.me().getLastAccessURL(), msg);
  }

  @RequiresPermissions("contest:edit")
  public void edit()
  {
    renderText("TODO");
  }

  @Before(POST.class)
  @RequiresPermissions("contest:edit")
  public void update()
  {
    renderText("TODO");
  }

  @RequiresPermissions("contest:add")
  public void add()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    long ctime = System.currentTimeMillis() + 3600000;
    setAttr("start_time", sdf.format(new Date(ctime)));
    setAttr("end_time", sdf.format(new Date(ctime + 18000000)));
    System.out.println(getAttr("end_time"));

    setTitle(getText("contest.add.title"));
  }

  @Before(POST.class)
  @RequiresPermissions("contest:add")
  public void save()
  {
    String start_time = getPara("start_time");
    String end_time = getPara("end_time");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    ContestModel contestModel = getModel(ContestModel.class, "contest");
    contestModel.set("uid", getAttr(OjConstants.USER_ID));
    try
    {
      contestModel.set("start_time", sdf.parse(start_time).getTime() / 1000);
      contestModel.set("end_time", sdf.parse(end_time).getTime() / 1000);
      System.out.println(contestModel.get("end_time"));
    } catch (ParseException e)
    {
      // TODO Auto-generated catch block
      // e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }
    contestModel.saveContest();

    redirect(new StringBuilder(2).append("/contest/show/").append(contestModel.getInt("cid")).toString());
  }

  @RequiresPermissions("contest:build")
  public void buildRank()
  {
    int cid = getParaToInt(0);
    ContestModel.dao.buildRank(cid);

    redirect(new StringBuilder(2).append("/contest/rank/").append(cid).toString(), new FlashMessage(getText("contest.buildRank.success")));
  }

}
