package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Mail extends Model<Mail>{
	public static final Mail dao = new Mail();
	public static final String ID = "id";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String REPLY = "reply";
	public static final String READ = "read";
	public static final String ATIME = "atime";
	public static final String CTIME = "ctime";
}
