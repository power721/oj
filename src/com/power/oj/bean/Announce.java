package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Announce extends Model<Announce>{
	public static final Announce dao = new Announce();
	public static final String ID = "id";
	public static final String UID = "uid";
	public static final String TITLE = "title";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";
	public static final String CONTENT = "content";
	public static final String ATIME = "atime";
	public static final String CTIME = "ctime";
	public static final String MTIME = "mtime";
	public static final String STATUS = "status";
}
