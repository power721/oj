package com.power.oj.contest.model;

import com.jfinal.plugin.activerecord.Model;

public class ContestClarifyModel extends Model<ContestClarifyModel> {
	private static final long serialVersionUID = 1L;

	public static final ContestClarifyModel dao = new ContestClarifyModel();

	public static final String TABLE_NAME = "contest_clarify";
	public static final String ID = "id";
	public static final String CID = "cid";
	public static final String NUM = "num";
	public static final String UID = "uid";
	public static final String ADMIN = "admin";
	public static final String QUESTION = "question";
	public static final String REPLY = "reply";
	public static final String PUBLIC = "public";
	public static final String CTIME = "ctime";
	public static final String ATIME = "atime";
	public static final String MTIME = "mtime";

	public Integer getId() {
		return getInt(ID);
	}

	public ContestClarifyModel setId(Integer value) {
		return set(ID, value);
	}

	public Integer getCid() {
		return getInt(CID);
	}

	public ContestClarifyModel setCid(Integer value) {
		return set(CID, value);
	}

	public Integer getNum() {
		return getInt(NUM);
	}

	public ContestClarifyModel setNum(Integer value) {
		return set(NUM, value);
	}

	public Integer getUid() {
		return getInt(UID);
	}

	public ContestClarifyModel setUid(Integer value) {
		return set(UID, value);
	}

	public Integer getAdmin() {
		return getInt(ADMIN);
	}

	public ContestClarifyModel setAdmin(Integer value) {
		return set(ADMIN, value);
	}

	public String getQuestion() {
		return getStr(QUESTION);
	}

	public ContestClarifyModel setQuestion(String value) {
		return set(QUESTION, value);
	}

	public String getReply() {
		return getStr(REPLY);
	}

	public ContestClarifyModel setReply(String value) {
		return set(REPLY, value);
	}

	public Boolean getPublic() {
		return getBoolean(PUBLIC);
	}

	public ContestClarifyModel setPublic(Boolean value) {
		return set(PUBLIC, value);
	}

	public Integer getCtime() {
		return getInt(CTIME);
	}

	public ContestClarifyModel setCtime(Integer value) {
		return set(CTIME, value);
	}

	public Integer getAtime() {
		return getInt(ATIME);
	}

	public ContestClarifyModel setAtime(Integer value) {
		return set(ATIME, value);
	}

	public Integer getMtime() {
		return getInt(MTIME);
	}

	public ContestClarifyModel setMtime(Integer value) {
		return set(MTIME, value);
	}

}
