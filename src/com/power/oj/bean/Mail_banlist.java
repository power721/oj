package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Mail_banlist extends Model<Mail_banlist>{
	public static final Mail_banlist dao = new Mail_banlist();
	public static final String ID = "id";
	public static final String UID = "uid";
	public static final String BAN_UID = "ban_uid";
	public static final String CTIME = "ctime";
}
