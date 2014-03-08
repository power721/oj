package com.power.oj.api;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import com.power.oj.problem.ProblemService;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;

@Before(GuestInterceptor.class)
public class ProblemApiController extends OjController
{
  private static final ProblemService problemService = ProblemService.me();
  
  @ClearInterceptor(ClearLayer.ALL)
  public void userInfo()
  {
    Integer pid = getParaToInt("pid");
    Integer uid = UserService.me().getCurrentUid();
    
    if (uid == null)
    {
      renderNull();
      return;
    }

    setAttr(OjConstants.LANGUAGE_NAME, OjConfig.language_name);
    setAttr(OjConstants.RESULT_TYPE, OjConfig.result_type);
    setAttr("userInfo", problemService.getUserInfo(pid, uid));
    renderJson(new String[]
    { "userInfo", "language_name", "result_type" });
  }

  @ClearInterceptor(ClearLayer.ALL)
  public void userResult()
  {
    Integer pid = getParaToInt("pid");
    Integer uid = UserService.me().getCurrentUid();
    if (uid == null)
    {
      renderNull();
      return;
    }

    Record userResult = problemService.getUserResult(pid, uid);
    if (userResult != null && userResult.getInt("result") != null)
      userResult.set("result", OjConfig.result_type.get(userResult.getInt("result")));
    
    renderJson(userResult);
  }

  @ClearInterceptor(ClearLayer.ALL)
  public void status()
  {
    /*if (!isParaExists(0))
    {
      forwardAction("/contest/problem_status");
      return;
    }*/

    Integer pid = getParaToInt(0);
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
    setAttr("user", UserService.me().getCurrentUser());
    setAttr("adminUser", UserService.me().isAdmin());
    setAttr("pid", pid);
    
    setAttr("query", query.toString());
    setAttr("solutionList", SolutionService.me().getProblemStatusPage(pageNumber, pageSize, language, pid));
    renderJson(new String[]{ "pid", "user", "adminUser", "pageSize", "language", "query", "program_languages", "solutionList" });
  }
  
  @ClearInterceptor(ClearLayer.ALL)
  public void getField()
  {
    Integer pid = getParaToInt("pid");
    String name = getPara("name");

    renderJson("result", problemService.getProblemField(pid, name));
  }
  
}
