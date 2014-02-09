package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Level extends Model<Level>{
	public static final Level dao = new Level();
	public static final String LEVEL = "level";
	public static final String EXP = "exp";
}
