package com.power.oj.api;

import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.ResultType;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;

@Before(GuestInterceptor.class)
public class ContestApiController extends OjController
{
  private static final SolutionService solutionService = SolutionService.me();
  private static final UserService userService = UserService.me();
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
  
  public void updateClarify()
  {
    //Integer cid = getParaToInt("cid");
    Integer id = getParaToInt("id");
    boolean isPublic = getParaToBoolean("ispublic");
    String reply = getPara("reply");
    
    renderJson("success", contestService.updateClarify(id, reply, isPublic));
  }
  
  public void code()
  {
    // TODO check permission
    Integer sid = getParaToInt("sid");
    boolean isAdmin = userService.isAdmin();
    ContestSolutionModel solutionModel = solutionService.findContestSolution4Json(sid);
    
    if (solutionModel == null)
    {
      renderJson("{\"success\":false,\"result\":\"Cannot find code.\"}");
      return;
    }
    ResultType resultType = OjConfig.result_type.get(solutionModel.getResult());
    Integer cid = solutionModel.getCid();
    int uid = solutionModel.getUid();
    int loginUid = userService.getCurrentUid();
    
    if (uid != loginUid && !isAdmin)
    {
      renderJson("{\"success\":false,\"result\":\"Permission denied.\"}");
      return;
    }

    if (!isAdmin)
    {
      String error = solutionModel.getError();
      if (error != null)
      {
        solutionModel.setError(error.replaceAll(StringUtil.replace(OjConfig.get("workPath"), "\\", "\\\\"), ""));
        // TODO replace "/"
      }
    }
    solutionModel.setSource(HtmlEncoder.text(solutionModel.getSource()));

    int num = solutionModel.getNum();
    setAttr("success", true);
    setAttr("language", OjConfig.language_name.get(solutionModel.getLanguage()));
    setAttr("alpha", (char) (num + 'A'));
    setAttr("problemTitle", contestService.getProblemTitle(cid, num));
    setAttr("resultLongName", resultType.getLongName());
    setAttr("resultName", resultType.getName());
    setAttr("solution", solutionModel);

    String brush = getAttrForStr("language").toLowerCase();
    if ("GCC".equalsIgnoreCase(brush))
      brush = "c";
    if ("G++".equalsIgnoreCase(brush))
      brush = "cc";
    if ("Python".equalsIgnoreCase(brush))
      brush = "py";
    setAttr("brush", brush);

    renderJson(new String[]{"success", "alpha", "problemTitle", "language", "resultLongName", "resultName", "solution", "brush"});
  }

  @ClearInterceptor(ClearLayer.ALL)
  public void recent()
  {
    renderJson(contestService.getRecentContest());
  }

  public void evictRecent()
  {
    contestService.evictRecentContest();
    renderNull();
  }
  
}
