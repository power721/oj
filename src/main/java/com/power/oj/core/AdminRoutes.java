package com.power.oj.core;

import com.jfinal.config.Routes;
import com.power.oj.admin.AdminController;
import com.power.oj.admin.ContestAdminController;
import com.power.oj.admin.FileAdminController;
import com.power.oj.admin.ProblemAdminController;
import com.power.oj.admin.UserAdminController;

public class AdminRoutes extends Routes {
    @Override
    public void config() {
        add("/admin", AdminController.class);
        add("/admin/contest", ContestAdminController.class);
        add("/admin/problem", ProblemAdminController.class);
        add("/admin/user", UserAdminController.class);
        add("/admin/file", FileAdminController.class);
    }
}
