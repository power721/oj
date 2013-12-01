package com.power.oj.core;

import com.jfinal.core.Controller;

/**
 * Base Controller
 * @author power
 *
 */
public class OjController extends Controller
{

	/**
	 * Redirect to url with succes message。
	 * @param url string of destination url
	 * @param msg string of message content
	 */
	public void redirect(String url, String msg)
	{
		setSessionAttr("msg", msg);
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
		setSessionAttr("msgType", msgType);
		setSessionAttr("msgTitle", msgTitle);
		setSessionAttr("msg", msg);
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
		setSessionAttr("msg", msg);
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
		setSessionAttr("msgType", msgType);
		setSessionAttr("msgTitle", msgTitle);
		setSessionAttr("msg", msg);
		super.redirect(url, withQueryString);
	}
}
