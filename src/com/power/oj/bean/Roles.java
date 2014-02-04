package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Roles extends Model<Roles>{
	public static final Roles dao = new Roles();
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String STATUS = "status";
}
