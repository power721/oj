package com.power.oj.cprogram.work;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.power.oj.cprogram.CProgramConstants;

/**
 * Created by w7037 on 2017/6/14.
 */
public class WorkInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation ai) {
        Controller controller = ai.getController();
        int wid = controller.getParaToInt(0, 0);
        if(wid == 0 || !isTrueType(wid)) {
            controller.renderError(404);
            return;
        }
        ai.invoke();
    }
    private static boolean isTrueType(int wid) {
        Integer type = Db.queryInt("select type from cprogram_work where id = ?", wid);
        if(type == null) return false;
        return type == CProgramConstants.work;
    }

}
