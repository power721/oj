package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Mail_banlist extends Model<Mail_banlist>{
	public static final Mail_banlist dao = new Mail_banlist();
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String BAN_USER = "ban_user";
	public static final String CTIME = "ctime";
}
