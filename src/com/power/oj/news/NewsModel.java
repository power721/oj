package com.power.oj.news;

import com.jfinal.plugin.activerecord.Model;
import com.power.oj.notice.NoticeModel;

public class NewsModel extends Model<NewsModel> {
	private static final long serialVersionUID = 1L;

	public static final NewsModel dao = new NewsModel();

	public static final String TABLE_NAME = "news";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String TIME = "time";
	public static final String AUTHOR = "author";
	public static final String IMAGE = "image";
	public static final String VIEW = "view";
	
	public Integer getId()
	  {
	    return getInt(ID);
	  }
	  
	  public NewsModel setId(Integer value)
	  {
	    return set(ID, value);
	  }
	  
	  public String getTitle()
	  {
	    return getStr(TITLE);
	  }
	  
	  public NewsModel setTitle(String value)
	  {
	    return set(TITLE, value);
	  }
	  
	  public Integer getTime()
	  {
	    return getInt(TIME);
	  }
	  
	  public NewsModel setTime(Integer value)
	  {
	    return set(TIME, value);
	  }
	  
	  public String getContent()
	  {
	    return getStr(CONTENT);
	  }
	  
	  public NewsModel setContent(String value)
	  {
	    return set(CONTENT, value);
	  }
	  
	  public String getAuthor()
	  {
		  return getStr(AUTHOR);
	  }
	  
	  public NewsModel setAuthor(String value)
	  {
		  return set(AUTHOR, value);
	  }
	  
	  public Integer getView()
	  {
	    return getInt(VIEW);
	  }
	  
	  public NewsModel setView(Integer value)
	  {
	    return set(VIEW, value);
	  }
	  
	  public String getImage()
	  {
		  return getStr(IMAGE);
	  }
	  
	  public NewsModel setImage(String value)
	  {
		  return set(IMAGE, value);
	  }
}
