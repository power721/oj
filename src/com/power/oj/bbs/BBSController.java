package com.power.oj.bbs;

import com.power.oj.core.OjController;

public class BBSController extends OjController
{

	public void index()
	{
		setTitle("Web Board");
		render("index.html");
	}
}
