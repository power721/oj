package com.power.oj.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * 
 * @author power
 *
 */
public class UrlFiterHandler extends Handler
{

	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
	{
		int index = target.indexOf(".ws");
		if (index == -1)
		{
			request.setAttribute("baseUrl", OjConfig.baseUrl);
			request.setAttribute("siteTitle", OjConfig.siteTitle);
			nextHandler.handle(target, request, response, isHandled);
		}
	}

}
