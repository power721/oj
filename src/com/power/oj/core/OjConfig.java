package com.power.oj.core;

import java.util.HashMap;
import java.util.List;

import jodd.util.collection.IntHashMap;

import com.power.oj.admin.AdminController;
import com.power.oj.bbs.BBSController;
import com.power.oj.contest.ContestController;
import com.power.oj.contest.ContestModel;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.interceptor.AccessLogInterceptor;
import com.power.oj.core.interceptor.GlobalInterceptor;
import com.power.oj.core.interceptor.FlashMessageInterceptor;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.core.model.SessionModel;
import com.power.oj.core.model.VariableModel;
import com.power.oj.core.service.OjService;
import com.power.oj.mail.MailController;
import com.power.oj.problem.ProblemController;
import com.power.oj.problem.ProblemModel;
import com.power.oj.shiro.ShiroInViewInterceptor;
import com.power.oj.shiro.freemarker.ShiroTags;
import com.power.oj.solution.SolutionController;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.*;
import com.power.oj.user.interceptor.UserInterceptor;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;

/**
 * Configure the system.
 * 
 * @author power
 * 
 */
public class OjConfig extends JFinalConfig
{
  protected static final Logger log = Logger.getLogger(OjConfig.class);

  public static String baseUrl = null;
  public static String siteTitle = null;
  public static String userAvatarPath = null;
  public static String problemImagePath = null;
  public static String uploadPath = null;
  public static String downloadPath = null;

  public static List<LanguageModel> program_languages;
  public static IntHashMap language_type = new IntHashMap();
  public static IntHashMap language_name = new IntHashMap();
  public static List<ResultType> judge_result;
  public static IntHashMap result_type = new IntHashMap();

  public static HashMap<String, VariableModel> variable = new HashMap<String, VariableModel>();

  public static int contestPageSize = 20;
  public static int contestRankPageSize = 50;
  public static int problemPageSize = 50;
  public static int userPageSize = 20;
  public static int statusPageSize = 20;

  public static long timeStamp;
  public static long startGlobalInterceptorTime;
  public static long startGlobalHandlerTime;

  private Routes routes;

  /**
   * 配置常量
   */
  public void configConstant(Constants me)
  {
    loadPropertyFile("oj.properties");

    FreeMarkerRender.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    me.setDevMode(getPropertyToBoolean("devMode", false));
    me.setBaseViewPath("/WEB-INF/view");
    me.setError401View("/WEB-INF/view/error/401.html");
    me.setError403View("/WEB-INF/view/error/403.html");
    me.setError404View("/WEB-INF/view/error/404.html");
    me.setError500View("/WEB-INF/view/error/500.html");

    log.debug("configConstant finished.");
  }

  /**
   * 配置路由
   */
  public void configRoute(Routes me)
  {
    this.routes = me;

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
    DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
    dp.addFilter(new StatFilter());
    WallFilter wall = new WallFilter();
    wall.setDbType("mysql");
    dp.addFilter(wall);
    me.add(dp);

    // 配置ActiveRecord插件
    ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
    arp.setShowSql(getPropertyToBoolean("devMode", false));
    arp.addMapping("user", "uid", UserModel.class); // 映射user表到 User模型,主键是uid
    arp.addMapping("problem", "pid", ProblemModel.class);
    arp.addMapping("solution", "sid", SolutionModel.class);
    arp.addMapping("contest", "cid", ContestModel.class);
    arp.addMapping("session", "session_id", SessionModel.class);
    arp.addMapping("program_language", LanguageModel.class);
    arp.addMapping("variable", VariableModel.class);
    me.add(arp);

    me.add(new EhCachePlugin());
    me.add(new ShiroPlugin(routes));

    log.debug("configPlugin finished.");
  }

  /**
   * 配置全局拦截器
   */
  public void configInterceptor(Interceptors me)
  {
    me.add(new GlobalInterceptor());
    me.add(new FlashMessageInterceptor());
    me.add(new AccessLogInterceptor());
    me.add(new UserInterceptor());
    me.add(new ShiroInterceptor());
    me.add(new ShiroInViewInterceptor());

    log.debug("configInterceptor finished.");
  }

  /**
   * 配置处理器
   */
  public void configHandler(Handlers me)
  {
    me.add(new UrlFilterHandler());
    me.add(new ContextPathHandler(OjConstants.BASE_URL));
    me.add(new DruidStatViewHandler("/druid"));

    log.debug("configHandler finished.");
  }

  /**
   * 初始化常量
   */
  public void afterJFinalStart()
  {
    OjService.me().initJudgeResult();
    OjService.me().loadLanguage();
    OjService.me().loadVariable();

    log.debug("afterJFinalStart finished.");
  }

  /*
   * get OJ variable from DB cache
   */
  public static String get(String name)
  {
    return get(name, null);
  }
  
  public static String get(String name, String defaultValue)
  {
    VariableModel model = variable.get(name);
    if (model != null)
    {
      return model.getStr("value");
    }
    
    return defaultValue;
  }
  
  public static Integer getInt(String name)
  {
    return getInt(name, null);
  }

  public static Integer getInt(String name, Integer defaultValue)
  {
    VariableModel model = variable.get(name);
    if (model != null)
    {
      return model.getInt("int_value");
    }
    
    return defaultValue;
  }

  public static Boolean getBoolean(String name)
  {
    return getBoolean(name ,null);
  }

  public static Boolean getBoolean(String name, Boolean defaultValue)
  {
    VariableModel model = variable.get(name);
    if (model != null)
    {
      return model.getBoolean("boolean_value");
    }
    
    return defaultValue;
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
   * 建议使用 JFinal 手册推荐的方式启动项目 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
   * 使用内置的Jetty容器， 基于Tomcat开发，需要将jetty.jar删除
   */
  public static void main(String[] args)
  {
    JFinal.start("WebRoot", 8000, "/", 5);
  }
}
