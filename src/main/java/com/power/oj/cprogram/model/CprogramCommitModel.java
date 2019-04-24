package com.power.oj.cprogram.model;

import com.jfinal.plugin.activerecord.Model;

public class CprogramCommitModel extends Model<CprogramCommitModel> {
    private static final long serialVersionUID = 1L;

    public static final CprogramCommitModel dao = new CprogramCommitModel();

    public static final String TABLE_NAME = "cprogram_commit";
    public static final String ID = "id";
    public static final String CID = "cid";
    public static final String UID = "uid";
    public static final String NUM = "num";
    public static final String COMMIT = "commit";

    /*
     * auto generated getter and setter
     */
    public Integer getId() {
        return getInt(ID);
    }

    public CprogramCommitModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getCid() {
        return getInt(CID);
    }

    public CprogramCommitModel setCid(Integer value) {
        return set(CID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public CprogramCommitModel setUid(Integer value) {
        return set(UID, value);
    }

    public Integer getNum() {
        return getInt(NUM);
    }

    public CprogramCommitModel setNum(Integer value) {
        return set(NUM, value);
    }

    public String getCommit() {
        return getStr(COMMIT);
    }

    public CprogramCommitModel setCommit(String value) {
        return set(COMMIT, value);
    }

}
