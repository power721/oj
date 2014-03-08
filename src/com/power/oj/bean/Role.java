package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Role extends Model<Role>{
	public static final Role dao = new Role();
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";
}
