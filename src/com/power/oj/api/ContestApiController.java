package com.power.oj.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.power.oj.contest.ContestService;
import com.power.oj.core.OjController;

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
    
    if(result == 0)
    {
      renderJson("{\"success\":true}");
    }
    else if (result == -1)
    {
      renderJson("{\"success\":false, \"result\":\"Duplicate problems for this contest.\"}");
    }
    else if(result == -2)
    {
      renderJson("{\"success\":false, \"result\":\"Too many problems in this contest.\"}");
    }
    else if(result == -3)
    {
      renderJson("{\"success\":false, \"result\":\"This problem does not exist.\"}");
    }
    else
    {
      renderJson("{\"success\":false, \"result\":\"Add problem failed.\"}");
    }
  }
  
}
