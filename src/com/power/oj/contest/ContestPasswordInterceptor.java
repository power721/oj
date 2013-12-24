package com.power.oj.contest;

import jodd.util.StringBand;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConstants;
import com.power.oj.user.UserService;
import com.power.oj.util.CryptUtils;

public class ContestPasswordInterceptor implements Interceptor
{

  private static final Logger log = Logger.getLogger(ContestPasswordInterceptor.class);
      
  @Override
  public void intercept(ActionInvocation ai)
  {
    
    Controller controller = ai.getController();
    if (UserService.hasPermission("contest:view:password"))
    {
      ai.invoke();
      return;
    }
    
    int cid = 0;
    if (controller.getParaToInt(0) != null)
      cid = controller.getParaToInt(0);
    else if (controller.getParaToInt("cid") != null)
      cid = controller.getParaToInt("cid");

    if (!ContestModel.dao.isContestHasPassword(cid))
    {
      ai.invoke();
      return;
    }

    String token_name = new StringBand("cid-").append(cid).toString();
    String token_token = "";
    try
    {
      token_token = CryptUtils.decrypt(controller.getCookie(token_name), token_name);
      if (ContestModel.dao.checkContestPassword(cid, token_token))
      {
        ai.invoke();
        return;
      }
    } catch (Exception e)
    {
      log.error(e.getMessage());
    }
   
    controller.keepPara(OjConstants.PAGE_TITLE);

    controller.setAttr("title", ContestModel.dao.getContestTitle(cid));
    controller.setAttr("cid", cid);

    controller.render("password.html");
  }

}
