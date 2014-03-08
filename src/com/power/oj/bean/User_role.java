package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class User_role extends Model<User_role>{
	public static final User_role dao = new User_role();
	public static final String ID = "id";
	public static final String UID = "uid";
	public static final String RID = "rid";
	public static final String STATUS = "status";
}
