package com.power.oj.service;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.power.oj.problem.ProblemService;

// TODO not implemented
public class VisitCountService extends TimerTask
{
  public static final byte problemViewCount = 0x01;
  public static final byte userViewCount = 0x02;
  public static final byte contestViewCount = 0x03;
  public static final byte contestProblemViewCount = 0x04;
  public static final byte postViewCount = 0x05;
  public static final byte noticeViewCount = 0x06;
  public static final byte articleViewCount = 0x07;
  
  private static final ProblemService problemService = ProblemService.me();
  
  private final static Logger log = Logger.getLogger(VisitCountService.class);
  private static boolean start = false;
  private static VisitCountService daemon;
  private static Timer click_timer;
  private final static long INTERVAL = 15 * 60 * 1000;

  /**
   * 支持统计的对象类型
   */
  private final static byte[] TYPES = new byte[]
  { problemViewCount, userViewCount, contestViewCount, contestProblemViewCount, postViewCount, noticeViewCount };

  // 内存队列
  @SuppressWarnings("serial")
  private final static ConcurrentHashMap<Byte, ConcurrentHashMap<Integer, Integer>> queues = new ConcurrentHashMap<Byte, ConcurrentHashMap<Integer, Integer>>() {
    {
      for (byte type : TYPES)
        put(type, new ConcurrentHashMap<Integer, Integer>());
    }
  };

  /**
   * 记录访问统计
   * 
   * @param type
   * @param obj_id
   */
  public static void record(byte type, Integer obj_id)
  {
    ConcurrentHashMap<Integer, Integer> queue = queues.get(type);
    if (queue != null)
    {
      Integer nCount = queue.get(obj_id);
      if (nCount == null)
      {
        switch(type)
        {
          case problemViewCount: nCount = problemService.getViewCount(obj_id);break;
          default: nCount = 1;
        }
      }
      else
      {
        nCount += 1;
      }
      
      queue.put(obj_id, nCount.intValue());
    }
  }
  
  public static Integer get(byte type, Integer obj_id)
  {
    ConcurrentHashMap<Integer, Integer> queue = queues.get(type);
    if (queue != null)
    {
      Integer nCount = queue.get(obj_id);
      if (nCount == null)
      {
        switch(type)
        {
          case problemViewCount: nCount = problemService.getViewCount(obj_id);break;
        }
      }
      return nCount;
    }
    
    return 0;
  }

  /**
   * 启动统计数据写入定时器
   * 
   * @param ctx
   */
  public static void start()
  {
    if (!start)
    {
      daemon = new VisitCountService();
      click_timer = new Timer("VisitCountService", true);
      click_timer.schedule(daemon, INTERVAL, INTERVAL);// 运行间隔1分钟
      start = true;
    }
    log.info("VisitCountService started.");
  }

  /**
   * 释放Service
   */
  public static void destroy()
  {
    if (start)
    {
      click_timer.cancel();
      start = false;
    }
    log.info("VisitCountService stopped.");
  }

  @Override
  public void run()
  {
    for (byte type : TYPES)
    {
      ConcurrentHashMap<Integer, Integer> queue = queues.remove(type);
      queues.put(type, new ConcurrentHashMap<Integer, Integer>());
      try
      {
        _flush(type, queue);
      } catch (Throwable t)
      {
        log.fatal("Failed to flush click stat data.", t);
        // 此处发送异常报告
      } finally
      {
        // 此处关闭数据库连接
      }
    }
  }

  @Override
  public boolean cancel()
  {
    boolean b = super.cancel();
    // 写回剩余数据，Tomcat停止时不会丢失数据
    this.run();
    return b;
  }

  /**
   * 写访问统计数据到数据库
   * 
   * @param type
   * @param queue
   */
  private void _flush(byte type, ConcurrentHashMap<Integer, Integer> queue)
  {
    if (queue.size() == 0)
      return;
    switch (type)
    {
      // 数据写入数据库....
      case problemViewCount:
      for (Integer pid : queue.keySet())
      {
        problemService.setViewCount(pid, queue.get(pid));
      }
      break;
    }
    System.out.printf("Flush to database: type=%d\n", type);
  }

  /**
   * 测试
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception
  {
    start();
    for (int i = 0; i < 10; i++)
      new Timer("OfferTask_" + (i + 1), false).schedule(new TimerTask() {
        private Random rnd = new Random(System.currentTimeMillis());

        @Override
        public void run()
        {
          record(TYPES[rnd.nextInt(TYPES.length)], rnd.nextInt(10));
        }
      }, 0, 1000);
  }
}
