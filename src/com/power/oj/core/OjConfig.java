package com.power.oj.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jodd.util.collection.IntHashMap;

import com.power.oj.admin.AdminController;
import com.power.oj.bbs.BBSController;
import com.power.oj.contest.ContestController;
import com.power.oj.contest.ContestModel;
import com.power.oj.core.interceptor.AccessLogInterceptor;
import com.power.oj.core.interceptor.BaseURLInterceptor;
import com.power.oj.core.interceptor.GlobalInterceptor;
import com.power.oj.core.interceptor.MessageInterceptor;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.core.model.VariableModel;
import com.power.oj.mail.MailController;
import com.power.oj.problem.ProblemController;
import com.power.oj.problem.ProblemModel;
import com.power.oj.solution.SolutionController;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.*;
import com.power.oj.util.Tool;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;

/**
 * Configure the system.
 * 
 * @author power
 * 
 */
public class OjConfig extends JFinalConfig
{
	protected final Logger log = Logger.getLogger(getClass());

	public static String baseUrl = null;
	public static String siteTitle = "Power OJ";

	public static List<LanguageModel> program_languages;
	public static IntHashMap language_type = new IntHashMap();
	public static IntHashMap language_name = new IntHashMap();
	public static List<ResultType> judge_result;
	public static IntHashMap result_type = new IntHashMap();

	public static HashMap<String, VariableModel> variable = new HashMap<String, VariableModel>();

	public static int contestPageSize = 20;
	public static int problemPageSize = 50;
	public static int userPageSize = 20;

	public static long startGlobalInterceptorTime;
	public static long startGlobalHandlerTime;

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me)
	{
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("oj.properties");

		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setBaseViewPath("/WEB-INF/pages");
		me.setError404View("/WEB-INF/pages/common/404.html");
		me.setError500View("/WEB-INF/pages/common/500.html");

		log.debug("configConstant finished.");
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me)
	{
		me.add("/", CommonController.class, "/common/");
		me.add("/admin", AdminController.class);
		me.add("/bbs", BBSController.class);
		me.add("/contest", ContestController.class);
		me.add("/mail", MailController.class);
		me.add("/problem", ProblemController.class);
		me.add("/solution", SolutionController.class);
		me.add("/user", UserController.class);

		log.debug("configRoute finished.");
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me)
	{
		// 配置C3p0数据库连接池插件
		/*
		 * C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"),
		 * getProperty("user"), getProperty("password") .trim());
		 * me.add(c3p0Plugin);
		 */
		DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		dp.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		dp.addFilter(wall);
		me.add(dp);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		arp.setShowSql(true);
		arp.addMapping("user", "uid", UserModel.class); // 映射user表到 User模型,
														// 主键是uid
		arp.addMapping("problem", "pid", ProblemModel.class);
		arp.addMapping("solution", "sid", SolutionModel.class);
		arp.addMapping("contest", "cid", ContestModel.class);
		arp.addMapping("program_language", LanguageModel.class);
		arp.addMapping("variable", VariableModel.class);
		me.add(arp);

		log.debug("configPlugin finished.");
	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me)
	{
		me.add(new GlobalInterceptor());
		me.add(new SessionInViewInterceptor());
		me.add(new BaseURLInterceptor());
		me.add(new MessageInterceptor());
		me.add(new AccessLogInterceptor());
		me.add(new UserInterceptor());

		log.debug("configInterceptor finished.");
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me)
	{
		me.add(new UrlFiterHandler());
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
		me.add(dvh);

		log.debug("configHandler finished.");
	}

	/**
	 * 初始化常量
	 */
	public void afterJFinalStart()
	{
		baseUrl = Tool.formatBaseURL(getProperty(OjConstants.BASE_URL));
		siteTitle = getProperty(OjConstants.SITE_TITLE, "Power OJ");
		init();

		log.debug("afterJFinalStart finished.");
	}

	/**
	 * Initialize configuration form DB. Initialize the programming language and
	 * judge result type.
	 */
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
			language_name.put(Language.getInt("id"), Language.getStr("name"));
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

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目 运行此 main
	 * 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此 使用内置的Jetty容器，
	 * 基于Tomcat开发，需要将jetty.jar删除
	 */
	public static void main(String[] args)
	{
		JFinal.start("WebRoot", 8000, "/", 5);
	}
}
