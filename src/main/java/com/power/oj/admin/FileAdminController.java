package com.power.oj.admin;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RequiresPermissions("admin")
@RequiresAuthentication
public class FileAdminController extends AdminController {

    private static final AdminService adminService = AdminService.me();

    public void browse() {
        String dir = getPara(0);
        if (dir == null) {
            dir = "";
        }

        setAttr("dir", dir);
        setAttr("files", adminService.getJudgeFiles(dir));
    }

    public void view() {
        String dir = getPara(0);
        String name = getPara("name");
        if (dir == null) {
            dir = "";
        }

        setAttr("dir", dir);
        setAttr("name", name);
        setAttr("content" ,adminService.getFileContent(dir, name));
    }

    public void logs() {

    }

    public void log() {

    }

    public void download() {

    }

}
