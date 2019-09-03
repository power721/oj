package com.power.oj.cprogram.model;

import com.jfinal.plugin.activerecord.Model;

public class CprogramInfoModel extends Model<CprogramInfoModel> {
    private static final long serialVersionUID = 1L;

    public static final CprogramInfoModel dao = new CprogramInfoModel();

    public static final String TABLE_NAME = "cprogram_info";
    public static final String CID = "cid";
    public static final String TYPE = "type";
    public static final String COMMIT = "commit";
    public static final String WEEK = "week";
    public static final String LECTURE = "lecture";

    public static final String TYPE_HOMEWORK = "HOMEWORK";
    public static final String TYPE_EXPERIMENT = "EXPERIMENT";
    public static final String TYPE_EXPERIMENT_EXAM = "EXPERIMENT_EXAM";
    public static final String TYPE_COURSE_EXAM = "COURSE_EXAM";

    /*
     * auto generated getter and setter
     */

    public Integer getCid() {
        return getInt(CID);
    }

    public CprogramInfoModel setCid(Integer value) {
        return set(CID, value);
    }

    public String getType() {
        return getStr(TYPE);
    }

    public CprogramInfoModel setType(String value) {
        return set(TYPE, value);
    }

    public String getCommit() {
        return getStr(COMMIT);
    }

    public CprogramInfoModel setCommit(String value) {
        return set(COMMIT, value);
    }

    public Integer getWeek() {
        return getInt(WEEK);
    }

    public CprogramInfoModel setWeek(Integer value) {
        return set(WEEK, value);
    }

    public Integer getLecture() {
        return getInt(LECTURE);
    }

    public CprogramInfoModel setLecture(Integer value) {
        return set(LECTURE, value);
    }

}
