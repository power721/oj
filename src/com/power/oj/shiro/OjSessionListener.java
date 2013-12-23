package com.power.oj.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import com.jfinal.log.Logger;
import com.power.oj.core.OjConfig;
import com.power.oj.core.model.SessionModel;
import com.power.oj.core.service.SessionService;

public class OjSessionListener implements SessionListener
{

  private static final Logger log = Logger.getLogger(OjSessionListener.class);
  
  @Override
  public void onExpiration(Session session)
  {
    String id = (String) session.getId();
    
    SessionService.removeModel(id);
    SessionService.removeSession(id);
    
    SessionModel.dao.deleteSession(id);
  }

  @Override
  public void onStart(Session session)
  {
    String id = (String) session.getId();
    
    SessionModel sessionModel = new SessionModel().set("session_id", id).set("ip_address", session.getHost());//.set("user_agent", agent);
    sessionModel.set("ctime", OjConfig.timeStamp).set("last_activity", OjConfig.timeStamp).set("session_expires", OjConfig.timeStamp + session.getTimeout());
    sessionModel.save();
    
    SessionService.putModel(id, sessionModel);
    SessionService.putSession(id, session);
    
    log.info(session.toString());
  }

  @Override
  public void onStop(Session session)
  {
    String id = (String) session.getId();
    
    SessionService.removeModel(id);
    SessionService.removeSession(id);
    
    SessionModel.dao.deleteSession(id);

    log.info(session.toString());
    log.info(session.getStartTimestamp().toString());
  }

}
