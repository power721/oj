package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Contest extends Model<Contest>{
	public static final Contest dao = new Contest();
	public static final String CID = "cid";
	public static final String UID = "uid";
	public static final String TITLE = "title";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String PASS = "pass";
	public static final String FREEZE = "freeze";
	public static final String ATIME = "atime";
	public static final String CTIME = "ctime";
	public static final String MTIME = "mtime";
	public static final String STATUS = "status";
}
