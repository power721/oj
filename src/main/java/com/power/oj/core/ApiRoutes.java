package com.power.oj.core;

import com.jfinal.config.Routes;
import com.power.oj.api.AdminApiController;
import com.power.oj.api.CommonApiController;
import com.power.oj.api.ContestApiController;
import com.power.oj.api.DiscussionApiController;
import com.power.oj.api.FriendApiController;
import com.power.oj.api.JudgeApiController;
import com.power.oj.api.MailApiController;
import com.power.oj.api.ProblemApiController;
import com.power.oj.api.UserApiController;
import com.power.oj.api.oauth.QQLoginApiController;
import com.power.oj.api.oauth.SinaLoginApiController;

public class ApiRoutes extends Routes {
    @Override
    public void config() {
        add("/api", CommonApiController.class, "/common/");
        add("/api/admin", AdminApiController.class, "/admin/");
        add("/api/contest", ContestApiController.class, "/contest/");
        add("/api/discuss", DiscussionApiController.class, "/discuss/");
        add("/api/friend", FriendApiController.class, "/user/");
        add("/api/mail", MailApiController.class, "/mail/");
        add("/api/oauth/qq", QQLoginApiController.class, "/user/");
        add("/api/oauth/sina", SinaLoginApiController.class, "/user/");
        add("/api/problem", ProblemApiController.class, "/problem/");
        add("/api/user", UserApiController.class, "/user/");
        add("/api/judge", JudgeApiController.class, "/solution/");
    }
}
