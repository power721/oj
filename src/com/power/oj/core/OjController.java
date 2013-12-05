package com.power.oj.core;

import javax.servlet.http.Cookie;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

/**
 * Base Controller
 * @author power
 *
 */
public class OjController extends Controller
{
	protected final Logger log = Logger.getLogger(getClass());
	
	/**
	 * Redirect to url with succes message。
	 * @param url string of destination url
	 * @param msg string of message content
	 */
	public void redirect(String url, String msg)
	{
		setSessionAttr(OjConstants.MSG, msg);
		super.redirect(url);
	}

	/**
	 * Redirect to url with message。
	 * @param url string of destination url
	 * @param msg string of message content
	 * @param msgType string of "success", "info", "warning", "error"
	 * @param msgTitle string of message title
	 */
	public void redirect(String url, String msg, String msgType, String msgTitle)
	{
		setSessionAttr(OjConstants.MSG_TYPE, msgType);
		setSessionAttr(OjConstants.MSG_TITLE, msgTitle);
		setSessionAttr(OjConstants.MSG, msg);
		super.redirect(url);
	}

	/**
	 * Redirect to url with succes message。
	 * @param url string of destination url
	 * @param withQueryString whether contains query string
	 * @param msg string of message content
	 */
	public void redirect(String url, boolean withQueryString, String msg)
	{
		setSessionAttr(OjConstants.MSG, msg);
		super.redirect(url, withQueryString);
	}

	/**
	 * Redirect to url with message。
	 * @param url string of destination url
	 * @param withQueryString whether contains query string
	 * @param msg string of message content
	 * @param msgType string of "success", "info", "warning", "error"
	 * @param msgTitle string of message title
	 */
	public void redirect(String url, boolean withQueryString, String msg, String msgType, String msgTitle)
	{
		setSessionAttr(OjConstants.MSG_TYPE, msgType);
		setSessionAttr(OjConstants.MSG_TITLE, msgTitle);
		setSessionAttr(OjConstants.MSG, msg);
		super.redirect(url, withQueryString);
	}
	
	/**
	 * The the page title of the view.
	 * @param title string of the page title.
	 */
	public void setTitle(String title)
	{
		setAttr(OjConstants.PAGE_TITLE, title);
	}
	
	/**
	 * Set the redirect uri, redirect to this uri after login/logout.
	 * @param redirectURI string of the uri.
	 */
	public void setRedirectURI(String redirectURI)
	{
		setAttr(OjConstants.REDIRECT_URI, redirectURI);
	}
	
	/**
	 * Set the cookie in safe way.
	 * @param name
	 * @param value
	 * @param maxAgeInSeconds
	 * @return this OjController.
	 */
	public OjController setCookieHttpOnly(String name, String value, int maxAgeInSeconds)
	{
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		setCookie(cookie);
		return this;
	}
	
	/**
	 * Get the Logger object.
	 * @return log
	 */
	public Logger getLog()
	{
		return log;
	}
}
