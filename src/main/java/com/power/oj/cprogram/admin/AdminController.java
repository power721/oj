package com.power.oj.cprogram.admin;

import com.power.oj.core.OjController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.power.oj.admin.AdminService;


/**
 * Created by w7037 on 2017/6/14.
 */
@RequiresPermissions("teacher")
public class AdminController extends OjController{
    public void index() {
        setAttrs(AdminService.me().getSystemInfo());
    }
}
