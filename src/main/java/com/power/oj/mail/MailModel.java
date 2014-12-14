package com.power.oj.mail;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class MailModel extends Model<MailModel> {

	/**
   * 
   */
	private static final long serialVersionUID = 854677482560407471L;

	public static final MailModel dao = new MailModel();
	public static final String ID = "id";
	public static final String MID = "mid";
	public static final String USER = "user";
	public static final String PEER = "peer";
	public static final String STATUS = "status";

	public List<MailModel> findUserNewMails(Integer uid) {
		return find("SELECT * FROM mail WHERE user=? AND status=0", uid);
	}

	public Long countUserNewMails(Integer uid) {
		return findFirst("SELECT COUNT(*) AS count FROM mail WHERE user=? AND status=0", uid).getLong("count");
	}

	public Long countUserNewMails(Integer user, Integer peer) {
		return findFirst("SELECT COUNT(*) AS count FROM mail WHERE user=? AND peer=? AND status=0", user, peer).getLong("count");
	}

	public boolean hasNewMails(Integer user, Integer peer) {
		return findFirst("SELECT 1 FROM mail WHERE user=? AND peer=? AND status=0 LIMIT 1", user, peer) != null;
	}

	public int resetUserNewMails(Integer user, Integer peer) {
		return Db.update("UPDATE mail SET status=1 WHERE user=? AND peer=? AND status=0", user, peer);
	}

	public int deleteMail(Integer uid, Integer id) {
		return Db.update("DELETE FROM mail WHERE user=? AND id=?", uid, id);
	}

	public int deleteMailGroup(Integer user, Integer peer) {
		return Db.update("DELETE FROM mail WHERE user=? AND peer=?", user, peer);
	}

	public int deleteUserAllMails(Integer uid) {
		return Db.update("DELETE FROM mail WHERE user=?", uid);
	}

	public Integer getId() {
		return getInt(ID);
	}

	public MailModel setId(Integer value) {
		return set(ID, value);
	}

	public Integer getMid() {
		return getInt(MID);
	}

	public MailModel setMid(Integer value) {
		return set(MID, value);
	}

	public Integer getUser() {
		return getInt(USER);
	}

	public MailModel setUser(Integer value) {
		return set(USER, value);
	}

	public Integer getPeer() {
		return getInt(PEER);
	}

	public MailModel setPeer(Integer value) {
		return set(PEER, value);
	}

	public Boolean getStatus() {
		return getBoolean(STATUS);
	}

	public MailModel setStatus(Boolean value) {
		return set(STATUS, value);
	}

}
