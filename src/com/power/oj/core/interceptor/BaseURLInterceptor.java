package com.power.oj.core.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;

/**
 * Get the baseUrl and set in controller attribute.
 * 
 * @author power
 * 
 */
public class BaseURLInterceptor implements Interceptor
{

  @Override
  public void intercept(ActionInvocation ai)
  {
    String baseUrl = OjConfig.baseUrl;
    Controller controller = ai.getController();

    //if (StringKit.isBlank(baseUrl)) // if the baseUrl not initialized, we detect from the scheme once.
    {
      HttpServletRequest request = controller.getRequest();

      StringBuilder sb = new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName());
      if (request.getServerPort() != 80)
      {
        sb.append(":").append(request.getServerPort());
      }
      sb.append(request.getContextPath());

      baseUrl = sb.toString();
      OjConfig.baseUrl = baseUrl;
    }

    controller.setAttr(OjConstants.BASE_URL, baseUrl);

    ai.invoke();
  }

}
