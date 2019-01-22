package com.power.oj.core.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.power.oj.core.OjConfig;

public class OjVariableInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation ai) {
        if(isSwust(ai.getController())) {
            ai.getController().setAttr("isSwust", true);
        }
        ai.invoke();

        Controller controller = ai.getController();
        controller.setAttr("enable_qq_login", OjConfig.getBoolean("enable_qq_login"));
        controller.setAttr("enable_sina_login", OjConfig.getBoolean("enable_sina_login"));
        controller.setAttr("dev", OjConfig.isDevMode());
        controller.setAttr("gaAccount", OjConfig.getString("gaAccount"));
        controller.setAttr("domaiNname", OjConfig.getString("domaiNname"));
        controller.setAttr("cdn", OjConfig.getString("cdn"));
        controller.setAttr("icpRecord", OjConfig.getString("icpRecord"));
        controller.setAttr("oj_style", controller.getCookie("oj_style", "original"));
        controller.setAttr("oj_fluid_width", controller.getCookie("oj_fluid_width"));
    }

    private boolean isSwust(Controller con) {
        return con.getPara("swust") != null || con.getRequest().getRequestURL().toString().contains("swust.edu");
    }
}
