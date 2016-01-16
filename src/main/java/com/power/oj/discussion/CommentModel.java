package com.power.oj.discussion;

import com.jfinal.plugin.activerecord.Model;

public class CommentModel extends Model<CommentModel> {
    public static final CommentModel dao = new CommentModel();
    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String THREAD_ID = "threadId";
    public static final String QUOTE_ID = "quoteId";
    public static final String CONTENT = "content";
    public static final String IP = "ip";
    public static final String CTIME = "ctime";
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return getInt(ID);
    }

    public CommentModel setId(Integer value) {
        return set(ID, value);
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public CommentModel setUid(Integer value) {
        return set(UID, value);
    }

    public Integer getThreadId() {
        return getInt(THREAD_ID);
    }

    public CommentModel setThreadId(Integer value) {
        return set(THREAD_ID, value);
    }

    public Integer getQuoteId() {
        return getInt(QUOTE_ID);
    }

    public CommentModel setQuoteId(Integer value) {
        return set(QUOTE_ID, value);
    }

    public String getContent() {
        return getStr(CONTENT);
    }

    public CommentModel setContent(String value) {
        return set(CONTENT, value);
    }

    public String getIp() {
        return getStr(IP);
    }

    public CommentModel setIp(String value) {
        return set(IP, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public CommentModel setCtime(Integer value) {
        return set(CTIME, value);
    }

}
