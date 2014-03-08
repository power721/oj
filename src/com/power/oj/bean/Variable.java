package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Variable extends Model<Variable>{
	public static final Variable dao = new Variable();
	public static final String ID = "id";
	public static final String CID = "cid";
	public static final String NAME = "name";
	public static final String VALUE = "value";
	public static final String BOOLEAN_VALUE = "boolean_value";
	public static final String INT_VALUE = "int_value";
	public static final String TEXT_VALUE = "text_value";
	public static final String TYPE = "type";
	public static final String DESCRIPTION = "description";
}
