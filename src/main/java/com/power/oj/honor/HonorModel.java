package com.power.oj.honor;

import com.jfinal.plugin.activerecord.Model;

public class HonorModel extends Model<HonorModel> {
	private static final long serialVersionUID = 1L;

	public static final HonorModel dao = new HonorModel();

	public static final String TABLE_NAME = "honors";
	public static final String LEVEL = "level";
	public static final String CONTEST = "contest";
	public static final String TEAM = "team";
	public static final String PLAYER = "player";
	public static final String PRIZE = "prize";

	public String getLevel() {
		return getStr(LEVEL);
	}

	public HonorModel setLevel(String value) {
		return set(LEVEL, value);
	}

	public String getContest() {
		return getStr(CONTEST);
	}

	public HonorModel setContest(String value) {
		return set(CONTEST, value);
	}

	public String getTeam() {
		return getStr(TEAM);
	}

	public HonorModel setTeam(String value) {
		return set(TEAM, value);
	}

	public String getPlayer() {
		return getStr(PLAYER);
	}

	public HonorModel setPlayer(String value) {
		return set(PLAYER, value);
	}

	public String getPrize() {
		return getStr(PRIZE);
	}

	public HonorModel setPrize(String value) {
		return set(PRIZE, value);
	}
}
