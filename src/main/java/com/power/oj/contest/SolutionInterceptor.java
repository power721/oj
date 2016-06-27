package com.power.oj.contest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;

public class SolutionInterceptor implements Interceptor {

    private static final Logger LOGGER = Logger.getLogger(SolutionInterceptor.class);

    @Override
    public void intercept(Invocation ai) {
        if (ai.getActionKey().equals("/contest/code")) {
            Controller controller = ai.getController();
            Integer cid = controller.getParaToInt(0);
            Integer sid = controller.getParaToInt(1);
            boolean isAdmin = UserService.me().isAdmin();
            ContestSolutionModel solutionModel = SolutionService.me().findContestSolution(sid);
            Integer uid = solutionModel.getUid();
            Integer loginUid = UserService.me().getCurrentUid();

            if (!uid.equals(loginUid) && !isAdmin) {
                LOGGER.debug("user " + loginUid + " cannot access contest solution " + cid + "-" + sid);
                controller.renderError(403);
                return;
            }
        }
        ai.invoke();
    }

}
