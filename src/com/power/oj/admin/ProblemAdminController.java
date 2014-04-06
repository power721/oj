package com.power.oj.admin;

import java.io.IOException;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.problem.ProblemException;
import com.power.oj.problem.ProblemModel;

@RequiresPermissions("admin")
public class ProblemAdminController extends OjController
{
  public void index()
  {
    setTitle(getText("problem.index.title"));
  }

  public void show()
  {
    Integer pid = getParaToInt(0);
    ProblemModel problemModel = problemService.findProblemForShow(pid);
    if (problemModel == null)
    {
      FlashMessage msg = new FlashMessage(getText("problem.show.null"), MessageType.ERROR, getText("message.error.title"));
      redirect("/admin/problem", msg);
      return;
    }
    
    setAttr("prevPid", problemService.getPrevPid(pid));
    setAttr("nextPid", problemService.getNextPid(pid));
    setAttr("tagList", problemService.getTags(pid));
    setAttr("problem", problemModel);
    setAttr("spj", problemService.checkSpj(pid));
    
    setTitle(new StringBuilder(3).append(pid).append(": ").append(problemModel.getStr("title")).toString());
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
    String redirectURL = "/admin/problem";
    
    try
    {
      if (!problemService.addProblem(problemModel))
      {
        FlashMessage msg = new FlashMessage(getText("problem.save.warn"), MessageType.WARN, getText("message.warn.title"));
        setFlashMessage(msg);
      }
      redirectURL = new StringBuilder(2).append("/admin/problem/show/").append(problemModel.getPid()).toString();
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
  @RequiresPermissions("problem:edit")
  public void edit()
  {
    if (!isParaExists(0))
    {
      FlashMessage msg = new FlashMessage(getText("problem.para.null"), MessageType.ERROR, getText("message.error.title"));
      redirect("/admin/problem", msg);
      return;
    }

    Integer pid = getParaToInt(0);
    setAttr("problem", problemService.findProblem(pid));
    setTitle(new StringBuilder(2).append(getText("problem.edit.title")).append(pid).toString());
  }

  @RequiresPermissions("problem:edit")
  public void update()
  {
    ProblemModel problemModel = getModel(ProblemModel.class, "problem");
    problemService.updateProblem(problemModel);

    String redirectURL = new StringBuilder(2).append("/admin/problem/show/").append(problemModel.getPid()).toString();
    redirect(redirectURL, new FlashMessage(getText("problem.update.success")));
  }

  @RequiresPermissions("problem:build")
  public void build()
  {
    Integer pid = getParaToInt(0);
    String redirectURL = new StringBuilder(2).append("/admin/problem/show/").append(pid).toString();
    FlashMessage msg = new FlashMessage(getText("problem.build.success"));
    
    try
    {
      if (!problemService.build(pid))
      {
        log.error(new StringBuilder(3).append("Build problem ").append(pid).append(" statistics failed!").toString());
        msg = new FlashMessage(getText("problem.build.error"), MessageType.ERROR, getText("message.error.title"));
      }
    } catch(ProblemException e)
    {
      log.error(e.getLocalizedMessage());
      msg = new FlashMessage(getText("problem.show.null"), MessageType.ERROR, getText("message.error.title"));
    }
    
    redirect(redirectURL, msg);
  }
  
}
