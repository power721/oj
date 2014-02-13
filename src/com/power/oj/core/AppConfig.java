package com.power.oj.core;

import java.util.Locale;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.i18n.I18N;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;
import com.power.oj.admin.AdminController;
import com.power.oj.api.FriendApiController;
import com.power.oj.api.MailApiController;
import com.power.oj.api.UserApiController;
import com.power.oj.bbs.BBSController;
import com.power.oj.contest.ContestController;
import com.power.oj.contest.ContestModel;
import com.power.oj.core.controller.MainController;
import com.power.oj.core.controller.UeditorController;
import com.power.oj.core.handler.UrlFilterHandler;
import com.power.oj.core.interceptor.AccessLogInterceptor;
import com.power.oj.core.interceptor.BaseURLInterceptor;
import com.power.oj.core.interceptor.FlashMessageInterceptor;
import com.power.oj.core.interceptor.GlobalInterceptor;
import com.power.oj.core.interceptor.I18NInterceptor;
import com.power.oj.core.interceptor.DebugInterceptor;
import com.power.oj.core.model.LanguageModel;
import com.power.oj.core.model.SessionModel;
import com.power.oj.core.model.VariableModel;
import com.power.oj.mail.MailContentModel;
import com.power.oj.mail.MailController;
import com.power.oj.mail.MailModel;
import com.power.oj.problem.ProblemController;
import com.power.oj.problem.ProblemModel;
import com.power.oj.service.VisitCountService;
import com.power.oj.shiro.ShiroInViewInterceptor;
import com.power.oj.shiro.freemarker.ShiroTags;
import com.power.oj.social.FriendGroupModel;
import com.power.oj.social.FriendModel;
import com.power.oj.solution.SolutionController;
import com.power.oj.solution.SolutionModel;
import com.power.oj.user.UserController;
import com.power.oj.user.UserExtModel;
import com.power.oj.user.UserInterceptor;
import com.power.oj.user.UserModel;

public class AppConfig extends JFinalConfig
{
  private static final Logger log = Logger.getLogger(AppConfig.class);
  private Routes routes;
  private static String baseViewPath;

  /**
   * 配置常量
   */
  public void configConstant(Constants me)
  {
    loadPropertyFile("oj.properties");

    FreeMarkerRender.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    me.setDevMode(getPropertyToBoolean("devMode", false));
    baseViewPath = "/WEB-INF/view";
    me.setBaseViewPath(baseViewPath);
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

    me.add("/", MainController.class, "/common/");
    me.add("/ueditor", UeditorController.class, "/common/");
    me.add("/admin", AdminController.class);
    me.add("/bbs", BBSController.class);
    me.add("/contest", ContestController.class);
    me.add("/mail", MailController.class);
    me.add("/problem", ProblemController.class);
    me.add("/solution", SolutionController.class);
    me.add("/user", UserController.class);
    me.add("/api/mail", MailApiController.class, "/mail/");
    me.add("/api/user", UserApiController.class, "/user/");
    me.add("/api/friend", FriendApiController.class, "/user/");

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
    arp.addMapping("user_ext", "uid", UserExtModel.class);
    arp.addMapping("friend", FriendModel.class);
    arp.addMapping("friend_group", FriendGroupModel.class);
    arp.addMapping("problem", "pid", ProblemModel.class);
    arp.addMapping("solution", "sid", SolutionModel.class);
    arp.addMapping("contest", "cid", ContestModel.class);
    arp.addMapping("session", "session_id", SessionModel.class);
    arp.addMapping("mail", MailModel.class);
    arp.addMapping("mail_content", MailContentModel.class);
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
    if (OjConfig.getDevMode())
      me.add(new DebugInterceptor());
    me.add(new BaseURLInterceptor());
    me.add(new I18NInterceptor());
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
    //me.add(new ContextPathHandler(OjConstants.BASE_URL));
    me.add(new DruidStatViewHandler("/druid"));

    log.debug("configHandler finished.");
  }

  /**
   * 初始化常量
   */
  public void afterJFinalStart()
  {
    OjConfig.initJudgeResult();
    OjConfig.loadLanguage();
    OjConfig.loadVariable();
    OjConfig.loadLevel();
    
    I18N.init("ojText", Locale.ENGLISH, null);
    VisitCountService.start();
    
    log.info(PathKit.getWebRootPath());
    log.debug("afterJFinalStart finished.");
  }

  public static String getBaseViewPath()
  {
    return baseViewPath;
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
