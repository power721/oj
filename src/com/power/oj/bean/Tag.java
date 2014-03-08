package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Tag extends Model<Tag>{
	public static final Tag dao = new Tag();
	public static final String ID = "id";
	public static final String PID = "pid";
	public static final String UID = "uid";
	public static final String TAG = "tag";
	public static final String CTIME = "ctime";
	public static final String MTIME = "mtime";
	public static final String STATUS = "status";
}
