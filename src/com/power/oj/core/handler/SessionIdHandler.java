package com.power.oj.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class SessionIdHandler extends Handler
{

  @Override
  public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
  {
    int pos = target.indexOf(";");
    if (pos != -1) {
        target = target.substring(0, pos);
    }
    nextHandler.handle(target, request, response, isHandled);
  }

}
