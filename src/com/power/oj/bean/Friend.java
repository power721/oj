package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Friend extends Model<Friend>{
	public static final Friend dao = new Friend();
	public static final String ID = "id";
	public static final String GID = "gid";
	public static final String USER = "user";
	public static final String FRIEND = "friend";
	public static final String CTIME = "ctime";
}
