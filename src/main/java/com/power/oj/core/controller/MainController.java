package com.power.oj.core.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.contest.model.ContestModel;
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
        if (isSwust()) {
            int pageNumber = 1;
            /*
            int pageSize = OjConfig.noticePageSize;
            setAttr("noticeList", noticeService.getNoticePage(pageNumber, pageSize));
            */
            int pageSize = OjConfig.contestPageSize;

            setAttr("pendingContests", contestService.getContestList(pageNumber, pageSize, -1, ContestModel.PENDING));
            setAttr("contestList", contestService.getContestList(pageNumber, pageSize, -1, ContestModel.RUNNING));
            
            pageSize = OjConfig.newsPageSize;
            setAttr("newsList", newsService.getNewsListPage(pageNumber, pageSize));

            setAttr("newsAndNotimeList", MainService.getNewsAndNoticeList(OjConfig.newsPageSize, OjConfig.noticePageSize));
            setAttr("isSwust", true);
            setTitle(getText("page.index.title"));

            render("swust.html");
        } else {
            setAttr("problemsNumber", problemService.getProblemsNumber());

            render("index.html");
        }
    }

    private boolean isSwust() {
        return getPara("swust") != null || getRequest().getRequestURL().toString().contains("swust");
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
        if (ojService.addResource(model)) {
            redirect("/downloads");
        } else {
            redirect("/downloads", new ErrorFlashMessage("Add resource failed!"));
        }
    }

    @Before(POST.class)
    @RequiresPermissions("admin")
    public void updateResource() {
        ResourceModel model = getModel(ResourceModel.class, "resource");
        if (ojService.updateResource(model)) {
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
