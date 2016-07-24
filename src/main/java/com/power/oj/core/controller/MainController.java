package com.power.oj.core.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.ErrorFlashMessage;
import com.power.oj.core.model.ResourceModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.io.File;

/**
 * The controller for some common pages.
 *
 * @author power
 */
public class MainController extends OjController {

    public void index() {
        setAttr("problemsNumber", problemService.getProblemsNumber());
        setTitle(getText("page.index.title"));
        try {
            int pageNumber = getParaToInt(0, 1);
            int pageSize = getParaToInt(1, OjConfig.noticePageSize);
            Integer type = getParaToInt("type", -1);
            Integer status = getParaToInt("status", 0);
            setAttr("noticeList", noticeService.getNoticePage(pageNumber, pageSize));
            pageSize = OjConfig.contestPageSize;
            setAttr("contestList", contestService.getContestList(pageNumber, pageSize, type, status));
            pageSize = OjConfig.newsPageSize;
            setAttr("newsList", newsService.getNewsListPage(pageNumber, pageSize));
        } catch (NumberFormatException e) {
            redirect301("/");
        }

        if (getPara("swust") != null || getRequest().getRequestURL().toString().contains("swust")) {
            render("swust.html");
        } else {
            render("index.html");
        }
    }

    public void about() {
        setTitle(getText("page.about.title"));
    }

    public void contact() {
        setTitle(getText("page.contact.title"));
    }

    public void changelog() {
        setTitle(getText("page.changelog.title"));
    }

    public void faq() {
        setTitle(getText("page.faq.title"));
    }

    public void downloads() {
        setAttr("downloadPath", OjConfig.downloadPath);
        setAttr("resources", ojService.resourceList());
        setTitle(getText("page.downloads.title"));
    }

    public void tag() {
        setAttr("tagList", ojService.tagList());
        setTitle(getText("page.tag.title"));
    }

    public void resource() {
        String name = getPara("name");
        Integer id = getParaToInt("id");
        File file = ojService.getResourceFile(name, id);
        if (file != null) {
            renderFile(file);
        } else {
            redirect("/downloads", new ErrorFlashMessage("Cannot find the resource. Please contact the admin."));
        }
    }

    @RequiresPermissions("admin")
    public void editResource() {
        Integer id = getParaToInt(0);
        setAttr("downloadPath", OjConfig.downloadPath);
        setAttr("resource", ojService.getResource(id));
    }

    @Before(POST.class)
    @RequiresPermissions("admin")
    public void addResource() {
        ResourceModel model = getModel(ResourceModel.class, "resource");
        if(ojService.addResource(model)) {
            redirect("/downloads");
        } else {
            redirect("/downloads", new ErrorFlashMessage("Add resource failed!"));
        }
    }

    @Before(POST.class)
    @RequiresPermissions("admin")
    public void updateResource() {
        ResourceModel model = getModel(ResourceModel.class, "resource");
        if(ojService.updateResource(model)) {
            redirect("/downloads");
        } else {
            redirect("/downloads", new ErrorFlashMessage("Update resource failed!"));
        }
    }

    @Clear
    public void captcha() {
        renderCaptcha();
    }

}
