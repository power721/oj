package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class MailGroup extends Model<MailGroup>{
	public static final MailGroup dao = new MailGroup();
	public static final String ID = "id";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String CTIME = "ctime";
}
