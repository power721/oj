package com.power.oj.core;

import com.jfinal.core.Controller;

public class OjController extends Controller
{

	/**
	 * Redirect to url with succes message
	 */
	public void redirect(String url, String msg)
	{
		setSessionAttr("msg", msg);
		super.redirect(url);
	}

	/**
	 * Redirect to url with message
	 * 
	 * @param msgType
	 *            success info warning error
	 */
	public void redirect(String url, String msg, String msgType, String msgTitle)
	{
		setSessionAttr("msgType", msgType);
		setSessionAttr("msgTitle", msgTitle);
		setSessionAttr("msg", msg);
		super.redirect(url);
	}

	/**
	 * Redirect to url with succes message
	 */
	public void redirect(String url, boolean withQueryString, String msg)
	{
		setSessionAttr("msg", msg);
		super.redirect(url, withQueryString);
	}

	/**
	 * Redirect to url with succes message
	 * 
	 * @param msgType
	 *            success info warning error
	 */
	public void redirect(String url, boolean withQueryString, String msg, String msgType, String msgTitle)
	{
		setSessionAttr("msgType", msgType);
		setSessionAttr("msgTitle", msgTitle);
		setSessionAttr("msg", msg);
		super.redirect(url, withQueryString);
	}
}
