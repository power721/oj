package com.power.oj.contest;

import jodd.util.StringBand;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConstants;

public class ContestPasswordInterceptor implements Interceptor
{

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    int cid = controller.getParaToInt(0);

    if (!ContestModel.dao.isContestHasPassword(cid))
    {
      ai.invoke();
      return;
    }

    String token_name = new StringBand("cid").append(cid).toString();
    String contest_token = controller.getCookie(token_name);
    if (ContestModel.dao.checkContestPassword(cid, contest_token))
    {
      ai.invoke();
    } else
    {
      controller.keepPara(OjConstants.PAGE_TITLE);

      controller.setAttr("title", ContestModel.dao.getContestTitle(cid));
      controller.setAttr("cid", cid);

      controller.render("password.html");
    }
  }

}
