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
  { "/problem/userResult", "/login", "/signup", "/captcha", "/code", "/user/forget", "/user/reset" };

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    HttpSession session = controller.getSession();
    String actionKey = ai.getActionKey();
    boolean isPOST = "POST".equalsIgnoreCase(controller.getRequest().getMethod().toUpperCase());

    if (!isPOST && StringUtil.equalsOne(actionKey, skipActions) == -1)
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
