package com.power.oj.notice;

import com.jfinal.plugin.activerecord.Model;

public class NoticeModel extends Model<NoticeModel> {
	private static final long serialVersionUID = 1L;

	public static final NoticeModel dao = new NoticeModel();

	public static final String TABLE_NAME = "notice";
	public static final String ID = "id";
	public static final String UID = "uid";
	public static final String EDITOR_UID = "editorUid";
	public static final String CID = "cid";
	public static final String TITLE = "title";
	public static final String START_TIME = "startTime";
	public static final String END_TIME = "endTime";
	public static final String CONTENT = "content";
	public static final String ATIME = "atime";
	public static final String CTIME = "ctime";
	public static final String MTIME = "mtime";
	public static final String VIEW = "view";
	public static final String STATUS = "status";

	public Integer getId() {
		return getInt(ID);
	}

	public NoticeModel setId(Integer value) {
		return set(ID, value);
	}

	public Integer getUid() {
		return getInt(UID);
	}

	public NoticeModel setUid(Integer value) {
		return set(UID, value);
	}

	public Integer getEditorUid() {
		return getInt(EDITOR_UID);
	}

	public NoticeModel setEditorUid(Integer value) {
		return set(EDITOR_UID, value);
	}

	public Integer getCid() {
		return getInt(CID);
	}

	public NoticeModel setCid(Integer value) {
		return set(CID, value);
	}

	public String getTitle() {
		return getStr(TITLE);
	}

	public NoticeModel setTitle(String value) {
		return set(TITLE, value);
	}

	public Integer getStartTime() {
		return getInt(START_TIME);
	}

	public NoticeModel setStartTime(Integer value) {
		return set(START_TIME, value);
	}

	public Integer getEndTime() {
		return getInt(END_TIME);
	}

	public NoticeModel setEndTime(Integer value) {
		return set(END_TIME, value);
	}

	public String getContent() {
		return getStr(CONTENT);
	}

	public NoticeModel setContent(String value) {
		return set(CONTENT, value);
	}

	public Integer getAtime() {
		return getInt(ATIME);
	}

	public NoticeModel setAtime(Integer value) {
		return set(ATIME, value);
	}

	public Integer getCtime() {
		return getInt(CTIME);
	}

	public NoticeModel setCtime(Integer value) {
		return set(CTIME, value);
	}

	public Integer getMtime() {
		return getInt(MTIME);
	}

	public NoticeModel setMtime(Integer value) {
		return set(MTIME, value);
	}

	public Integer getView() {
		return getInt(VIEW);
	}

	public NoticeModel setView(Integer value) {
		return set(VIEW, value);
	}

	public Boolean getStatus() {
		return getBoolean(STATUS);
	}

	public NoticeModel setStatus(Boolean value) {
		return set(STATUS, value);
	}

}
