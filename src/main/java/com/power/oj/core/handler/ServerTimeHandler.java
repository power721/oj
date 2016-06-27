package com.power.oj.core.handler;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServerTimeHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        next.handle(target, request, response, isHandled);
        response.setDateHeader("ServerTime", System.currentTimeMillis());
    }
}
