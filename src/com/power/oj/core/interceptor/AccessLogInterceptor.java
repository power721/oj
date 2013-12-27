package com.power.oj.core.interceptor;

import javax.servlet.http.HttpSession;
import jodd.util.StringUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

import com.power.oj.core.service.SessionService;

/**
 * Update session table with url and timestamp.
 * 
 * @author power
 * 
 */
public class AccessLogInterceptor implements Interceptor
{
  private String skipActions[] =
  { "/login", "/user/signin", "/user/forget", "/user/recover", "/user/reset", "/user/resetPassword", "/logout", "/signup", "/captcha", "/contest/password", "/problem/userResult" };

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    HttpSession session = controller.getSession();
    String actionKey = ai.getActionKey();

    if (StringUtil.equalsOne(actionKey, skipActions) == -1)
    {
      StringBuilder sb = new StringBuilder(actionKey);
      if (controller.getPara() != null)
        sb.append("/").append(controller.getPara());

      String query = controller.getRequest().getQueryString();
      if (query != null)
      {
        sb.append("?").append(query);
      }

      String url = sb.toString();
      if (url.indexOf("ajax=1") == -1)
        SessionService.me().setLastAccessURL(url);

      SessionService.me().updateSession(session, url);
    }

    ai.invoke();
  }
  
}
