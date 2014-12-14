package com.power.oj.contest.model;

import com.jfinal.plugin.activerecord.Model;

public class ContestUserModel extends Model<ContestUserModel> {
	private static final long serialVersionUID = 1L;

	public static final ContestUserModel dao = new ContestUserModel();

	public static final String TABLE_NAME = "contest_user";
	public static final String ID = "id";
	public static final String UID = "uid";
	public static final String CID = "cid";
	public static final String CTIME = "ctime";

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

	public Integer getCtime() {
		return getInt(CTIME);
	}

	public ContestUserModel setCtime(Integer value) {
		return set(CTIME, value);
	}

}
