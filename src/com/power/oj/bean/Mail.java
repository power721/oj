package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Mail extends Model<Mail>{
	public static final Mail dao = new Mail();
	public static final String ID = "id";
	public static final String MID = "mid";
	public static final String USER = "user";
	public static final String PEER = "peer";
	public static final String STATUS = "status";
}
