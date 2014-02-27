package com.power.oj.notice;

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

public class NoticeController extends OjController
{
  private static final NoticeService noticeService = NoticeService.me();
  
  public void index()
  {
    int pageNumber = getParaToInt(0, 1);
    int pageSize = getParaToInt(1, OjConfig.noticePageSize);
    
    setAttr("noticeList", noticeService.getNoticePage(pageNumber, pageSize));
    setAttr("pageSize", OjConfig.noticePageSize);
    
    setTitle("Notice List");
  }
  
  public void show()
  {
    Integer id = getParaToInt(0);
    
    setAttr("notice", noticeService.getNotice(id));
    
    // TODO add view count
  }
  
  @RequiresPermissions("notice:add")
  public void add()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    long ctime = OjConfig.startInterceptorTime;
    
    setAttr("start_time", sdf.format(new Date(ctime)));
    setAttr("end_time", sdf.format(new Date(ctime + 18000000)));
    
    setTitle("Add Notice");
  }
  
  @Before(POST.class)
  @RequiresPermissions("notice:add")
  public void save()
  {
    String start_time = getPara("start_time");
    String end_time = getPara("end_time");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    NoticeModel noticeModel = getModel(NoticeModel.class, "notice"); // will lost start_time, end_time
    
    try
    {
      noticeModel.set("start_time", sdf.parse(start_time).getTime() / 1000);
      noticeModel.set("end_time", sdf.parse(end_time).getTime() / 1000);
    } catch (ParseException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }
    
    if (noticeService.saveNotice(noticeModel))
    {
      setFlashMessage(new FlashMessage("Add notice successful!"));
    }
    else
    {
      setFlashMessage(new FlashMessage("Add notice failed!", MessageType.ERROR, getText("message.error.title")));
    }
    
    redirect("/notice");
  }
  
  @RequiresPermissions("notice:edit")
  public void edit()
  {
    Integer id = getParaToInt(0);
    
    setAttr("notice", noticeService.getNotice(id));
  }
  
  @Before(POST.class)
  @RequiresPermissions("notice:edit")
  public void update()
  {
    String start_time = getPara("start_time");
    String end_time = getPara("end_time");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    NoticeModel noticeModel = getModel(NoticeModel.class, "notice"); // will lost start_time, end_time
    
    try
    {
      noticeModel.set("start_time", sdf.parse(start_time).getTime() / 1000);
      noticeModel.set("end_time", sdf.parse(end_time).getTime() / 1000);
    } catch (ParseException e)
    {
      if (OjConfig.getDevMode())
        e.printStackTrace();
      log.error(e.getLocalizedMessage());
    }
    
    if (noticeService.updateNotice(noticeModel))
    {
      setFlashMessage(new FlashMessage("Update notice successful!"));
    }
    else
    {
      setFlashMessage(new FlashMessage("Update notice failed!", MessageType.ERROR, getText("message.error.title")));
    }
    
    redirect("/notice/show/" + noticeModel.get("id"));
  }
  
  public void delete()
  {
    
  }
  
}
