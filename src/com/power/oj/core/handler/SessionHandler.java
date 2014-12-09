package com.power.oj.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.power.oj.core.service.SessionService;

public class SessionHandler extends Handler
{

  @Override
  public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
  {
    int pos = target.indexOf(';');
    if (pos != -1) {
        target = target.substring(0, pos);
    }
    
    SessionService.me().setHost(request);
    SessionService.me().setUserAgent(request);
    SessionService.me().checkAndSetSession();
    
    nextHandler.handle(target, request, response, isHandled);
  }

}
