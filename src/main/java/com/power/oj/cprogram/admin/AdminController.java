package com.power.oj.cprogram.admin;

import com.jfinal.core.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * Created by w7037 on 2017/6/14.
 */
@RequiresPermissions("teacher")
public class AdminController extends Controller{
    public void index() {
        renderText("hello~");
    }
}
