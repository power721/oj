package com.power.oj.core.controller;

import com.jfinal.aop.Clear;
import com.jfinal.ext.render.CaptchaRender;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;

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
        setTitle(getText("page.downloads.title"));
    }

    public void tag() {
        setAttr("tagList", ojService.tagList());
        setTitle(getText("page.tag.title"));
    }

    @Clear
    public void captcha() {
        CaptchaRender img = new CaptchaRender(OjConstants.RANDOM_CODE_KEY);
        render(img);
    }

}
