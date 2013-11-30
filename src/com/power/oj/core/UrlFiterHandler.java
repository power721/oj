package com.power.oj.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class UrlFiterHandler extends Handler
{

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
	{
		// TODO Auto-generated method stub
		int index = target.indexOf(".ws");
		if (index == -1)
		{
			request.setAttribute("baseUrl", OjConstants.baseUrl);
			request.setAttribute("siteTitle", OjConstants.siteTitle);
			nextHandler.handle(target, request, response, isHandled);
		}
	}

}
