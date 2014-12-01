package com.power.oj.core.interceptor;

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
  { "/problem/userResult", "/login", "/logout", "/signup", "/captcha", "/code", "/user/bind", "/user/info", "/user/archive", "/user/forget", "/user/reset", "/user/recover", "/user/verify" };

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    String actionKey = ai.getActionKey();
    boolean isPOST = "POST".equalsIgnoreCase(controller.getRequest().getMethod().toUpperCase());

    if (!isPOST && StringUtil.equalsOne(actionKey, skipActions) == -1)
    {
      StringBuilder sb = new StringBuilder(actionKey);
      if (controller.getPara() != null)
        sb.append('/').append(controller.getPara());

      String query = controller.getRequest().getQueryString();
      if (query != null)
      {
        sb.append('?').append(query);
      }

      String url = sb.toString();
      if (url.indexOf("ajax=1") == -1 && url.indexOf("api/") == -1)
      {
        SessionService.me().setLastAccessURL(url);
      }
    }

    ai.invoke();
  }
  
}
