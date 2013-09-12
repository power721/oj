package com.power.oj.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.power.oj.common.model.LanguageModel;
import com.power.oj.common.model.VariableModel;

import jodd.util.collection.IntHashMap;

public class OjConstants
{
	public static String baseUrl = null;
	public static String siteTitle = "Power OJ";
	public static final String randomCodeKey = "PowerOJCaptcha";
	
	public static List<LanguageModel> program_languages;
	public static IntHashMap language_type = new IntHashMap();
	public static List<ResultType> judge_result;
	public static IntHashMap result_type = new IntHashMap();

	public static HashMap<String, VariableModel> variable = new HashMap<String, VariableModel>();

	public static int contestPageSize = 20;
	public static int problemPageSize = 50;
	public static int userPageSize = 20;
	
	public static long startGlobalInterceptorTime;
	public static long startGlobalHandlerTime;

	public static void init()
	{
		for (VariableModel variableModel : VariableModel.dao.find("SELECT * FROM variable"))
		{
			variable.put(variableModel.getStr("name"), variableModel);
		}

		program_languages = LanguageModel.dao.find("SELECT * FROM program_language WHERE status=1");
		for (LanguageModel Language : program_languages)
		{
			language_type.put(Language.getInt("id"), Language);
		}

		judge_result = new ArrayList<ResultType>();
		judge_result.add(new ResultType(ResultType.AC, "AC", "Accepted"));
		judge_result.add(new ResultType(ResultType.PE, "PE", "Presentation Error"));
		judge_result.add(new ResultType(ResultType.TLE, "TLE", "Time Limit Exceed"));
		judge_result.add(new ResultType(ResultType.MLE, "MLE", "Memory Limit Exceed"));
		judge_result.add(new ResultType(ResultType.WA, "WA", "Wrong Answer"));
		judge_result.add(new ResultType(ResultType.RE, "RE", "Runtime Error"));
		judge_result.add(new ResultType(ResultType.OLE, "OLE", "Output Limit Exceed"));
		judge_result.add(new ResultType(ResultType.CE, "CE", "Compile Error"));
		judge_result.add(new ResultType(ResultType.SE, "SE", "System Error"));
		judge_result.add(new ResultType(ResultType.VE, "VE", "Validate Error"));
		judge_result.add(new ResultType(ResultType.Wait, "Wait", "Waiting"));

		for (Iterator<ResultType> it = judge_result.iterator(); it.hasNext();)
		{
			ResultType resultType = it.next();
			result_type.put(resultType.getId(), resultType);
		}
	}
	
	/*
	 * get OJ variable from DB cache
	 */
	public static String get(String name)
	{
		return variable.get(name).getStr("value");
	}

	public static int getInt(String name)
	{
		return variable.get(name).getInt("int_value");
	}

	public static boolean getBoolean(String name)
	{
		return variable.get(name).getBoolean("boolean_value");
	}

	public static String getText(String name)
	{
		return variable.get(name).getStr("text_value");
	}

	public static String getType(String name)
	{
		return variable.get(name).getStr("type");
	}
}
