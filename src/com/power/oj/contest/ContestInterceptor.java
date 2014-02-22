package com.power.oj.contest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.shiro.ShiroKit;

public class ContestInterceptor implements Interceptor
{
  private static final ContestService contestService = ContestService.me();
  
  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    if (ShiroKit.hasPermission("contest:view"))
    {
      ai.invoke();
      return;
    }
    
    int cid = 0;
    if (controller.getParaToInt(0) != null)
      cid = controller.getParaToInt(0);
    else if (controller.getParaToInt("cid") != null)
      cid = controller.getParaToInt("cid");
    
    if (contestService.isContestPending(cid))
    {
      controller.renderError(401);
    }
    
    ai.invoke();
  }

}
