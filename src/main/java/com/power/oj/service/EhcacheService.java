package com.power.oj.service;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EhcacheService extends TimerTask {
    private static final Logger log = Logger.getLogger(EhcacheService.class);
    private static final long INTERVAL = 5 * 60 * 1000; // 5 minutes
    private static boolean start = false;
    private static EhcacheService daemon;
    private static Timer clickTimer;

    public static void start() {
        if (!start) {
            daemon = new EhcacheService();
            clickTimer = new Timer("EhcacheService", true);
            clickTimer.schedule(daemon, INTERVAL, INTERVAL);
            start = true;
        }
        log.info("EhcacheService started.");
    }

    public static void destroy() {
        if (start) {
            clickTimer.cancel();
            start = false;
        }
        log.info("EhcacheService stopped.");
    }

    @Override
    public void run() {
        flush("problem");
    }

    @Override
    public boolean cancel() {
        boolean b = super.cancel();
        this.run();
        return b;
    }

    @SuppressWarnings("rawtypes")
    private int flush(String cacheName) {
        int count = 0;
        List keys = CacheKit.getKeys(cacheName);

        for (Object key : keys) {
            Model model = CacheKit.get(cacheName, key);
            if (model.update()) {
                count++;
            }
        }

        log.info(String.format("update %d models in cache %s.", count, cacheName));
        return count;
    }

}
