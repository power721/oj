package com.power.oj.solution;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.contest.ContestModel;
import com.power.oj.contest.ContestRankWebSocket;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.Message;
import com.power.oj.core.bean.MessageType;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.judge.Judge;
import com.power.oj.problem.ProblemModel;
import com.power.oj.user.UserModel;

public class SolutionController extends OjController
{

  @ActionKey("/status")
  public void index()
  {
    int pageNumber = getParaToInt("p", 1);
    int pageSize = getParaToInt("s", 20);
    int result = getParaToInt("result", -1);
    int language = getParaToInt("language", -1);
    int pid = 0;
    if (StringUtil.isNotBlank(getPara("pid")))
      pid = getParaToInt("pid", 0);
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
    if (pid > 0)
    {
      query.append("&pid=").append(pid);
    }
    if (StringUtil.isNotBlank(userName))
    {
      query.append("&name=").append(userName);
    }

    setTitle("Status");
    setAttr("solutionList", SolutionModel.dao.getPage(pageNumber, pageSize, result, language, pid, userName));
    setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.program_languages);
    setAttr(OjConstants.JUDGE_RESULT, OjConfig.judge_result);
    setAttr("result", result);
    setAttr("language", language);
    setAttr("pid", getPara("pid"));
    setAttr("name", getPara("name"));
    setAttr("query", query.toString());

    render("index.html");
  }

  @ActionKey("/code")
  @RequiresUser
  public void show()
  {
    int sid = getParaToInt(0);
    boolean isAdmin = getAttr(OjConstants.ADMIN_USER) != null;
    SolutionModel solutionModel = SolutionModel.dao.findFirst("SELECT * FROM solution WHERE sid=? LIMIT 1", sid);
    ResultType resultType = (ResultType) OjConfig.result_type.get(solutionModel.getInt("result"));
    int uid = solutionModel.getUid();
    int loginUid = getAttrForInt(OjConstants.USER_ID);
    if (uid != loginUid && !isAdmin)
    {
      Message msg = new Message("Permission Denied.", MessageType.ERROR, "Error!");
      redirect("/status", msg);
      return;
    }

    if (!isAdmin)
    {
      String error = solutionModel.getStr("error");
      if (error != null)
        solutionModel.set("error", error.replaceAll(StringUtil.replace(OjConfig.get("work_path"), "\\", "\\\\"), ""));
    }

    String problemTitle = "";
    int cid = solutionModel.getInt("cid");
    System.out.println(cid);
    if (cid > 0)
    {
      int num = solutionModel.getInt("num");
      problemTitle = ContestModel.dao.getProblemTitle(cid, num);
      setAttr("alpha", (char) (num + 'A'));
      setAttr("cid", cid);
    } else
    {
      problemTitle = ProblemModel.dao.getProblemTitle(solutionModel.getInt("pid"));
    }

    setTitle("Source code");
    setAttr("problemTitle", problemTitle);
    try
    {
      setAttr(OjConstants.USER, UserModel.dao.findById(uid, "name").get("name"));
    } catch (NullPointerException e)
    {
      log.warn(e.getLocalizedMessage());
    }
    LanguageModel language = (LanguageModel) OjConfig.language_type.get(solutionModel.getInt("language"));
    setAttr("language", language.get("name"));

    setAttr("resultLongName", resultType.getLongName());
    setAttr("resultName", resultType.getName());
    setAttr("solution", solutionModel);

    String brush = getAttrForStr("language").toLowerCase();
    if ("G++".equalsIgnoreCase(brush) || "GCC".equalsIgnoreCase(brush))
      brush = "cpp";
    setAttr("brush", brush);

    render("code.html");
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
    solutionModel.set("uid", getAttrForInt(OjConstants.USER_ID));
    String url = "/status";
    if (solutionModel.get("cid") != null)
    {
      int cid = solutionModel.getInt("cid");
      if (cid > 0)
      {
        url = "/contest/status/" + cid;
        ContestRankWebSocket.broadcast(cid, cid + "-" + solutionModel.getInt("num") + ": " + solutionModel.getUid());
      }
    }

    if (solutionModel.addSolution())
    {
      ProblemModel problemModel = ProblemModel.dao.findById(solutionModel.getInt("pid"));
      if (problemModel == null)
      {
        Message msg = new Message("Please choose a correct problem", MessageType.ERROR, "Error!");
        redirect(url, msg);
        return;
      }
      long stime = OjConfig.timeStamp;
      problemModel.set("submit", problemModel.getInt("submit") + 1).set("stime", stime);
      problemModel.update();

      synchronized (Judge.judgeList)
      {
        Judge.judgeList.add(solutionModel);
        if (Judge.threads < 1)
        {
          Judge.threads += 1;
          @SuppressWarnings("unused")
          Judge judge = new Judge();
        }
      }
      System.out.println(solutionModel.getInt("sid"));
    } else
    {
      Message msg = new Message("Submit failed, maybe code length is incorrect.", MessageType.ERROR, "Error!");
      redirect(url, msg);
      return;
    }

    redirect(url);
  }

  public void edit()
  {
    renderText("TODO");
  }
}
