package com.power.oj.cprogram.model;

import com.jfinal.plugin.activerecord.Model;

public class CprogramUserInfoModel extends Model<CprogramUserInfoModel> {
    private static final long serialVersionUID = 1L;

    public static final CprogramUserInfoModel dao = new CprogramUserInfoModel();

    public static final String TABLE_NAME = "cprogram_user_info";
    public static final String UID = "uid";
    public static final String CLASSES = "classes";
    public static final String STUID = "stuid";
    public static final String TID = "tid";
    public static final String CLASS_WEEK = "class_week";
    public static final String CLASS_LECTURE = "class_lecture";
    public static final String CTIME = "ctime";

    /*
     * auto generated getter and setter
     */
    public Integer getUid() {
        return getInt(UID);
    }

    public CprogramUserInfoModel setUid(Integer value) {
        return set(UID, value);
    }

    public String getClasses() {
        return getStr(CLASSES);
    }

    public CprogramUserInfoModel setClasses(String value) {
        return set(CLASSES, value);
    }

    public String getStuid() {
        return getStr(STUID);
    }

    public CprogramUserInfoModel setStuid(String value) {
        return set(STUID, value);
    }

    public Integer getTid() {
        return getInt(TID);
    }

    public CprogramUserInfoModel setTid(Integer value) {
        return set(TID, value);
    }

    public Integer getClass_week() {
        return getInt(CLASS_WEEK);
    }

    public CprogramUserInfoModel setClass_week(Integer value) {
        return set(CLASS_WEEK, value);
    }

    public Integer getClass_lecture() {
        return getInt(CLASS_LECTURE);
    }

    public CprogramUserInfoModel setClass_lecture(Integer value) {
        return set(CLASS_LECTURE, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public CprogramUserInfoModel setCtime(Integer value) {
        return set(CTIME, value);
    }

}
