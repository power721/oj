package com.power.oj.contest;


import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.core.OjConfig;
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
    
    ContestModel contestModle = controller.getAttr("contest");
    if (contestModle == null)
    {
      contestModle = contestService.getContest(cid);
    }
    if (contestModle == null)
    {
      controller.renderError(404);
      return;
    }

    controller.setAttr("cid", cid);
    controller.setAttr("contest", contestModle);
    
    if (checkPassword(controller, contestModle))
    {
      ai.invoke();
      return;
    }
    
    controller.keepPara(OjConstants.PAGE_TITLE);

    controller.setAttr("title", contestModle.getTitle());
    controller.setAttr("cid", cid);

    controller.render("password.html");
  }

  public boolean checkPassword(Controller controller, ContestModel contestModle)
  {
    if (!contestModle.hasPassword())
    {
      return true;
    }

    Integer cid = contestModle.getCid();
    String tokenName = new StringBuilder("cid-").append(cid).toString();
    String token = controller.getSessionAttr(tokenName);
    if (token != null)
    {
      try
      {
        token = CryptUtils.decrypt(token, tokenName);
        log.info(token);
        if (contestModle.checkPassword(token))
        {
          return true;
        }
      } catch (Exception e)
      {
        if (OjConfig.isDevMode())
          e.printStackTrace();
        log.error(e.getMessage());
      }
    }
   
    return false;
  }
  
}
