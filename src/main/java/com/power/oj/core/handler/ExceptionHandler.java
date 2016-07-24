package com.power.oj.core.handler;

import com.jfinal.handler.Handler;
import com.jfinal.log.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionHandler extends Handler {

    private static final Logger LOGGER = Logger.getLogger(ExceptionHandler.class);

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        try {
            next.handle(target, request, response, isHandled);
        } catch (Throwable t) {
            LOGGER.error("exception caught.", t);
        }
    }

}
