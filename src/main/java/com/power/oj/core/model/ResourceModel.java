package com.power.oj.core.model;


import com.jfinal.plugin.activerecord.Model;

public class ResourceModel extends Model<ResourceModel> {
    private static final long serialVersionUID = 1L;

    public static final ResourceModel dao = new ResourceModel();

    public static final String TABLE_NAME = "resource";
    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PATH = "path";
    public static final String CTIME = "ctime";
    public static final String DOWNLOAD = "download";
    public static final String ACCESS = "access";

    /*
     * auto generated getter and setter
     */
    public Integer getId() {
        return getInt(ID);
    }

    public ResourceModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public ResourceModel setUid(Integer value) {
        return set(UID, value);
    }

    public String getName() {
        return getStr(NAME);
    }

    public ResourceModel setName(String value) {
        return set(NAME, value);
    }

    public String getDescription() {
        return getStr(DESCRIPTION);
    }

    public ResourceModel setDescription(String value) {
        return set(DESCRIPTION, value);
    }

    public String getPath() {
        return getStr(PATH);
    }

    public ResourceModel setPath(String value) {
        return set(PATH, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public ResourceModel setCtime(Integer value) {
        return set(CTIME, value);
    }

    public Integer getDownload() {
        return getInt(DOWNLOAD);
    }

    public ResourceModel setDownload(Integer value) {
        return set(DOWNLOAD, value);
    }

    public String getAccess() {
        return getStr(ACCESS);
    }

    public ResourceModel setAccess(String value) {
        return set(ACCESS, value);
    }

}
