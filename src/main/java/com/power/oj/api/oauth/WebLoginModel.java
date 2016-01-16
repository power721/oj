package com.power.oj.api.oauth;

import com.jfinal.plugin.activerecord.Model;

public class WebLoginModel extends Model<WebLoginModel> {
    public static final WebLoginModel dao = new WebLoginModel();
    public static final String ID = "id";
    public static final String OPEN_ID = "openId";
    public static final String UID = "uid";
    public static final String NICK = "nick";
    public static final String AVATAR = "avatar";
    public static final String TYPE = "type";
    public static final String STATUS = "status";
    public static final String CTIME = "ctime";
    private static final long serialVersionUID = 1L;

    public WebLoginModel findByOpenID(String openid, String type) {
        String sql = "SELECT wb.* FROM web_login wb WHERE wb.open_id = ? AND wb.type = ? limit 1";
        return dao.findFirst(sql, openid, type);
    }

    public Integer getId() {
        return getInt(ID);
    }

    public WebLoginModel setId(Integer value) {
        return set(ID, value);
    }

    public String getOpenId() {
        return getStr(OPEN_ID);
    }

    public WebLoginModel setOpenId(String value) {
        return set(OPEN_ID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public WebLoginModel setUid(Integer value) {
        return set(UID, value);
    }

    public String getNick() {
        return getStr(NICK);
    }

    public WebLoginModel setNick(String value) {
        return set(NICK, value);
    }

    public String getAvatar() {
        return getStr(AVATAR);
    }

    public WebLoginModel setAvatar(String value) {
        return set(AVATAR, value);
    }

    public String getType() {
        return getStr(TYPE);
    }

    public WebLoginModel setType(String value) {
        return set(TYPE, value);
    }

    public Boolean getStatus() {
        return getBoolean(STATUS);
    }

    public WebLoginModel setStatus(Boolean value) {
        return set(STATUS, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public WebLoginModel setCtime(Integer value) {
        return set(CTIME, value);
    }

}
