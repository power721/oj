package com.power.oj.util;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StringKit;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Utils for common usage.
 * 
 * @author power
 * 
 */
public class Tool
{
	/**
	 * 
	 * @param url
	 *            string of url
	 * @return string of formatted base url
	 */
	public static String formatBaseURL(String url)
	{
		while (url != null && url.endsWith("/"))
			url = url.substring(0, url.length() - 1);
		return url;
	}

	/**
	 * Get the client IP address.
	 * 
	 * @param request
	 *            the HttpServletRequest object.
	 * @return the IP address.
	 */
	public static String getRemoteAddr(HttpServletRequest request)
	{
		if (StringKit.notBlank(request.getHeader("X-Real-IP")))
		{
			return request.getHeader("X-Real-IP");
		} else if (StringKit.notBlank(request.getHeader("x-forwarded-for")))
		{
			return request.getHeader("x-forwarded-for");
		}

		return request.getRemoteAddr();
	}

	/**
	 * Get the html content by url.
	 * 
	 * @param url
	 *            the string of url.
	 * @return the string of html content.
	 */
	public static String getHtmlByUrl(String url)
	{
		String html = null;

		HttpClient httpclient = new DefaultHttpClient();
		try
		{
			HttpGet httpget = new HttpGet(url);
			System.out.println("executing request " + httpget.getURI());

			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			html = httpclient.execute(httpget, responseHandler);

		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}

		return html;
	}
}
