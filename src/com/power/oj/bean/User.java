package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User>{
	public static final User dao = new User();
	public static final String UID = "uid";
	public static final String TID = "tid";
	public static final String NAME = "name";
	public static final String PASS = "pass";
	public static final String NICK = "nick";
	public static final String REALNAME = "realname";
	public static final String EMAIL = "email";
	public static final String LANGUAGE = "language";
	public static final String SCHOOL = "school";
	public static final String SOLVED = "solved";
	public static final String ACCEPT = "accept";
	public static final String SUBMIT = "submit";
	public static final String ATIME = "atime";
	public static final String CTIME = "ctime";
	public static final String MTIME = "mtime";
	public static final String LOGIN = "login";
	public static final String LOGIN_IP = "login_ip";
	public static final String PHONE = "phone";
	public static final String QQ = "qq";
	public static final String BLOG = "blog";
	public static final String GENDER = "gender";
	public static final String COMEFROM = "comefrom";
	public static final String ONLINE = "online";
	public static final String LEVEL = "level";
	public static final String CREDIT = "credit";
	public static final String SHARE = "share";
	public static final String AVATAR = "avatar";
	public static final String SIGN = "sign";
	public static final String CHECKIN = "checkin";
	public static final String CHECKINTIMES = "checkinTimes";
	public static final String STATUS = "status";
	public static final String DATA = "data";
	public static final String TOKEN = "token";
}
