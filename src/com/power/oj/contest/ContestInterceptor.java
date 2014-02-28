package com.power.oj.contest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserService;

public class ContestInterceptor implements Interceptor
{
  private static final ContestService contestService = ContestService.me();
  private static final UserService userService = UserService.me();
  
  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    Integer cid = 0;
    if (controller.getParaToInt(0) != null)
      cid = controller.getParaToInt(0);
    else if (controller.getParaToInt("cid") != null)
      cid = controller.getParaToInt("cid");
    
    ContestModel contestModle = contestService.getContest(cid);
    
    if (contestModle == null)
    {
      controller.renderError(404);
      return;
    }

    controller.setAttr("cid", cid);
    controller.setAttr("contest", contestModle);
    
    if (!ShiroKit.hasPermission("contest:view"))
    {
      Integer uid = userService.getCurrentUid();
      if (contestModle.isStrictPrivate() || contestModle.isTest())
      {
        if (uid == null || !contestService.isUserInContest(uid, cid))
        {
          controller.renderError(401);
          return;
        }
      } 
      else if (contestModle.isPrivate() && !contestModle.isFinished())
      {
        if (uid == null || !contestService.isUserInContest(uid, cid))
        {
          controller.renderError(401);
          return;
        }
      }
      
      if (contestModle.isPending())
      {
        controller.render("pending.html");
        return;
      }
    }
    
    ai.invoke();
  }

}
