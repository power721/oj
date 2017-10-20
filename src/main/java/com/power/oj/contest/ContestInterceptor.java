package com.power.oj.contest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.contest.model.ContestSolutionModel;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;
import com.power.oj.util.CryptUtils;

public class ContestInterceptor implements Interceptor {

    private static final Logger LOGGER = Logger.getLogger(ContestInterceptor.class);
    private static final ContestService contestService = ContestService.me();
    private static final UserService userService = UserService.me();

    @Override
    public void intercept(Invocation ai) {
        Controller controller = ai.getController();
        Integer cid = 0;

        if (controller.getParaToInt("cid") != null)
            cid = controller.getParaToInt("cid");
        else if (controller.getParaToInt(0) != null)
            cid = controller.getParaToInt(0);

        ContestModel contestModel = contestService.getContest(cid);
        if (contestModel == null || contestModel.getType() > 4) {
            LOGGER.debug("cannot find contest model for cid " + cid);
            controller.renderError(404);
            return;
        }
        controller.setAttr("cid", cid);
        controller.setAttr("title", contestModel.getTitle());
        controller.setAttr("contest", contestModel);

        if (!ShiroKit.hasPermission("contest:view")) {
            if (!checkAccess(contestModel)) {
                LOGGER.debug("cannot access contest " + cid);
                controller.renderError(403);
                return;
            }

            if (!checkPassword(controller, contestModel)) {
                controller.render("password.html");
                return;
            }

            if (ai.getActionKey().equals("/contest/code")) {
                if (!checkSolutionAccess(controller)) {
                    controller.renderError(403);
                    return;
                }
            }

            if (contestModel.isPending()) {
                controller.render("pending.html");
                return;
            }
        }

        ai.invoke();
    }

    private boolean checkAccess(ContestModel contestModle) {
        Integer uid = userService.getCurrentUid();
        Integer cid = contestModle.getCid();
        if (contestModle.isStrictPrivate() || contestModle.isTest()) {
            if (uid == null || !contestService.isUserInContest(uid, cid)) {
                return false;
            }
        } else if (contestModle.isPrivate() && !contestModle.isFinished()) {
            if (uid == null || !contestService.isUserInContest(uid, cid)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkSolutionAccess(Controller controller) {
        Integer sid = controller.getParaToInt(1);
        boolean isAdmin = UserService.me().isAdmin();
        ContestSolutionModel solutionModel = SolutionService.me().findContestSolution(sid);
        Integer uid = solutionModel.getUid();
        Integer loginUid = UserService.me().getCurrentUid();

        return !(!uid.equals(loginUid) && !isAdmin);
    }

    private boolean checkPassword(Controller controller, ContestModel contestModle) {
        if (!contestModle.hasPassword()) {
            return true;
        }

        Integer cid = contestModle.getCid();
        String tokenName = "cid-" + String.format("%04d", cid);
        String token = controller.getSessionAttr(tokenName);
        if (token != null) {
            try {
                token = CryptUtils.decrypt(token, tokenName);
                LOGGER.info(token);
                if (contestModle.checkPassword(token)) {
                    return true;
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return false;
    }

}
