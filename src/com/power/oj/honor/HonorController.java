package com.power.oj.honor;

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
import com.power.oj.news.NewsModel;
import com.power.oj.notice.NoticeModel;

public class HonorController extends OjController {
	
	private static final HonorModel dao = HonorModel.dao;
	public void index() {
		setTitle("Honor Lists");
		setAttr("proList", dao.find("SELECT * FROM honors WHERE LEVEL=?", "四川省大学生程序设计竞赛"));
		setAttr("asiaList", dao.find("SELECT * FROM honors WHERE LEVEL=?","ACM-ICPC亚洲区预选赛"));
	}
	
	public void add(){
		setTitle("Add a Team Honor");
	}
	
	@Before(POST.class)
	@RequiresPermissions("honor:add")
	public void save(){
		HonorModel honorModel = getModel(HonorModel.class, "honor");
		HonorModel newHonor = new HonorModel();
	    
	    newHonor.setLevel(honorModel.getLevel());
	    newHonor.setContest(honorModel.getContest());
	    newHonor.setTeam(honorModel.getTeam());
	    newHonor.setPlayer(honorModel.getPlayer());
	    newHonor.setPrize(honorModel.getPrize());
	    if (newHonor.save())
	    {
	      setFlashMessage(new FlashMessage("Add honor successful!"));
	    }
	    else
	    {
	      setFlashMessage(new FlashMessage("Add honor failed!", MessageType.ERROR, getText("message.error.title")));
	    }
	    redirect("/honor");
	}
	
}
