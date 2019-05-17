package com.power.oj.cprogram.admin;

import com.jfinal.config.Routes;
import com.power.oj.cprogram.admin.experiment.ExprimentController;
import com.power.oj.cprogram.admin.homework.HomeworkController;

public class AdminRoutes extends Routes {
    @Override
    public void config() {
        add("/cprogram/admin/homework", HomeworkController.class);
        add("/cprogram/admin", AdminController.class);
        add("/cprogram/admin/experiment", ExprimentController.class);
    }
}
