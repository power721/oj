package com.power.oj.core;

import com.power.oj.admin.AdminController;
import com.power.oj.bbs.BBSController;
import com.power.oj.contest.ContestController;
import com.power.oj.contest.ContestModel;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.core.model.VariableModel;
import com.power.oj.mail.MailController;
import com.power.oj.problem.ProblemController;
import com.power.oj.problem.ProblemModel;
import com.power.oj.solution.SolutionController;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.*;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * Configure the system.
 * @author power
 *
 */
public class OjConfig extends JFinalConfig
{

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
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me)
	{
		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password")
				.trim());
		me.add(c3p0Plugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.setShowSql(true);
		arp.addMapping("user", "uid", UserModel.class); // 映射user表到 User模型, 主键是uid
		arp.addMapping("problem", "pid", ProblemModel.class);
		arp.addMapping("solution", "sid", SolutionModel.class);
		arp.addMapping("contest", "cid", ContestModel.class);
		arp.addMapping("program_language", LanguageModel.class);
		arp.addMapping("variable", VariableModel.class);
		me.add(arp);
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
	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me)
	{
		me.add(new UrlFiterHandler());
	}

	/**
	 * 初始化常量
	 */
	public void afterJFinalStart()
	{
		OjConstants.baseUrl = Tool.formatBaseURL(getProperty("baseUrl"));
		OjConstants.siteTitle = getProperty("siteTitle", "Power OJ");
		OjConstants.init();
	}

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目 运行此 main
	 * 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 * 使用内置的Jetty容器， 基于Tomcat开发，需要将jetty.jar删除
	 */
	public static void main(String[] args)
	{
		JFinal.start("WebRoot", 8000, "/", 5);
	}
}
