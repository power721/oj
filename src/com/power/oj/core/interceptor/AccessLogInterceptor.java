package com.power.oj.core.interceptor;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.core.service.SessionService;

/**
 * Update session table with url and timestamp.
 * 
 * @author power
 * 
 */
public class AccessLogInterceptor implements Interceptor
{
  protected static final Logger log = Logger.getLogger(AccessLogInterceptor.class);
  
  private String skipActions[] =
  { "/login", "/user/signin", "/logout", "/captcha", "/contest/password", "/problem/userResult" };

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    Session session = SecurityUtils.getSubject().getSession(true);
    String actionKey = ai.getActionKey();

    if (StringUtil.equalsOne(actionKey, skipActions) == -1)
    {
      StringBand sb = new StringBand(actionKey);
      if (controller.getPara() != null)
        sb.append("/").append(controller.getPara());

      String query = controller.getRequest().getQueryString();
      if (query != null)
      {
        sb.append("?").append(query);
      }

      String url = sb.toString();
      if (url.indexOf("ajax=1") == -1)
        SessionService.setLastAccessURL(url);

      SessionService.update(session, url);
    }

    ai.invoke();
  }
  
}
