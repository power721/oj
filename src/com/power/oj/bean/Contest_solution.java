package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Contest_solution extends Model<Contest_solution>{
	public static final Contest_solution dao = new Contest_solution();
	public static final String SID = "sid";
	public static final String UID = "uid";
	public static final String PID = "pid";
	public static final String CID = "cid";
	public static final String NUM = "num";
	public static final String TIME = "time";
	public static final String MEMORY = "memory";
	public static final String RESULT = "result";
	public static final String LANGUAGE = "language";
	public static final String CTIME = "ctime";
	public static final String ERROR = "error";
	public static final String SOURCE = "source";
	public static final String CODE_LEN = "code_len";
	public static final String SYSTEM_ERROR = "system_error";
}
