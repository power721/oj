package com.power.oj.cprogram;

/**
 * Created by w703710691d on 2017/6/14.
 */
import com.jfinal.config.Routes;


public class CProgramRoutes extends Routes {
    @Override
    public void config() {
        add("/cprogram", CProgramMainController.class);
    }
}