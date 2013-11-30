package com.power.oj.core;

public class Tool
{
	public static String formatBaseURL(String s)
	{
		while (s.endsWith("/"))
			s = s.substring(0, s.length()-1);
		return s;
	}
}
