package com.power.oj.core.interceptor;

import javax.servlet.http.HttpSession;

import jodd.util.StringBand;
import jodd.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.power.oj.core.OjConfig;

/**
 * Update session table with url and timestamp.
 * 
 * @author power
 * 
 */
public class AccessLogInterceptor implements Interceptor
{
  protected final Logger log = Logger.getLogger(getClass());
  private String skipActions[] =
  { "/login", "/user/signin", "/logout", "/captcha", "/contest/password", "/problem/userResult" };

  @Override
  public void intercept(ActionInvocation ai)
  {
    Controller controller = ai.getController();
    HttpSession session = controller.getSession(true);
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
        OjConfig.lastURL = url;

      Db.update("UPDATE session SET last_activity=UNIX_TIMESTAMP(),uri=? WHERE session_id=?", url, session.getId());
    }

    ai.invoke();
  }

}
