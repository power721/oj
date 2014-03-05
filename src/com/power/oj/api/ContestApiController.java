package com.power.oj.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.power.oj.contest.ContestService;
import com.power.oj.core.OjController;

@Before(GuestInterceptor.class)
public class ContestApiController extends OjController
{
  private static final ContestService contestService = ContestService.me();
  
  @RequiresPermissions("contest:addProblem")
  public void addProblem()
  {
    Integer cid = getParaToInt("cid");
    Integer pid = getParaToInt("pid");
    String title = getPara("title");
    int result = contestService.addProblem(cid, pid, title);
    
    renderJson("result", result);
  }
  
  @RequiresPermissions("contest:removeProblem")
  public void removeProblem()
  {
    Integer cid = getParaToInt("cid");
    Integer pid = getParaToInt("pid");
    
    int result = contestService.removeProblem(cid, pid);
    
    if (result > 0)
    {
      renderJson("{\"success\":true}");
    }
    else if (result == -1)
    {
      renderJson("{\"success\":false, \"result\":\"Cannot delete problem.\"}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Delete problem failed.\"}");
    }
  }
  
  @RequiresPermissions("contest:addProblem")
  public void reorderProblem()
  {
    Integer cid = getParaToInt("cid");
    String pid = getPara("pid");
    
    if (contestService.reorderProblem(cid, pid) > 0)
    {
      renderJson("{\"success\":true}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Unknown error!\"}");
    }
  }

  @RequiresPermissions("contest:addUser")
  public void addUser()
  {
    Integer cid = getParaToInt("cid");
    Integer uid = getParaToInt("uid");
    //String name = getPara("name");
    int result = contestService.addUser(cid, uid);
    
    renderJson("result", result);
  }
  
  @RequiresPermissions("contest:addUser")
  public void removeUser()
  {
    Integer cid = getParaToInt("cid");
    Integer uid = getParaToInt("uid");
    
    int result = contestService.removeUser(cid, uid);
    
    renderJson("result", result);
  }
  
  public void postQuestion()
  {
    Integer cid = getParaToInt("cid");
    String question = getPara("question");
    
    renderJson("success", contestService.addClarify(cid, question));
  }
  
}
