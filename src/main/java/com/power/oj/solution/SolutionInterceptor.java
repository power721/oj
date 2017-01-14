package com.power.oj.solution;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class SolutionInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation ai) {
        Controller controller = ai.getController();
        Integer sid = controller.getParaToInt(0);
        if (!SolutionService.me().canAccessSolution(sid)) {
            controller.renderError(403);
            return;
        }
        ai.invoke();
    }

}
