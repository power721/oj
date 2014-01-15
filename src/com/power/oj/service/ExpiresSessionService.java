package com.power.oj.service;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.power.oj.core.OjConstants;
import com.power.oj.core.model.SessionModel;

public class ExpiresSessionService extends TimerTask
{

  private final static Logger log = Logger.getLogger(ExpiresSessionService.class);
  private static boolean start = false;
  private static ExpiresSessionService daemon;
  private static Timer click_timer;
  private final static long INTERVAL = OjConstants.SESSION_EXPIRES_TIME;

  @Override
  public void run()
  {
    int numOfExpiresSession = SessionModel.dao.expiresSession();
    log.info(new StringBuilder(3).append("ExpiresSessionService executed: ").append(numOfExpiresSession).append(" sessions deleted.").toString());
  }

  public static void start()
  {
    if (!start)
    {
      daemon = new ExpiresSessionService();
      click_timer = new Timer("ExpiresSessionService", true);
      click_timer.schedule(daemon, INTERVAL, INTERVAL);
      start = true;
    }
    log.info("ExpiresSessionService started.");
  }

}
