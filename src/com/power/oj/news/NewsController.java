package com.power.oj.news;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.core.bean.FlashMessage;
import com.power.oj.core.bean.MessageType;
import com.power.oj.notice.NoticeModel;

public class NewsController extends OjController {

	private static final NewsModel dao = NewsModel.dao;

	public void index() {
		int pageNumber = 1;
		int pageSize = OjConfig.noticePageSize;
		setAttr("noticeList", noticeService.getNoticePage(pageNumber, pageSize));
		setAttr("problemsNumber", problemService.getProblemsNumber());
		setTitle(getText("page.index.title"));
		Integer type = getParaToInt("type", -1);
		Integer status = getParaToInt("status", -1);
		pageSize = OjConfig.contestPageSize;
		setAttr("contestList", contestService.getContestList(pageNumber,
				pageSize, type, status));
		pageSize = OjConfig.newsPageSize;
		pageNumber = getParaToInt(0, 1);
		setAttr("newsList", newsService.getNewsListPage(pageNumber, pageSize));
	}

	public void show() {
		int pageSize = 20;
		int pageNumber = getParaToInt(0, 1);
		setAttr("newsList", newsService.getNewsListPage(pageNumber, pageSize));
	}

	public void each() {
		int id = getParaToInt(0);
		setAttr("news", newsService.getNews(id));
	}

	@RequiresPermissions("news:add")
	public void add() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long ctime = OjConfig.startInterceptorTime;

		setAttr("publishTime", sdf.format(new Date(ctime)));

		setTitle("Add News");
	}

	@Before(POST.class)
	@RequiresPermissions("news:add")
	public void save() {
		String author = getPara("author");
		String iconPath = getPara("iconPath");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		NewsModel newsModel = getModel(NewsModel.class, "news");
		try {
			Date newDate = new Date();
			String publishTime = sdf.format(newDate);
			newsModel.setAuthor(author);
			newsModel.setIcon(iconPath);
			newsModel.setTime((int) (sdf.parse(publishTime).getTime() / 1000));
		} catch (ParseException e) {
			if (OjConfig.isDevMode())
				e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}

		if (newsService.saveNews(newsModel)) {
			setFlashMessage(new FlashMessage("Add news successful!"));
		} else {
			setFlashMessage(new FlashMessage("Add news failed!",
					MessageType.ERROR, getText("message.error.title")));
		}

		redirect("/news/show");
	}

	@RequiresPermissions("news:edit")
	public void edit() {
		Integer id = getParaToInt(0);

		setAttr("news", newsService.getNews(id));
	}

	@Before(POST.class)
	@RequiresPermissions("news:edit")
	public void update() {
		String author = getPara("author");
		String iconPath = getPara("iconPath");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		NewsModel newsModel = getModel(NewsModel.class, "news");
		try {
			Date newDate = new Date();
			String publishTime = sdf.format(newDate);
			newsModel.setAuthor(author);
			newsModel.setIcon(iconPath);
			newsModel.setTime((int) (sdf.parse(publishTime).getTime() / 1000));
		} catch (ParseException e) {
			if (OjConfig.isDevMode())
				e.printStackTrace();
			log.error(e.getLocalizedMessage());
		}

		if (newsService.updateNews(newsModel)) {
			setFlashMessage(new FlashMessage("Update news successful!"));
		} else {
			setFlashMessage(new FlashMessage("Update news failed!",
					MessageType.ERROR, getText("message.error.title")));
		}

		redirect("/news/each/" + newsModel.getId());
	}
	
	@RequiresPermissions("news:show")
	public void delect(){
		Integer id = getParaToInt("id");
		if(newsService.delectNews(id)){
			setFlashMessage(new FlashMessage("Delect news successful!"));
		} else {
			setFlashMessage(new FlashMessage("Delect news failed!",
					MessageType.ERROR, getText("message.error.title")));
		}
		redirect("/news/show");
	}
}
