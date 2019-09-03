package com.power.oj.cprogram.model;

import com.jfinal.plugin.activerecord.Model;

public class ScoreModel extends Model<ScoreModel> {
    private static final long serialVersionUID = 1L;

    public static final ScoreModel dao = new ScoreModel();

    public static final String TABLE_NAME = "score";
    public static final String RID = "rid";
    public static final String UID = "uid";
    public static final String CID = "cid";
    public static final String SCORE1 = "score1";
    public static final String SCORE2 = "score2";
    public static final String ACCEPTED = "accepted";
    public static final String SUBMITED = "submited";
    public static final String CTIME = "ctime";
    public static final String WEEK = "week";
    public static final String LECTURE = "lecture";

    /*
     * auto generated getter and setter
     */
    public Integer getRid() {
        return getInt(RID);
    }

    public ScoreModel setRid(Integer value) {
        return set(RID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public ScoreModel setUid(Integer value) {
        return set(UID, value);
    }

    public Integer getCid() {
        return getInt(CID);
    }

    public ScoreModel setCid(Integer value) {
        return set(CID, value);
    }

    public Integer getScore1() {
        return getInt(SCORE1);
    }

    public ScoreModel setScore1(Integer value) {
        return set(SCORE1, value);
    }

    public Integer getScore2() {
        return getInt(SCORE2);
    }

    public ScoreModel setScore2(Integer value) {
        return set(SCORE2, value);
    }

    public Integer getAccepted() {
        return getInt(ACCEPTED);
    }

    public ScoreModel setAccepted(Integer value) {
        return set(ACCEPTED, value);
    }

    public Integer getSubmited() {
        return getInt(SUBMITED);
    }

    public ScoreModel setSubmited(Integer value) {
        return set(SUBMITED, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public ScoreModel setCtime(Integer value) {
        return set(CTIME, value);
    }

    public Integer getWeek() {
        return getInt(WEEK);
    }

    public ScoreModel setWeek(Integer value) {
        return set(WEEK, value);
    }

    public Integer getLecture() {
        return getInt(LECTURE);
    }

    public ScoreModel setLecture(Integer value) {
        return set(LECTURE, value);
    }

}
