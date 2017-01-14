package com.power.oj.contest.model;

import com.jfinal.plugin.activerecord.Model;

public class ContestUserModel extends Model<ContestUserModel> {
    private static final long serialVersionUID = 1L;

    public static final ContestUserModel dao = new ContestUserModel();

    public static final String TABLE_NAME = "contest_user";
    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String CID = "cid";
    public static final String SPECIAL = "special";
    public static final String FRESHMAN = "freshman";
    public static final String GIRLS = "girls";
    public static final String NICK = "nick";
    public static final String CTIME = "ctime";

    /*
    * auto generated getter and setter
    */
    public Integer getId() {
        return getInt(ID);
    }

    public ContestUserModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public ContestUserModel setUid(Integer value) {
        return set(UID, value);
    }

    public Integer getCid() {
        return getInt(CID);
    }

    public ContestUserModel setCid(Integer value) {
        return set(CID, value);
    }

    public Boolean getSpecial() {
        return getBoolean(SPECIAL);
    }

    public ContestUserModel setSpecial(Boolean value) {
        return set(SPECIAL, value);
    }

    public Boolean getFreshman() {
        return getBoolean(FRESHMAN);
    }

    public ContestUserModel setFreshman(Boolean value) {
        return set(FRESHMAN, value);
    }

    public Boolean getGirls() {
        return getBoolean(GIRLS);
    }

    public ContestUserModel setGirls(Boolean value) {
        return set(GIRLS, value);
    }

    public String getNick() {
        return getStr(NICK);
    }

    public ContestUserModel setNick(String value) {
        return set(NICK, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public ContestUserModel setCtime(Integer value) {
        return set(CTIME, value);
    }

}
