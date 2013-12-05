package com.power.oj.util;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StringKit;

/**
 * Utils for common usage.
 * @author power
 *
 */
public class Tool
{
	/**
	 * 
	 * @param url string of url
	 * @return string of formatted base url
	 */
	public static String formatBaseURL(String url)
	{
		while (url !=null && url.endsWith("/"))
			url = url.substring(0, url.length()-1);
		return url;
	}
	
	/**
	 * Get the client IP address.
	 * @param request the HttpServletRequest object.
	 * @return the IP address.
	 */
	public static String getRemoteAddr(HttpServletRequest request)
	{
		if (StringKit.notBlank(request.getHeader("X-Real-IP")))
		{
			return request.getHeader("X-Real-IP");
		}
		else if (StringKit.notBlank(request.getHeader("x-forwarded-for")))
		{
			return request.getHeader("x-forwarded-for");
		}

		return request.getRemoteAddr();
	}
}
