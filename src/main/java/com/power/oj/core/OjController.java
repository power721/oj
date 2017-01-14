package com.power.oj.core;

import com.jfinal.core.Controller;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.log.Logger;
import com.power.oj.contest.ContestService;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.service.OjService;
import com.power.oj.core.service.SessionService;
import com.power.oj.discussion.DiscussionService;
import com.power.oj.judge.JudgeService;
import com.power.oj.mail.MailService;
import com.power.oj.news.NewsService;
import com.power.oj.notice.NoticeService;
import com.power.oj.problem.ProblemService;
import com.power.oj.social.SocialService;
import com.power.oj.solution.SolutionService;
import com.power.oj.user.UserService;
import com.power.oj.util.freemarker.FreemarkerKit;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Controller provides some common methods.
 *
 * @author power
 */
public class OjController extends Controller {
    protected static ContestService contestService = ContestService.me();
    protected static DiscussionService discussionService = DiscussionService.me();
    protected static JudgeService judgeService = JudgeService.me();
    protected static MailService mailService = MailService.me();
    protected static NoticeService noticeService = NoticeService.me();
    protected static NewsService newsService = NewsService.me();
    protected static OjService ojService = OjService.me();
    protected static ProblemService problemService = ProblemService.me();
    protected static SessionService sessionService = SessionService.me();
    protected static SocialService socialService = SocialService.me();
    protected static SolutionService solutionService = SolutionService.me();
    protected static UserService userService = UserService.me();
    protected final Logger log = Logger.getLogger(getClass());

    /**
     * Redirect to url with flash message。
     *
     * @param url string of destination url.
     * @param msg message with content, type and title.
     * @see FlashMessageInterceptor
     */
    protected void redirect(String url, FlashMessage msg) {
        setFlashMessage(msg);
        redirect(url);
    }

    /**
     * Redirect to url with flash message。
     *
     * @param url             string of destination url.
     * @param withQueryString whether contains query string.
     * @param msg             message with content, type and title.
     * @see FlashMessageInterceptor
     */
    protected void redirect(String url, boolean withQueryString, FlashMessage msg) {
        setFlashMessage(msg);
        redirect(url, withQueryString);
    }

    /**
     * 渲染视图为字符串
     *
     * @param view 视图模版
     * @return 视图渲染后的字符串
     */
    protected String renderTpl(String view) {
        final Enumeration<String> attrs = getAttrNames();
        final Map<String, Object> root = new HashMap<String, Object>();
        while (attrs.hasMoreElements()) {
            String attrName = attrs.nextElement();
            root.put(attrName, getAttr(attrName));
        }
        return FreemarkerKit.processString(view, root);
    }

    /**
     * Set flash message in session.
     *
     * @param message message with content, type and title.
     */
    protected void setFlashMessage(FlashMessage message) {
        setSessionAttr(OjConstants.MSG, message.getContent());
        setSessionAttr(OjConstants.MSG_TYPE, message.getType());
        setSessionAttr(OjConstants.MSG_TITLE, message.getTitle());
    }

    /**
     * Set message in attr.
     *
     * @param message message with content, type and title.
     */
    protected void setAttrMessage(FlashMessage message) {
        setAttr(OjConstants.MSG, message.getContent());
        setAttr(OjConstants.MSG_TYPE, message.getType());
        setAttr(OjConstants.MSG_TITLE, message.getTitle());
    }

    /**
     * The the page title of the view.
     *
     * @param title string of the page title.
     */
    protected void setTitle(String title) {
        setAttr(OjConstants.PAGE_TITLE, title);
    }

    protected String getText(String key, Object... args) {
        Res resEn = I18n.use("en_US");
        return String.format(resEn.get(key), args);
    }

}
