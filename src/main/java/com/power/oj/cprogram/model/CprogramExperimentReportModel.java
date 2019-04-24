package com.power.oj.cprogram.model;

import com.jfinal.plugin.activerecord.Model;

public class CprogramExperimentReportModel extends Model<CprogramExperimentReportModel> {
    private static final long serialVersionUID = 1L;

    public static final CprogramExperimentReportModel dao = new CprogramExperimentReportModel();

    public static final String TABLE_NAME = "cprogram_experiment_report";
    public static final String ID = "id";
    public static final String CID = "cid";
    public static final String UID = "uid";
    public static final String TIMES = "times";
    public static final String WEEK = "week";
    public static final String LECTURE = "lecture";
    public static final String COMMIT = "commit";

    /*
     * auto generated getter and setter
     */
    public Integer getId() {
        return getInt(ID);
    }

    public CprogramExperimentReportModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getCid() {
        return getInt(CID);
    }

    public CprogramExperimentReportModel setCid(Integer value) {
        return set(CID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public CprogramExperimentReportModel setUid(Integer value) {
        return set(UID, value);
    }

    public Integer getTimes() {
        return getInt(TIMES);
    }

    public CprogramExperimentReportModel setTimes(Integer value) {
        return set(TIMES, value);
    }

    public Integer getWeek() {
        return getInt(WEEK);
    }

    public CprogramExperimentReportModel setWeek(Integer value) {
        return set(WEEK, value);
    }

    public Integer getLecture() {
        return getInt(LECTURE);
    }

    public CprogramExperimentReportModel setLecture(Integer value) {
        return set(LECTURE, value);
    }

    public String getCommit() {
        return getStr(COMMIT);
    }

    public CprogramExperimentReportModel setCommit(String value) {
        return set(COMMIT, value);
    }

}
