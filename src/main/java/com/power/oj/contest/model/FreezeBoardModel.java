package com.power.oj.contest.model;

import com.jfinal.plugin.activerecord.Model;

public class FreezeBoardModel extends Model<FreezeBoardModel> {
	private static final long serialVersionUID = 1L;

	public static final FreezeBoardModel dao = new FreezeBoardModel();

	public static final String TABLE_NAME = "freeze_board";
	public static final String ID = "id";
	public static final String CID = "cid";
	public static final String UID = "uid";
	public static final String SOLVED = "solved";
	public static final String PENALTY = "penalty";
	public static final String A_SOLVED_TIME = "A_SolvedTime";
	public static final String A_WRONG_NUM = "A_WrongNum";
	public static final String B_SOLVED_TIME = "B_SolvedTime";
	public static final String B_WRONG_NUM = "B_WrongNum";
	public static final String C_SOLVED_TIME = "C_SolvedTime";
	public static final String C_WRONG_NUM = "C_WrongNum";
	public static final String D_SOLVED_TIME = "D_SolvedTime";
	public static final String D_WRONG_NUM = "D_WrongNum";
	public static final String E_SOLVED_TIME = "E_SolvedTime";
	public static final String E_WRONG_NUM = "E_WrongNum";
	public static final String F_SOLVED_TIME = "F_SolvedTime";
	public static final String F_WRONG_NUM = "F_WrongNum";
	public static final String G_SOLVED_TIME = "G_SolvedTime";
	public static final String G_WRONG_NUM = "G_WrongNum";
	public static final String H_SOLVED_TIME = "H_SolvedTime";
	public static final String H_WRONG_NUM = "H_WrongNum";
	public static final String I_SOLVED_TIME = "I_SolvedTime";
	public static final String I_WRONG_NUM = "I_WrongNum";
	public static final String J_SOLVED_TIME = "J_SolvedTime";
	public static final String J_WRONG_NUM = "J_WrongNum";
	public static final String K_SOLVED_TIME = "K_SolvedTime";
	public static final String K_WRONG_NUM = "K_WrongNum";
	public static final String L_SOLVED_TIME = "L_SolvedTime";
	public static final String L_WRONG_NUM = "L_WrongNum";
	public static final String M_SOLVED_TIME = "M_SolvedTime";
	public static final String M_WRONG_NUM = "M_WrongNum";
	public static final String N_SOLVED_TIME = "N_SolvedTime";
	public static final String N_WRONG_NUM = "N_WrongNum";
	public static final String O_SOLVED_TIME = "O_SolvedTime";
	public static final String O_WRONG_NUM = "O_WrongNum";
	public static final String P_SOLVED_TIME = "P_SolvedTime";
	public static final String P_WRONG_NUM = "P_WrongNum";
	public static final String Q_SOLVED_TIME = "Q_SolvedTime";
	public static final String Q_WRONG_NUM = "Q_WrongNum";
	public static final String R_SOLVED_TIME = "R_SolvedTime";
	public static final String R_WRONG_NUM = "R_WrongNum";
	public static final String S_SOLVED_TIME = "S_SolvedTime";
	public static final String S_WRONG_NUM = "S_WrongNum";
	public static final String T_SOLVED_TIME = "T_SolvedTime";
	public static final String T_WRONG_NUM = "T_WrongNum";
	public static final String U_SOLVED_TIME = "U_SolvedTime";
	public static final String U_WRONG_NUM = "U_WrongNum";
	public static final String V_SOLVED_TIME = "V_SolvedTime";
	public static final String V_WRONG_NUM = "V_WrongNum";
	public static final String W_SOLVED_TIME = "W_SolvedTime";
	public static final String W_WRONG_NUM = "W_WrongNum";
	public static final String X_SOLVED_TIME = "X_SolvedTime";
	public static final String X_WRONG_NUM = "X_WrongNum";
	public static final String Y_SOLVED_TIME = "Y_SolvedTime";
	public static final String Y_WRONG_NUM = "Y_WrongNum";
	public static final String Z_SOLVED_TIME = "Z_SolvedTime";
	public static final String Z_WRONG_NUM = "Z_WrongNum";

	public Integer getId() {
		return getInt(ID);
	}

	public FreezeBoardModel setId(Integer value) {
		return set(ID, value);
	}

	public Integer getCid() {
		return getInt(CID);
	}

	public FreezeBoardModel setCid(Integer value) {
		return set(CID, value);
	}

	public Integer getUid() {
		return getInt(UID);
	}

	public FreezeBoardModel setUid(Integer value) {
		return set(UID, value);
	}

	public Integer getSolved() {
		return getInt(SOLVED);
	}

	public FreezeBoardModel setSolved(Integer value) {
		return set(SOLVED, value);
	}

	public Integer getPenalty() {
		return getInt(PENALTY);
	}

	public FreezeBoardModel setPenalty(Integer value) {
		return set(PENALTY, value);
	}

	public Integer getSolvedTime(int num) {
		char c = (char) (num + 'A');
		String field = new StringBuilder().append(c).append("_SolvedTime").toString();
		return getInt(field);
	}

	public FreezeBoardModel setSolvedTime(int num, Integer value) {
		char c = (char) (num + 'A');
		String field = new StringBuilder().append(c).append("_SolvedTime").toString();
		return set(field, value);
	}

	public Integer getWrongNum(int num) {
		char c = (char) (num + 'A');
		String field = new StringBuilder().append(c).append("_WrongNum").toString();
		return getInt(field);
	}

	public FreezeBoardModel setWrongNum(int num, Integer value) {
		char c = (char) (num + 'A');
		String field = new StringBuilder().append(c).append("_WrongNum").toString();
		return set(field, value);
	}

	/*
	 * public Integer getA_SolvedTime() { return getInt(A_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setA_SolvedTime(Integer value) { return
	 * set(A_SOLVED_TIME, value); }
	 * 
	 * public Integer getA_WrongNum() { return getInt(A_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setA_WrongNum(Integer value) { return
	 * set(A_WRONG_NUM, value); }
	 * 
	 * public Integer getB_SolvedTime() { return getInt(B_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setB_SolvedTime(Integer value) { return
	 * set(B_SOLVED_TIME, value); }
	 * 
	 * public Integer getB_WrongNum() { return getInt(B_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setB_WrongNum(Integer value) { return
	 * set(B_WRONG_NUM, value); }
	 * 
	 * public Integer getC_SolvedTime() { return getInt(C_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setC_SolvedTime(Integer value) { return
	 * set(C_SOLVED_TIME, value); }
	 * 
	 * public Integer getC_WrongNum() { return getInt(C_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setC_WrongNum(Integer value) { return
	 * set(C_WRONG_NUM, value); }
	 * 
	 * public Integer getD_SolvedTime() { return getInt(D_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setD_SolvedTime(Integer value) { return
	 * set(D_SOLVED_TIME, value); }
	 * 
	 * public Integer getD_WrongNum() { return getInt(D_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setD_WrongNum(Integer value) { return
	 * set(D_WRONG_NUM, value); }
	 * 
	 * public Integer getE_SolvedTime() { return getInt(E_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setE_SolvedTime(Integer value) { return
	 * set(E_SOLVED_TIME, value); }
	 * 
	 * public Integer getE_WrongNum() { return getInt(E_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setE_WrongNum(Integer value) { return
	 * set(E_WRONG_NUM, value); }
	 * 
	 * public Integer getF_SolvedTime() { return getInt(F_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setF_SolvedTime(Integer value) { return
	 * set(F_SOLVED_TIME, value); }
	 * 
	 * public Integer getF_WrongNum() { return getInt(F_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setF_WrongNum(Integer value) { return
	 * set(F_WRONG_NUM, value); }
	 * 
	 * public Integer getG_SolvedTime() { return getInt(G_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setG_SolvedTime(Integer value) { return
	 * set(G_SOLVED_TIME, value); }
	 * 
	 * public Integer getG_WrongNum() { return getInt(G_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setG_WrongNum(Integer value) { return
	 * set(G_WRONG_NUM, value); }
	 * 
	 * public Integer getH_SolvedTime() { return getInt(H_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setH_SolvedTime(Integer value) { return
	 * set(H_SOLVED_TIME, value); }
	 * 
	 * public Integer getH_WrongNum() { return getInt(H_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setH_WrongNum(Integer value) { return
	 * set(H_WRONG_NUM, value); }
	 * 
	 * public Integer getI_SolvedTime() { return getInt(I_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setI_SolvedTime(Integer value) { return
	 * set(I_SOLVED_TIME, value); }
	 * 
	 * public Integer getI_WrongNum() { return getInt(I_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setI_WrongNum(Integer value) { return
	 * set(I_WRONG_NUM, value); }
	 * 
	 * public Integer getJ_SolvedTime() { return getInt(J_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setJ_SolvedTime(Integer value) { return
	 * set(J_SOLVED_TIME, value); }
	 * 
	 * public Integer getJ_WrongNum() { return getInt(J_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setJ_WrongNum(Integer value) { return
	 * set(J_WRONG_NUM, value); }
	 * 
	 * public Integer getK_SolvedTime() { return getInt(K_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setK_SolvedTime(Integer value) { return
	 * set(K_SOLVED_TIME, value); }
	 * 
	 * public Integer getK_WrongNum() { return getInt(K_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setK_WrongNum(Integer value) { return
	 * set(K_WRONG_NUM, value); }
	 * 
	 * public Integer getL_SolvedTime() { return getInt(L_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setL_SolvedTime(Integer value) { return
	 * set(L_SOLVED_TIME, value); }
	 * 
	 * public Integer getL_WrongNum() { return getInt(L_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setL_WrongNum(Integer value) { return
	 * set(L_WRONG_NUM, value); }
	 * 
	 * public Integer getM_SolvedTime() { return getInt(M_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setM_SolvedTime(Integer value) { return
	 * set(M_SOLVED_TIME, value); }
	 * 
	 * public Integer getM_WrongNum() { return getInt(M_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setM_WrongNum(Integer value) { return
	 * set(M_WRONG_NUM, value); }
	 * 
	 * public Integer getN_SolvedTime() { return getInt(N_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setN_SolvedTime(Integer value) { return
	 * set(N_SOLVED_TIME, value); }
	 * 
	 * public Integer getN_WrongNum() { return getInt(N_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setN_WrongNum(Integer value) { return
	 * set(N_WRONG_NUM, value); }
	 * 
	 * public Integer getO_SolvedTime() { return getInt(O_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setO_SolvedTime(Integer value) { return
	 * set(O_SOLVED_TIME, value); }
	 * 
	 * public Integer getO_WrongNum() { return getInt(O_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setO_WrongNum(Integer value) { return
	 * set(O_WRONG_NUM, value); }
	 * 
	 * public Integer getP_SolvedTime() { return getInt(P_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setP_SolvedTime(Integer value) { return
	 * set(P_SOLVED_TIME, value); }
	 * 
	 * public Integer getP_WrongNum() { return getInt(P_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setP_WrongNum(Integer value) { return
	 * set(P_WRONG_NUM, value); }
	 * 
	 * public Integer getQ_SolvedTime() { return getInt(Q_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setQ_SolvedTime(Integer value) { return
	 * set(Q_SOLVED_TIME, value); }
	 * 
	 * public Integer getQ_WrongNum() { return getInt(Q_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setQ_WrongNum(Integer value) { return
	 * set(Q_WRONG_NUM, value); }
	 * 
	 * public Integer getR_SolvedTime() { return getInt(R_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setR_SolvedTime(Integer value) { return
	 * set(R_SOLVED_TIME, value); }
	 * 
	 * public Integer getR_WrongNum() { return getInt(R_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setR_WrongNum(Integer value) { return
	 * set(R_WRONG_NUM, value); }
	 * 
	 * public Integer getS_SolvedTime() { return getInt(S_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setS_SolvedTime(Integer value) { return
	 * set(S_SOLVED_TIME, value); }
	 * 
	 * public Integer getS_WrongNum() { return getInt(S_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setS_WrongNum(Integer value) { return
	 * set(S_WRONG_NUM, value); }
	 * 
	 * public Integer getT_SolvedTime() { return getInt(T_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setT_SolvedTime(Integer value) { return
	 * set(T_SOLVED_TIME, value); }
	 * 
	 * public Integer getT_WrongNum() { return getInt(T_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setT_WrongNum(Integer value) { return
	 * set(T_WRONG_NUM, value); }
	 * 
	 * public Integer getU_SolvedTime() { return getInt(U_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setU_SolvedTime(Integer value) { return
	 * set(U_SOLVED_TIME, value); }
	 * 
	 * public Integer getU_WrongNum() { return getInt(U_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setU_WrongNum(Integer value) { return
	 * set(U_WRONG_NUM, value); }
	 * 
	 * public Integer getV_SolvedTime() { return getInt(V_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setV_SolvedTime(Integer value) { return
	 * set(V_SOLVED_TIME, value); }
	 * 
	 * public Integer getV_WrongNum() { return getInt(V_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setV_WrongNum(Integer value) { return
	 * set(V_WRONG_NUM, value); }
	 * 
	 * public Integer getW_SolvedTime() { return getInt(W_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setW_SolvedTime(Integer value) { return
	 * set(W_SOLVED_TIME, value); }
	 * 
	 * public Integer getW_WrongNum() { return getInt(W_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setW_WrongNum(Integer value) { return
	 * set(W_WRONG_NUM, value); }
	 * 
	 * public Integer getX_SolvedTime() { return getInt(X_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setX_SolvedTime(Integer value) { return
	 * set(X_SOLVED_TIME, value); }
	 * 
	 * public Integer getX_WrongNum() { return getInt(X_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setX_WrongNum(Integer value) { return
	 * set(X_WRONG_NUM, value); }
	 * 
	 * public Integer getY_SolvedTime() { return getInt(Y_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setY_SolvedTime(Integer value) { return
	 * set(Y_SOLVED_TIME, value); }
	 * 
	 * public Integer getY_WrongNum() { return getInt(Y_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setY_WrongNum(Integer value) { return
	 * set(Y_WRONG_NUM, value); }
	 * 
	 * public Integer getZ_SolvedTime() { return getInt(Z_SOLVED_TIME); }
	 * 
	 * public FreezeBoardModel setZ_SolvedTime(Integer value) { return
	 * set(Z_SOLVED_TIME, value); }
	 * 
	 * public Integer getZ_WrongNum() { return getInt(Z_WRONG_NUM); }
	 * 
	 * public FreezeBoardModel setZ_WrongNum(Integer value) { return
	 * set(Z_WRONG_NUM, value); }
	 */
}
