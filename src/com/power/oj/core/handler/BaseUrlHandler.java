package com.power.oj.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;

public class BaseUrlHandler extends Handler
{

  @Override
  public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
  {
    StringBuilder sb = new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName());
    
    if (request.getServerPort() != 80)
    {
      sb.append(":").append(request.getServerPort());
    }
    sb.append(request.getContextPath());

    OjConfig.baseUrl = sb.toString();
    
    request.setAttribute(OjConstants.BASE_URL, OjConfig.baseUrl);
    nextHandler.handle(target, request, response, isHandled);
  }

}
