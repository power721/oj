package com.power.oj.admin;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.upload.UploadFile;
import com.power.oj.core.OjConfig;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.io.File;
import java.io.IOException;

@RequiresPermissions("admin")
@RequiresAuthentication
public class FileAdminController extends AdminController {

    private static final AdminService adminService = AdminService.me();

    public void view() {
        String dir = getPara("dir");
        String name = getPara("name");
        String type = getPara("type");
        if (dir == null) {
            dir = "";
        }

        setAttr("dir", dir);
        setAttr("name", name);
        setAttr("type", type);
        if ("image".equals(type)) {
            setAttr("path", adminService.getImagePath(dir, name));
        } else {
            setAttr("content", adminService.getFileContent(dir, name, type));
        }
    }

    public void logs() {
        setAttr("logs", adminService.getLogs());
    }

    public void log() {
        String dir = getPara("dir");
        String name = getPara("name");
        if (dir == null) {
            dir = "";
        }

        setAttr("dir", dir);
        setAttr("name", name);
    }

    public void judge() {
        String dir = getPara("dir");
        if (dir == null) {
            dir = "";
        }

        setAttr("dir", dir);
        setAttr("files", adminService.getJudgeFiles(dir));
    }

    public void resources() {
        String dir = getPara("dir");
        if (dir == null) {
            dir = "";
        }

        setAttr("dir", dir);
        setAttr("path", OjConfig.downloadPath);
        setAttr("resources", adminService.getResources(dir));
    }

    public void images() {
        String dir = getPara("dir");
        if (dir == null) {
            dir = "";
        }

        setAttr("dir", dir);
        setAttr("path", OjConfig.uploadPath);
        setAttr("baseDir", OjConfig.uploadPath.replace(OjConfig.webRootPath, ""));
        setAttr("images", adminService.getImages(dir));
    }

    public void download() {
        String dir = getPara("dir");
        String name = getPara("name");
        String type = getPara("type");
        if (dir == null) {
            dir = "";
        }

        File file = adminService.downloadFile(dir, name, type);
        if (file == null) {
            renderError(404);
        } else {
            renderFile(file);
        }
    }

    public void delete() {
        String dir = getPara("dir");
        String name = getPara("name");
        String type = getPara("type");
        if (dir == null) {
            dir = "";
        }

        renderJson("result", adminService.deleteFile(dir, name, type));
    }

    @Before(POST.class)
    public void upload() {
        UploadFile uploadFile = getFile("file", "", 100 * 1024 * 1024, "UTF-8");
        File file = uploadFile.getFile();
        String filename = getPara("name");

        try {
            filename = adminService.uploadFile(filename, file);
        } catch (IOException e) {
            log.error("upload file failed!", e);

            renderJson("error", "Move file to download directory failed.");
            return;
        }
        renderJson(filename);
    }

    @Before(POST.class)
    public void uploadImage() {
        UploadFile uploadFile = getFile("file", "", 100 * 1024 * 1024, "UTF-8");
        File file = uploadFile.getFile();
        String dirName = getPara("dir");
        String filename = getPara("name");

        try {
            filename = adminService.uploadImage(dirName, filename, file);
        } catch (IOException e) {
            log.error("upload image failed!", e);

            renderJson("error", "Move file to upload directory failed.");
            return;
        }
        renderJson(filename);
    }

}
