package com.power.oj.cprogram.work;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.cprogram.CProgramConstants;

import java.util.List;

/**
 * Created by w7037 on 2017/6/14.
 */
public class WorkService {
    static Page<Record> GetWorksList(int pageNumber, int pageSize) {
        String sql =
                "SELECT " +
                    "cprogram_work.id, " +
                    "cprogram_work.title, " +
                    "cprogram_work.time, " +
                    "cprogram_work.startTime, " +
                    "cprogram_work.endTime, " +
                    "user.nick";
        String sqlExe =
                "FROM " +
                    "cprogram_work " +
                "INNER JOIN user ON " +
                    "cprogram_work.uid = user.uid " +
                "WHERE " +
                    "cprogram_work.type = ? " +
                    "ORDER BY cprogram_work.startTime DESC";
        Page<Record> page = Db.paginate(pageNumber, pageSize, sql, sqlExe, CProgramConstants.work);
        return page;
    }
    static Page<Record> GetOwnWorkList(int uid, int pageNumber, int pageSize) {
        String sql =
                "SELECT " +
                        "cprogram_work.id, " +
                        "cprogram_work.title, " +
                        "cprogram_work.time, " +
                        "cprogram_work.startTime, " +
                        "cprogram_work.endTime, " +
                        "user.nick ";
        String sqlExe =
                        "FROM " +
                        "cprogram_work " +
                        "INNER JOIN user ON " +
                        "cprogram_work.uid = user.uid " +
                        "WHERE " +
                        "cprogram_work.type =  ? " +
                        "AND cprogram_work.uid = ? " +
                        "ORDER BY cprogram_work.startTime DESC";
        Page<Record> page = Db.paginate(pageNumber, pageSize, sql, sqlExe, CProgramConstants.work, uid);
        return page;
    }
}
