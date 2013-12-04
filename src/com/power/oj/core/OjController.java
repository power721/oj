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
}
