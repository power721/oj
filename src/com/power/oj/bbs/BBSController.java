package com.power.oj.bbs;

import com.jfinal.core.Controller;

public class BBSController extends Controller
{

	public void index()
	{
		setAttr("pageTitle", "Web Board");
		render("index.html");
	}
}
