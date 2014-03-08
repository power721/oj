package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Session extends Model<Session>{
	public static final Session dao = new Session();
	public static final String SESSION_ID = "session_id";
	public static final String UID = "uid";
	public static final String NAME = "name";
	public static final String IP_ADDRESS = "ip_address";
	public static final String USER_AGENT = "user_agent";
	public static final String CTIME = "ctime";
	public static final String LAST_ACTIVITY = "last_activity";
	public static final String SESSION_EXPIRES = "session_expires";
	public static final String SESSION_DATA = "session_data";
	public static final String USER_DATA = "user_data";
	public static final String URI = "uri";
}
