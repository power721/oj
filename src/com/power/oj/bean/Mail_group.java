package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Mail_group extends Model<Mail_group>{
	public static final Mail_group dao = new Mail_group();
	public static final String ID = "id";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String CTIME = "ctime";
}
