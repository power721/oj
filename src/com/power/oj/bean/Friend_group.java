package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Friend_group extends Model<Friend_group>{
	public static final Friend_group dao = new Friend_group();
	public static final String ID = "id";
	public static final String UID = "uid";
	public static final String NAME = "name";
	public static final String CTIME = "ctime";
}
