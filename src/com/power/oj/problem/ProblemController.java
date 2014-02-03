package com.power.oj.problem;

import java.io.IOException;
import java.util.List;

import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.service.VisitCountService;
import com.power.oj.user.UserService;

public class ProblemController extends OjController
{
  
  private static final ProblemService problemService = ProblemService.me();
  
  public void index()
  {
    int pageNumber = 1;
    if (isParaExists("p"))
      pageNumber = getParaToInt("p", 1);
    else
      pageNumber = getCookieToInt("pageNumber", 1);
    int pageSize = getParaToInt("s", OjConfig.problemPageSize);

    setAttr("pageSize", OjConfig.problemPageSize);
    setAttr("problemList", problemService.getProblems(pageNumber, pageSize));
    setCookie("pageNumber", String.valueOf(pageNumber), OjConstants.COOKIE_AGE);

    setTitle(getText("problem.index.title"));
  }

  public void show()
  {
    Integer pid = getParaToInt(0);
    ProblemModel problemModel = problemService.findProblem(pid);
    if (problemModel == null)
    {
      FlashMessage msg = new FlashMessage(getText("problem.show.null"), MessageType.ERROR, getText("message.error.title"));
      redirect("/problem", msg);
      return;
    }
    
    int sample_input_rows = 1;
    if (StringUtil.isNotBlank(problemModel.getStr("sample_input")))
      sample_input_rows = StringUtil.count(problemModel.getStr("sample_input"), '\n') + 1;
    int sample_output_rows = 1;
    if (StringUtil.isNotBlank(problemModel.getStr("sample_output")))
      sample_output_rows = StringUtil.count(problemModel.getStr("sample_output"), '\n') + 1;
    problemModel.put("sample_input_rows", sample_input_rows);
    problemModel.put("sample_output_rows", sample_output_rows);

    setAttr("prevPid", problemService.getPrevPid(pid));
    setAttr("nextPid", problemService.getNextPid(pid));
    setAttr("tagList", problemService.getTags(pid));
    setAttr("userResult", problemService.getUserResult(pid));
    setAttr("problem", problemModel);
    setCookie("pageNumber", String.valueOf(ProblemModel.dao.getPageNumber(pid, OjConfig.problemPageSize)), OjConstants.COOKIE_AGE);

    //problemModel.incView();
    VisitCountService.record(VisitCountService.problemViewCount, pid);
    
    setTitle(new StringBuilder(3).append(pid).append(": ").append(problemModel.getStr("title")).toString());
  }

  @RequiresPermissions("problem:submit")
  public void submit()
  {
    Integer pid = getParaToInt(0);
    boolean ajax = getParaToBoolean("ajax", false);
    ProblemModel problemModel = problemService.findProblem(pid);
    
    if (problemModel == null)
    {
      FlashMessage msg = new FlashMessage(getText("problem.show.null"), MessageType.ERROR, getText("message.error.title"));
      redirect("/problem", msg);
      return;
    }
    
    setAttr("problem", problemModel);
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    
    if (isParaExists("s"))
    {
      setAttr("solution", problemService.getSolution(pid, getParaToInt("s", 0)));
    }

    setTitle(getText("problem.submit.title"));
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
      FlashMessage msg = new FlashMessage(getText("problem.para.null"), MessageType.ERROR, getText("message.error.title"));
      redirect("/problem", msg);
      return;
    }

    Integer pid = getParaToInt(0);
    boolean ajax = getParaToBoolean("ajax", false);
    
    setAttr("problem", problemService.findProblem(pid));
    setTitle(new StringBuilder(2).append(getText("problem.edit.title")).append(pid).toString());

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
    redirect(redirectURL, new FlashMessage(getText("problem.update.success")));
  }

  @RequiresPermissions("problem:add")
  public void add()
  {
    setTitle(getText("problem.add.title"));
  }

  @RequiresPermissions("problem:add")
  public void save()
  {
    ProblemModel problemModel = getModel(ProblemModel.class, "problem");
    String redirectURL = "/problem";
    
    try
    {
      if (!problemService.addProblem(problemModel))
      {
        FlashMessage msg = new FlashMessage(getText("problem.save.warn"), MessageType.WARN, getText("message.warn.title"));
        setFlashMessage(msg);
      }
      redirectURL = new StringBuilder(2).append("/problem/show/").append(problemModel.getInt("pid")).toString();
    } catch (IOException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getMessage());
      
      FlashMessage msg = new FlashMessage(getText("problem.save.error"), MessageType.ERROR, getText("message.error.title"));
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

    Integer pid = getParaToInt(0);
    boolean ajax = getParaToBoolean("ajax", false);

    if (!ajax)
    {
      ProblemModel problemModel = problemService.findProblem(pid);
      if (problemModel == null)
      {
        FlashMessage msg = new FlashMessage(getText("problem.status.null"), MessageType.ERROR, getText("message.error.title"));
        redirect("/problem", msg);
        return;
      }

      setAttr("problem", problemModel);
      setAttr("resultList", problemService.getProblemStatus(pid));
      setAttr("prevPid", problemService.getPrevPid(pid));
      setAttr("nextPid", problemService.getNextPid(pid));
    }

    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.statusPageSize);
    Integer language = getParaToInt("language");
    StringBuilder query = new StringBuilder();
    if (language != null)
    {
      query.append("&language=").append(language);
    }
    
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.language_name);
    setAttr("pageSize", OjConfig.statusPageSize);
    setAttr("language", language);
    setAttr("pid", pid);
    
    if (ajax)
    {
      setAttr("query", query.toString());
      setAttr("solutionList", problemService.getProblemStatusPage(pageNumber, pageSize, language, pid));
      renderJson(new String[]
      { "pid", "userID", "adminUser", "pageSize", "language", "query", "program_languages", "solutionList" });
      // render("ajax/status.html");
    } else
    {
      setTitle(new StringBuilder(2).append(String.format(getText("problem.status.title"), pid)).toString());
    }
  }

  public void search()
  {
    Integer pid = 0;
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
      problemModel = problemService.findProblem(pid);
      if (problemModel == null)
        pid = 0;
    } else if (isParaBlank("word"))
    {
      pid = problemService.getRandomPid();
    }

    if (pid != 0)
    {
      redirect(new StringBuilder(2).append("/problem/show/").append(pid).toString());
      return;
    }

    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", OjConfig.problemPageSize);
    String word = HtmlEncoder.text(getPara("word").trim());
    String scope = getPara("scope");
    setAttr("problemList", problemService.searchProblem(pageNumber, pageSize, scope, word));
    setAttr("pageSize", OjConfig.problemPageSize);
    setAttr("word", word);
    setAttr("scope", scope != null ? scope : "all");
    
    setTitle(new StringBuilder(2).append(getText("problem.search.title")).append(word).toString());
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

  @ClearInterceptor(ClearLayer.ALL)
  public void userResult()
  {
    if (UserService.me().getCurrentUid() == null)
    {
      renderNull();
      return;
    }

    int pid = getParaToInt("pid");
    int uid = UserService.me().getCurrentUid();
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
    redirect(redirectURL, new FlashMessage(getText("problem.tag.success")));
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
      FlashMessage msg = new FlashMessage(getText("problem.build.error"), MessageType.ERROR, getText("message.error.title"));
      redirect(redirectURL, msg);
    }

    redirect(redirectURL, new FlashMessage(getText("problem.build.success")));
  }
}
