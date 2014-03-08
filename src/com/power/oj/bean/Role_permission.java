package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Role_permission extends Model<Role_permission>{
	public static final Role_permission dao = new Role_permission();
	public static final String ID = "id";
	public static final String RID = "rid";
	public static final String PID = "pid";
}
