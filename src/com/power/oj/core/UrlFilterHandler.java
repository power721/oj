package com.power.oj.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * Filter the url which dose not handled by JFinal.
 * @author power
 * 
 */
public class UrlFilterHandler extends Handler
{

	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
	{
		int index = target.indexOf(".ws");
		if (index == -1)
		{
			//request.setAttribute(OjConstants.BASE_URL, OjConfig.baseUrl);
			request.setAttribute(OjConstants.SITE_TITLE, OjConfig.siteTitle);
			nextHandler.handle(target, request, response, isHandled);
		}
	}

}
