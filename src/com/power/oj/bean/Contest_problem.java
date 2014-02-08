package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Contest_problem extends Model<Contest_problem>{
	public static final Contest_problem dao = new Contest_problem();
	public static final String ID = "id";
	public static final String CID = "cid";
	public static final String PID = "pid";
	public static final String TITLE = "title";
	public static final String NUM = "num";
	public static final String ACCEPT = "accept";
	public static final String SUBMIT = "submit";
	public static final String FIRST_BLOOD = "first_blood";
	public static final String FIRST_BLOOD_TIME = "first_blood_time";
}
