package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Loginlog extends Model<Loginlog>{
	public static final Loginlog dao = new Loginlog();
	public static final String ID = "id";
	public static final String UID = "uid";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	public static final String IP = "ip";
	public static final String CTIME = "ctime";
	public static final String SUCCESS = "success";
	public static final String INFO = "info";
}
