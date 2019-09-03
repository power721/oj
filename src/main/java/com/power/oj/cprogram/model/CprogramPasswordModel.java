package com.power.oj.cprogram.model;

import com.jfinal.plugin.activerecord.Model;

public class CprogramPasswordModel extends Model<CprogramPasswordModel> {
    private static final long serialVersionUID = 1L;

    public static final CprogramPasswordModel dao = new CprogramPasswordModel();

    public static final String TABLE_NAME = "cprogram_password";
    public static final String ID = "id";
    public static final String CID = "cid";
    public static final String PASSWORD = "password";
    public static final String UID = "uid";

    /*
     * auto generated getter and setter
     */
    public Integer getId() {
        return getInt(ID);
    }

    public CprogramPasswordModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getCid() {
        return getInt(CID);
    }

    public CprogramPasswordModel setCid(Integer value) {
        return set(CID, value);
    }

    public String getPassword() {
        return getStr(PASSWORD);
    }

    public CprogramPasswordModel setPassword(String value) {
        return set(PASSWORD, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public CprogramPasswordModel setUid(Integer value) {
        return set(UID, value);
    }

}
