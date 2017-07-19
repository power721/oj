package com.power.oj.core.controller;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainService {
    static public List<Record> getNewsAndNoticeList(int newsPageNumber, int noticePageNumber) {
        String sql = "select " +
                "news.title, " +
                "news.content, " +
                "news.time, " +
                "news.author " +
                "from news " +
                "order by news.time desc " +
                "limit 0," + newsPageNumber;
        List<Record> list = Db.find(sql);
        sql = "select " +
                "notice.title, " +
                "notice.content, " +
                "startTime as time, " +
                "user.nick as author " +
                "from notice " +
                "inner join user on notice.uid = user.uid " +
                "where " +
                "notice.status = 1 " +
                "and notice.startTime <= ? and ? <= notice.endTime " +
                "order by notice.startTime desc " +
                "limit 0, ?";
        List<Object> parase = new ArrayList<>();
        parase.add(OjConfig.timeStamp);
        parase.add(OjConfig.timeStamp);
        parase.add(noticePageNumber);
        List<Record> noticelist = Db.find(sql, parase.toArray());
        list.addAll(noticelist);
        list.sort(new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return o2.getInt("time").compareTo(o1.getInt("time"));
            }
        });
        return list;
    }
}
