package com.power.oj.news;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.power.oj.core.OjConfig;
import com.power.oj.notice.NoticeModel;
import com.power.oj.user.UserService;

public class NewsService
{
  private static final NewsModel dao = NewsModel.dao;
  private static final NewsService me = new NewsService();
  private static final UserService userService = UserService.me();
  
  private NewsService() {}
  public static NewsService me()
  {
    return me;
  }
  
  public Page<NewsModel> getNewsListPage(int pageNumber, int pageSize)
  {
	  String sql = "SELECT * ";
	  String from = "FROM news ORDER BY id DESC";
	  
	  return  dao.paginate(pageNumber, pageSize, sql, from);
  }
  
  public NewsModel getNews(Integer id)
  {
    return dao.findFirst("SELECT * ,FROM_UNIXTIME(time, '%Y-%m-%d %H:%i:%s') AS publishTime FROM news WHERE id = ?", id);
  }
  
  public boolean saveNews(NewsModel newsModel)
  {
    NewsModel newNews = new NewsModel();
    
    newNews.setTitle(newsModel.getTitle());
    newNews.setTime(newsModel.getTime());
    newNews.setContent(newsModel.getContent());
    newNews.setImage(newsModel.getImage());
    newNews.setAuthor(newsModel.getAuthor());
    return newNews.save();
  }
  
  public boolean updateNews(NewsModel newsModel)
  {
    Integer id = newsModel.getId();
    NewsModel newNews = dao.findById(id);

    newNews.setId(id);
    newNews.setTitle(newsModel.getTitle());
    newNews.setTime(newsModel.getTime());
    newNews.setContent(newsModel.getContent());
    newNews.setAuthor(newsModel.getAuthor());
    newNews.setImage(newsModel.getImage());
    return newNews.update();
  }
  
  public boolean deleteNews(Integer id){
	  return dao.deleteById(id);
  }
}
