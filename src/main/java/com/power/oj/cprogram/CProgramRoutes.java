package com.power.oj.cprogram;

/**
 * Created by w703710691d on 2017/6/14.
 */
import com.jfinal.config.Routes;
import com.power.oj.cprogram.admin.AdminController;
import com.power.oj.cprogram.course_exam.CourseExameController;
import com.power.oj.cprogram.experiment.ExperimentController;
import com.power.oj.cprogram.experiment_exam.ExperimentExamController;
import com.power.oj.cprogram.work.WorkController;

public class CProgramRoutes extends Routes {
    @Override
    public void config() {
        add("/cprogram", CProgramMainController.class);
        add("/cprogram/work", WorkController.class);
        add("/cprogram/admin", AdminController.class);
        add("/cprogram/experiment", ExperimentController.class);
        add("/cprogram/experiment_exam", ExperimentExamController.class);
        add("/cprogram/course_exam", CourseExameController.class);
    }
}