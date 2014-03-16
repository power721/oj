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
    
    setAttr("startTime", sdf.format(new Date(ctime)));
    setAttr("endTime", sdf.format(new Date(ctime + 18000000)));
    
    setTitle("Add Notice");
  }
  
  @Before(POST.class)
  @RequiresPermissions("notice:add")
  public void save()
  {
    String startTime = getPara("startTime");
    String endTime = getPara("endTime");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    NoticeModel noticeModel = getModel(NoticeModel.class, "notice"); // will lost start_time, end_time
    
    try
    {
      noticeModel.setStartTime((int) (sdf.parse(startTime).getTime() / 1000));
      noticeModel.setEndTime((int) (sdf.parse(endTime).getTime() / 1000));
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
    String startTime = getPara("startTime");
    String endTime = getPara("endTime");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    NoticeModel noticeModel = getModel(NoticeModel.class, "notice"); // will lost start_time, end_time
    
    try
    {
      noticeModel.setStartTime((int) (sdf.parse(startTime).getTime() / 1000));
      noticeModel.setEndTime((int) (sdf.parse(endTime).getTime() / 1000));
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
    
    redirect("/notice/show/" + noticeModel.getId());
  }
  
  public void delete()
  {
    
  }
  
}
