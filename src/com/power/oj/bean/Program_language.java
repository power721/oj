package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Program_language extends Model<Program_language>{
	public static final Program_language dao = new Program_language();
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String EXT_TIME = "ext_time";
	public static final String EXT_MEMORY = "ext_memory";
	public static final String TIME_FACTOR = "time_factor";
	public static final String MEMORY_FACTOR = "memory_factor";
	public static final String EXT = "ext";
	public static final String EXE = "exe";
	public static final String COMPLIE_ORDER = "complie_order";
	public static final String COMPILE_CMD = "compile_cmd";
	public static final String BRUSH = "brush";
	public static final String SCRIPT = "script";
	public static final String STATUS = "status";
}
