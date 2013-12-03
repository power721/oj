package com.power.oj.util;

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
}
