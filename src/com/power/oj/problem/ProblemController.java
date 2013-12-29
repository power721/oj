package com.power.oj.problem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import jodd.io.FileUtil;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.Message;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.bean.ResultType;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserService;

public class ProblemController extends OjController
{
  
  private static final UserService userService = UserService.me();
  
  public void index()
  {
    setTitle("Problem List");
    int pageNumber = 1;
    if (isParaExists("p"))
      pageNumber = getParaToInt("p", 1);
    else
      pageNumber = Integer.parseInt(getCookie("pageNumber", "1"));
    int pageSize = getParaToInt("s", OjConfig.problemPageSize);

    String sql = "SELECT pid,title,source,accept,submit,FROM_UNIXTIME(ctime, '%Y-%m-%d %H:%i:%s') AS ctime,status";
    StringBuilder sb = new StringBuilder("FROM problem");
    if (getAttr(OjConstants.ADMIN_USER) == null)
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
      Message msg = new Message("Please specify the problem ID.", MessageType.ERROR, "Error!");
      redirect("/problem", msg);
      return;
    }

    int pid = getParaToInt(0);
    boolean isAdmin = userService.isAdmin();
    ProblemModel problemModel = ProblemModel.dao.findByPid(pid, isAdmin);
    if (problemModel == null)
    {
      Message msg = new Message("Cannot find this problem!", MessageType.ERROR, "Error!");
      redirect("/problem", msg);
      return;
    }
    int uid = 0;
    if (getAttrForInt(OjConstants.USER_ID) != null)
    {
      uid = getAttrForInt(OjConstants.USER_ID);
      Record record = Db.findFirst("SELECT MIN(result) AS result FROM solution WHERE uid=? AND pid=? LIMIT 1", uid, pid);
      if (record != null)
      {
        setAttr("userResult", record.getInt("result"));
      }
    }

    int sample_input_rows = 1;
    if (StringUtil.isNotBlank(problemModel.getStr("sample_input")))
      sample_input_rows = StringUtil.count(problemModel.getStr("sample_input"), '\n') + 1;
    int sample_output_rows = 1;
    if (StringUtil.isNotBlank(problemModel.getStr("sample_output")))
      sample_output_rows = StringUtil.count(problemModel.getStr("sample_output"), '\n') + 1;
    problemModel.put("sample_input_rows", sample_input_rows);
    problemModel.put("sample_output_rows", sample_output_rows);

    setTitle(new StringBuilder(3).append(pid).append(": ").append(problemModel.getStr("title")).toString());
    setAttr("prevPid", ProblemModel.dao.getPrevPid(pid, isAdmin));
    setAttr("nextPid", ProblemModel.dao.getNextPid(pid, isAdmin));
    setAttr("tagList", ProblemModel.dao.getTags(pid));
    setAttr("problem", problemModel);
    setCookie("pageNumber", String.valueOf(ProblemModel.dao.getPageNumber(pid, OjConfig.problemPageSize)), 3600 * 24 * 7);

    problemModel.incView();
    render("show.html");
  }

  @RequiresPermissions("problem:submit")
  public void submit()
  {
    int pid = getParaToInt(0);
    boolean isAdmin = userService.isAdmin();
    ProblemModel problemModel = ProblemModel.dao.findByPid(pid, isAdmin);
    setAttr("problem", problemModel);
    setAttr(OjConstants.USER, userService.getCurrentUser());
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    boolean ajax = getParaToBoolean("ajax", false);
    int sid = 0;
    if (isParaExists("s"))
    {
      sid = getParaToInt("s", 0);
      StringBuilder sb = new StringBuilder("SELECT pid,uid,language,source FROM solution WHERE sid=? AND pid=?");
      if (!isAdmin)
        sb.append(" AND uid=").append(getAttrForInt(OjConstants.USER_ID));
      sb.append(" LIMIT 1");
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

  @RequiresPermissions("problem:edit")
  public void edit()
  {
    if (!isParaExists(0))
    {
      Message msg = new Message("Please specify the problem ID.", MessageType.ERROR, "Error!");
      redirect("/problem", msg);
      return;
    }

    int pid = getParaToInt(0);
    ProblemModel problemModel = ProblemModel.dao.findById(pid);
    setAttr("problem", problemModel);
    setTitle(new StringBuilder(2).append("Edit Problem ").append(pid).toString());
    boolean ajax = getParaToBoolean("ajax", false);

    if (ajax)
      render("ajax/edit.html");
    else
      render("edit.html");
  }

  @RequiresPermissions("problem:edit")
  public void update()
  {
    ProblemModel problemModel = getModel(ProblemModel.class, "problem");
    problemModel.updateProblem();

    String redirectURL = new StringBuilder(2).append("/problem/show/").append(problemModel.getInt("pid")).toString();
    redirect(redirectURL, new Message("The changes have been saved."));
  }

  @RequiresPermissions("problem:add")
  public void add()
  {
    setTitle("Add a problem");
    render("add.html");
  }

  @RequiresPermissions("problem:add")
  public void save()
  {
    ProblemModel problemModel = getModel(ProblemModel.class, "problem");
    problemModel.set("uid", getAttr(OjConstants.USER_ID));
    problemModel.saveProblem();

    String redirectURL = new StringBuilder(2).append("/problem/show/").append(problemModel.getInt("pid")).toString();
    File dataDir = new File(new StringBuilder(3).append(OjConfig.get("data_path")).append("\\").append(problemModel.getInt("pid")).toString());
    if (dataDir.isDirectory())
    {
      Message msg = new Message("The data directory already exists.", MessageType.WRAN, "Warning!");
      redirect(redirectURL, msg);
      return;
    }

    try
    {
      FileUtil.mkdirs(dataDir);
    } catch (IOException e)
    {
      log.error(e.getMessage());
      Message msg = new Message("The data directory cannot create.", MessageType.ERROR, "Error!");
      redirect(redirectURL, msg);
      return;
    }

    redirect(redirectURL);
  }

  @RequiresPermissions("problem:delete")
  public void delete()
  {
    renderText("TODO");
  }

  public void status()
  {
    if (!isParaExists(0))
    {
      forwardAction("/contest/problem_status");
      return;
    }

    int pid = getParaToInt(0);
    boolean ajax = getParaToBoolean("ajax", false);

    if (!ajax)
    {
      boolean isAdmin = userService.isAdmin();
      ProblemModel problemModel = ProblemModel.dao.findByPid(pid, isAdmin);
      if (problemModel == null)
      {
        Message msg = new Message("Permission Denied.", MessageType.ERROR, "Error!");
        redirect("/problem", msg);
        return;
      }

      List<SolutionModel> resultList = SolutionModel.dao.find("SELECT result,COUNT(*) AS count FROM solution WHERE pid=? GROUP BY result", pid);
      for (SolutionModel record : resultList)
      {
        try
        {
          ResultType resultType = (ResultType) OjConfig.result_type.get(record.getInt("result"));
          record.put("longName", resultType.getLongName());
          record.put("name", resultType.getName());
        } catch (NullPointerException e)
        {
          log.warn(e.getLocalizedMessage());
        }
      }
      setAttr("resultList", resultList);
      setAttr("problem", problemModel);
      setAttr("prevPid", ProblemModel.dao.getPrevPid(pid, isAdmin));
      setAttr("nextPid", ProblemModel.dao.getNextPid(pid, isAdmin));
    }

    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.statusPageSize);
    int language = getParaToInt("language", -1);
    StringBuilder query = new StringBuilder();
    if (language > -1)
    {
      query.append("&language=").append(language);
    }
    Page<SolutionModel> solutionList = SolutionModel.dao.getProblemStatusPage(pageNumber, pageSize, language, pid);

    setTitle(new StringBuilder(3).append("Problem ").append(pid).append(" Status").toString());
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    setAttr("language", language);
    setAttr("query", query.toString());
    setAttr("pid", pid);
    setAttr("solutionList", solutionList);

    if (ajax)
    {
      renderJson(new String[]
      { "pid", "language", "query", "program_languages", "solutionList" });
      // render("ajax/status.html");
    } else
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
      problemModel = ProblemModel.dao.findByPid(pid, getAttr(OjConstants.ADMIN_USER) != null);
      if (problemModel == null)
        pid = 0;
    } else if (isParaBlank("word"))
      pid = ProblemModel.dao.getRandomProblem();

    if (pid != 0)
    {
      redirect(new StringBuilder(2).append("/problem/show/").append(pid).toString());
      return;
    }

    String word = HtmlEncoder.text(getPara("word").trim());
    String scope = getPara("scope");
    setAttr("problemList", ProblemModel.dao.searchProblem(scope, word));
    setAttr("word", word);
    setAttr("scope", scope != null ? scope : "all");
    setTitle(new StringBuilder(2).append("Search problem: ").append(word).toString());

    render("search.html");
  }

  public void userInfo()
  {
    if (getAttrForInt(OjConstants.USER_ID) == null)
    {
      return;
    }

    int pid = getParaToInt("pid");
    int uid = getAttrForInt(OjConstants.USER_ID);
    List<Record> userInfo = null;

    if (uid > 0)
    {
      setAttr(OjConstants.LANGUAGE_NAME, OjConfig.language_name);
      setAttr(OjConstants.RESULT_TYPE, OjConfig.result_type);
      userInfo = ProblemModel.dao.getUserInfo(pid, uid);
      setAttr("userInfo", userInfo);
    }
    renderJson(new String[]
    { "userInfo", "language_name", "result_type" });
  }

  public void userResult()
  {
    if (getAttrForInt(OjConstants.USER_ID) == null)
    {
      return;
    }

    int pid = getParaToInt("pid");
    int uid = getAttrForInt(OjConstants.USER_ID);
    Record userResult = null;

    if (uid > 0)
    {
      userResult = ProblemModel.dao.getUserResult(pid, uid);
      if (userResult != null && userResult.getInt("result") != null)
        userResult.set("result", OjConfig.result_type.get(userResult.getInt("result")));
    }
    renderJson(userResult);
  }

  @Before(POST.class)
  @RequiresPermissions("problem:addTag")
  public void tag()
  {
    String op = getPara("op");
    String tag = HtmlEncoder.text(getPara("tag").trim());
    int pid = getParaToInt("pid");
    int uid = getAttrForInt(OjConstants.USER_ID);
    if ("add".equals(op) && StringUtil.isNotBlank(tag))
      ProblemModel.dao.addTag(pid, uid, tag);

    String redirectURL = new StringBuilder(3).append("/problem/show/").append(pid).append("#tag").toString();
    redirect(redirectURL, new Message("The changes have been saved."));
  }

  @RequiresPermissions("problem:build")
  public void build()
  {
    int pid = getParaToInt(0);
    String redirectURL = new StringBuilder(2).append("/problem/show/").append(pid).toString();
    ProblemModel problemModel = ProblemModel.dao.findById(pid);
    
    if (problemModel != null && !problemModel.build())
    {
      log.error(new StringBuilder(3).append("Build problem ").append(pid).append(" statistics failed!").toString());
      redirect(redirectURL, new Message("Build problem statistics failed!"));
    }

    redirect(redirectURL, new Message("The problem statistics have been updated."));
  }
}
