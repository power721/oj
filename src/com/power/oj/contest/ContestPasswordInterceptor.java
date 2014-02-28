package com.power.oj.contest;


import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConstants;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.util.CryptUtils;

public class ContestPasswordInterceptor implements Interceptor
{

  private static final Logger log = Logger.getLogger(ContestPasswordInterceptor.class);
  private static final ContestService contestService = ContestService.me();
      
  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    if (ShiroKit.hasPermission("contest:view:password"))
    {
      ai.invoke();
      return;
    }
    
    int cid = 0;
    if (controller.getParaToInt(0) != null)
      cid = controller.getParaToInt(0);
    else if (controller.getParaToInt("cid") != null)
      cid = controller.getParaToInt("cid");

    if (!contestService.isContestHasPassword(cid))
    {
      ai.invoke();
      return;
    }

    String token_name = new StringBuilder("cid-").append(cid).toString();
    String token_token = "";
    String token = controller.getSessionAttr(token_name);
    if (token != null)
    {
      try
      {
        token_token = CryptUtils.decrypt(token, token_name);
        if (contestService.checkContestPassword(cid, token_token))
        {
          ai.invoke();
          return;
        }
      } catch (Exception e)
      {
        log.error(e.getMessage());
      }
    }
   
    controller.keepPara(OjConstants.PAGE_TITLE);

    controller.setAttr("title", contestService.getContestTitle(cid));
    controller.setAttr("cid", cid);

    controller.render("password.html");
  }

}
