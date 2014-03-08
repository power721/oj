package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Message extends Model<Message>{
	public static final Message dao = new Message();
	public static final String ID = "id";
	public static final String UID = "uid";
	public static final String PID = "pid";
	public static final String CID = "cid";
	public static final String REPLY = "reply";
	public static final String THREAD = "thread";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String ATIME = "atime";
	public static final String CTIME = "ctime";
	public static final String MTIME = "mtime";
	public static final String STATUS = "status";
}
