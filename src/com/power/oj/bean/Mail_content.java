package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Mail_content extends Model<Mail_content>{
	public static final Mail_content dao = new Mail_content();
	public static final String ID = "id";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String CONTENT = "content";
	public static final String CTIME = "ctime";
}
