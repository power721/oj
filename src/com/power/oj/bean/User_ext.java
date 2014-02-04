package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class User_ext extends Model<User_ext>{
	public static final User_ext dao = new User_ext();
	public static final String UID = "uid";
	public static final String TID = "tid";
	public static final String REALNAME = "realname";
	public static final String LOGIN = "login";
	public static final String LOGIN_IP = "login_ip";
	public static final String PHONE = "phone";
	public static final String QQ = "qq";
	public static final String BLOG = "blog";
	public static final String GENDER = "gender";
	public static final String ONLINE = "online";
	public static final String LEVEL = "level";
	public static final String CREDIT = "credit";
	public static final String AVATAR = "avatar";
}
