package com.power.oj.admin;

import com.power.oj.core.OjController;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RequiresPermissions("admin")
@RequiresAuthentication
public class AdminController extends OjController {
    private static final AdminService adminService = AdminService.me();

    public void index() {
        setAttrs(adminService.getSystemInfo());

        setTitle(getText("admin.index.title"));
    }

    public void info() {
        setAttrs(adminService.getOjInfo());
    }

}
