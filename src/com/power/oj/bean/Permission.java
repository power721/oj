package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Permission extends Model<Permission>{
	public static final Permission dao = new Permission();
	public static final String ID = "id";
	public static final String MODULE = "module";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String TITLE = "title";
	public static final String PARENTID = "parentID";
	public static final String STATUS = "status";
}
