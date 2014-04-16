package com.power.oj.solution;

import jodd.util.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.contest.ContestService;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.bean.ResultType;

public class SolutionController extends OjController
{
  
  @ActionKey("/status")
  public void index()
  {
    int pageNumber = getParaToInt(0, 1);
    int pageSize = getParaToInt(1, OjConfig.statusPageSize);
    int result = getParaToInt("result", -1);
    int language = getParaToInt("language", 0);
    int pid = 0;
    if (StringUtil.isNotBlank(getPara("pid")))
      pid = getParaToInt("pid", 0);
    String userName = getPara("name");
    StringBuilder query = new StringBuilder();

    if (result > -1)
    {
      query.append("&result=").append(result);
    }
    if (language > 0)
    {
      query.append("&language=").append(language);
    }
    if (pid > 0)
    {
      query.append("&pid=").append(pid);
    }
    if (StringUtil.isNotBlank(userName))
    {
      query.append("&name=").append(userName);
    }

    setAttr("solutionList", solutionService.getPage(pageNumber, pageSize, result, language, pid, userName));
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    setAttr(OjConstants.JUDGE_RESULT, OjConfig.judge_result);
    setAttr("pageSize", OjConfig.statusPageSize);
    setAttr("result", result);
    setAttr("language", language);
    setAttr("pid", getPara("pid"));
    setAttr("name", getPara("name"));
    setAttr("query", query.length()>0 ? query.substring(1).toString() : null);

    setTitle(getText("solution.index.title"));
  }

  @ActionKey("/code")
  @RequiresUser
  public void show()
  {
    Integer sid = getParaToInt(0);
    boolean isAdmin = userService.isAdmin();
    SolutionModel solutionModel = solutionService.findSolution(sid);
    ResultType resultType = OjConfig.result_type.get(solutionModel.getResult());
    Integer uid = solutionModel.getUid();
    Integer loginUid = userService.getCurrentUid();
    if (!uid.equals(loginUid) && !isAdmin)
    {
      FlashMessage msg = new FlashMessage(getText("solution.show.error"), MessageType.ERROR, getText("message.error.title"));
      redirect("/status", msg);
      return;
    }

    if (!isAdmin)
    {
      String error = solutionModel.getError();
      if (error != null)
      {
        solutionModel.set("error", error.replaceAll(StringUtil.replace(OjConfig.getString("workPath"), "\\", "\\\\"), ""));
        // TODO replace "/"
      }
    }

    String problemTitle = "";
    Integer cid = solutionModel.getCid();
    if (cid != null && cid > 0)
    {
      int num = solutionModel.getNum();
      problemTitle = ContestService.me().getProblemTitle(cid, num);
      setAttr("alpha", (char) (num + 'A'));
      setAttr("cid", cid);
    } else
    {
      problemTitle = problemService.getProblemField(solutionModel.getPid(), "title");
    }

    setAttr("problemTitle", problemTitle);
    try
    {
      setAttr("submitUser", userService.getUserByUid(uid));
    } catch (NullPointerException e)
    {
      log.warn(e.getLocalizedMessage());
    }
    
    setAttr("language", OjConfig.language_name.get(solutionModel.getLanguage()));
    setAttr("resultLongName", resultType.getLongName());
    setAttr("resultName", resultType.getName());
    setAttr("solution", solutionModel);

    String brush = getAttrForStr("language").toLowerCase();
    if ("G++".equalsIgnoreCase(brush) || "GCC".equalsIgnoreCase(brush))
      brush = "cpp";
    setAttr("brush", brush);

    setTitle(getText("solution.show.title"));
    render("code.html");
  }
  
  @RequiresPermissions("code:rejudge")
  public void rejudge()
  {
    Integer sid = getParaToInt(0);
    judgeService.rejudgeSolution(sid);
    
    redirect("/code/" + sid, new FlashMessage("Server got your rejudge request."));
  }

  public void add()
  {
    renderText("TODO");
  }

  @Before(POST.class)
  @RequiresPermissions("problem:submit")//code:add
  public void save()
  {
    SolutionModel solutionModel = getModel(SolutionModel.class, "solution");
    solutionModel.set("uid", userService.getCurrentUid());
    Integer cid = solutionModel.getInt("cid");
    String url = "/status";
    
    if (cid != null && cid > 0)
    {
      url = new StringBuilder(2).append("/contest/status/").append(cid).toString();
    }
    
    if (solutionService.submitSolution(solutionModel) != 0)
    {
      setFlashMessage(new FlashMessage(getText("solution.save.error"), MessageType.ERROR, getText("message.error.title")));
    }

    redirect(url);
  }

  public void edit()
  {
    renderText("TODO");
  }
  
}
