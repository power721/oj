package com.power.oj.core.handler;

import com.jfinal.handler.Handler;
import com.power.oj.core.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        int pos = target.indexOf(';');
        if (pos != -1) {
            target = target.substring(0, pos);
        }

        SessionService.me().setHost(request);
        SessionService.me().setUserAgent(request);

        next.handle(target, request, response, isHandled);
    }

}
