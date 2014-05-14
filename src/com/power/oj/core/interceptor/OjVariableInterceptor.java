package com.power.oj.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConfig;

public class OjVariableInterceptor implements Interceptor
{

  @Override
  public void intercept(ActionInvocation ai)
  {
    ai.invoke();
    
    Controller controller = ai.getController();
    controller.setAttr("enable_qq_login", OjConfig.getBoolean("enable_qq_login"));
    controller.setAttr("enable_sina_login", OjConfig.getBoolean("enable_sina_login"));
    controller.setAttr("dev", OjConfig.isDevMode());
    controller.setAttr("gaAccount", OjConfig.getString("gaAccount"));
    controller.setAttr("domaiNname", OjConfig.getString("domaiNname"));
    controller.setAttr("cdn", OjConfig.getString("cdn"));
    controller.setAttr("icpRecord", OjConfig.getString("icpRecord"));
    controller.setAttr("version", OjConfig.getString("version", "20140221"));
    controller.setAttr("oj_style", controller.getCookie("oj_style", "original"));
    controller.setAttr("oj_fluid_width", controller.getCookie("oj_fluid_width"));
  }

}
