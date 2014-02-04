package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Problem extends Model<Problem>{
	public static final Problem dao = new Problem();
	public static final String PID = "pid";
	public static final String UID = "uid";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String INPUT = "input";
	public static final String OUTPUT = "output";
	public static final String IN_PATH = "in_path";
	public static final String OUT_PATH = "out_path";
	public static final String SAMPLE_INPUT = "sample_input";
	public static final String SAMPLE_OUTPUT = "sample_output";
	public static final String HINT = "hint";
	public static final String SOURCE = "source";
	public static final String SAMPLE_PROGRAM = "sample_Program";
	public static final String TIME_LIMIT = "time_limit";
	public static final String MEMORY_LIMIT = "memory_limit";
	public static final String ATIME = "atime";
	public static final String CTIME = "ctime";
	public static final String MTIME = "mtime";
	public static final String STIME = "stime";
	public static final String ACCEPT = "accept";
	public static final String SOLVED = "solved";
	public static final String SUBMIT = "submit";
	public static final String SUBMIT_USER = "submit_user";
	public static final String ERROR = "error";
	public static final String RATIO = "ratio";
	public static final String DIFFICULTY = "difficulty";
	public static final String VIEW = "view";
	public static final String STATUS = "status";
}
