package com.power.oj.social;

import com.jfinal.plugin.activerecord.Model;

public class FriendGroupModel extends Model<FriendGroupModel> {
    public static final FriendGroupModel dao = new FriendGroupModel();
    public static final String TABLE_NAME = "friend_group";
    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String COUNT = "count";
    public static final String CTIME = "ctime";
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return getInt(ID);
    }

    public FriendGroupModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public FriendGroupModel setUid(Integer value) {
        return set(UID, value);
    }

    public String getName() {
        return getStr(NAME);
    }

    public FriendGroupModel setName(String value) {
        return set(NAME, value);
    }

    public Integer getCount() {
        return getInt(COUNT);
    }

    public FriendGroupModel setCount(Integer value) {
        return set(COUNT, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public FriendGroupModel setCtime(Integer value) {
        return set(CTIME, value);
    }

}
