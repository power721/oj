package com.power.oj.bean;

import com.jfinal.plugin.activerecord.Model;

public class Board extends Model<Board>
{
  private static final long serialVersionUID = 1L;
  
  public static final Board dao = new Board();
  
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

  public <T> T getId()
  {
    return get(ID);
  }
  
  public Board setId(Object value)
  {
    return set(ID, value);
  }
  
  public <T> T getCid()
  {
    return get(CID);
  }
  
  public Board setCid(Object value)
  {
    return set(CID, value);
  }
  
  public <T> T getUid()
  {
    return get(UID);
  }
  
  public Board setUid(Object value)
  {
    return set(UID, value);
  }
  
  public <T> T getSolved()
  {
    return get(SOLVED);
  }
  
  public Board setSolved(Object value)
  {
    return set(SOLVED, value);
  }
  
  public <T> T getPenalty()
  {
    return get(PENALTY);
  }
  
  public Board setPenalty(Object value)
  {
    return set(PENALTY, value);
  }
  
  public <T> T getA_SolvedTime()
  {
    return get(A_SOLVED_TIME);
  }
  
  public Board setA_SolvedTime(Object value)
  {
    return set(A_SOLVED_TIME, value);
  }
  
  public <T> T getA_WrongNum()
  {
    return get(A_WRONG_NUM);
  }
  
  public Board setA_WrongNum(Object value)
  {
    return set(A_WRONG_NUM, value);
  }
  
  public <T> T getB_SolvedTime()
  {
    return get(B_SOLVED_TIME);
  }
  
  public Board setB_SolvedTime(Object value)
  {
    return set(B_SOLVED_TIME, value);
  }
  
  public <T> T getB_WrongNum()
  {
    return get(B_WRONG_NUM);
  }
  
  public Board setB_WrongNum(Object value)
  {
    return set(B_WRONG_NUM, value);
  }
  
  public <T> T getC_SolvedTime()
  {
    return get(C_SOLVED_TIME);
  }
  
  public Board setC_SolvedTime(Object value)
  {
    return set(C_SOLVED_TIME, value);
  }
  
  public <T> T getC_WrongNum()
  {
    return get(C_WRONG_NUM);
  }
  
  public Board setC_WrongNum(Object value)
  {
    return set(C_WRONG_NUM, value);
  }
  
  public <T> T getD_SolvedTime()
  {
    return get(D_SOLVED_TIME);
  }
  
  public Board setD_SolvedTime(Object value)
  {
    return set(D_SOLVED_TIME, value);
  }
  
  public <T> T getD_WrongNum()
  {
    return get(D_WRONG_NUM);
  }
  
  public Board setD_WrongNum(Object value)
  {
    return set(D_WRONG_NUM, value);
  }
  
  public <T> T getE_SolvedTime()
  {
    return get(E_SOLVED_TIME);
  }
  
  public Board setE_SolvedTime(Object value)
  {
    return set(E_SOLVED_TIME, value);
  }
  
  public <T> T getE_WrongNum()
  {
    return get(E_WRONG_NUM);
  }
  
  public Board setE_WrongNum(Object value)
  {
    return set(E_WRONG_NUM, value);
  }
  
  public <T> T getF_SolvedTime()
  {
    return get(F_SOLVED_TIME);
  }
  
  public Board setF_SolvedTime(Object value)
  {
    return set(F_SOLVED_TIME, value);
  }
  
  public <T> T getF_WrongNum()
  {
    return get(F_WRONG_NUM);
  }
  
  public Board setF_WrongNum(Object value)
  {
    return set(F_WRONG_NUM, value);
  }
  
  public <T> T getG_SolvedTime()
  {
    return get(G_SOLVED_TIME);
  }
  
  public Board setG_SolvedTime(Object value)
  {
    return set(G_SOLVED_TIME, value);
  }
  
  public <T> T getG_WrongNum()
  {
    return get(G_WRONG_NUM);
  }
  
  public Board setG_WrongNum(Object value)
  {
    return set(G_WRONG_NUM, value);
  }
  
  public <T> T getH_SolvedTime()
  {
    return get(H_SOLVED_TIME);
  }
  
  public Board setH_SolvedTime(Object value)
  {
    return set(H_SOLVED_TIME, value);
  }
  
  public <T> T getH_WrongNum()
  {
    return get(H_WRONG_NUM);
  }
  
  public Board setH_WrongNum(Object value)
  {
    return set(H_WRONG_NUM, value);
  }
  
  public <T> T getI_SolvedTime()
  {
    return get(I_SOLVED_TIME);
  }
  
  public Board setI_SolvedTime(Object value)
  {
    return set(I_SOLVED_TIME, value);
  }
  
  public <T> T getI_WrongNum()
  {
    return get(I_WRONG_NUM);
  }
  
  public Board setI_WrongNum(Object value)
  {
    return set(I_WRONG_NUM, value);
  }
  
  public <T> T getJ_SolvedTime()
  {
    return get(J_SOLVED_TIME);
  }
  
  public Board setJ_SolvedTime(Object value)
  {
    return set(J_SOLVED_TIME, value);
  }
  
  public <T> T getJ_WrongNum()
  {
    return get(J_WRONG_NUM);
  }
  
  public Board setJ_WrongNum(Object value)
  {
    return set(J_WRONG_NUM, value);
  }
  
  public <T> T getK_SolvedTime()
  {
    return get(K_SOLVED_TIME);
  }
  
  public Board setK_SolvedTime(Object value)
  {
    return set(K_SOLVED_TIME, value);
  }
  
  public <T> T getK_WrongNum()
  {
    return get(K_WRONG_NUM);
  }
  
  public Board setK_WrongNum(Object value)
  {
    return set(K_WRONG_NUM, value);
  }
  
  public <T> T getL_SolvedTime()
  {
    return get(L_SOLVED_TIME);
  }
  
  public Board setL_SolvedTime(Object value)
  {
    return set(L_SOLVED_TIME, value);
  }
  
  public <T> T getL_WrongNum()
  {
    return get(L_WRONG_NUM);
  }
  
  public Board setL_WrongNum(Object value)
  {
    return set(L_WRONG_NUM, value);
  }
  
  public <T> T getM_SolvedTime()
  {
    return get(M_SOLVED_TIME);
  }
  
  public Board setM_SolvedTime(Object value)
  {
    return set(M_SOLVED_TIME, value);
  }
  
  public <T> T getM_WrongNum()
  {
    return get(M_WRONG_NUM);
  }
  
  public Board setM_WrongNum(Object value)
  {
    return set(M_WRONG_NUM, value);
  }
  
  public <T> T getN_SolvedTime()
  {
    return get(N_SOLVED_TIME);
  }
  
  public Board setN_SolvedTime(Object value)
  {
    return set(N_SOLVED_TIME, value);
  }
  
  public <T> T getN_WrongNum()
  {
    return get(N_WRONG_NUM);
  }
  
  public Board setN_WrongNum(Object value)
  {
    return set(N_WRONG_NUM, value);
  }
  
  public <T> T getO_SolvedTime()
  {
    return get(O_SOLVED_TIME);
  }
  
  public Board setO_SolvedTime(Object value)
  {
    return set(O_SOLVED_TIME, value);
  }
  
  public <T> T getO_WrongNum()
  {
    return get(O_WRONG_NUM);
  }
  
  public Board setO_WrongNum(Object value)
  {
    return set(O_WRONG_NUM, value);
  }
  
  public <T> T getP_SolvedTime()
  {
    return get(P_SOLVED_TIME);
  }
  
  public Board setP_SolvedTime(Object value)
  {
    return set(P_SOLVED_TIME, value);
  }
  
  public <T> T getP_WrongNum()
  {
    return get(P_WRONG_NUM);
  }
  
  public Board setP_WrongNum(Object value)
  {
    return set(P_WRONG_NUM, value);
  }
  
  public <T> T getQ_SolvedTime()
  {
    return get(Q_SOLVED_TIME);
  }
  
  public Board setQ_SolvedTime(Object value)
  {
    return set(Q_SOLVED_TIME, value);
  }
  
  public <T> T getQ_WrongNum()
  {
    return get(Q_WRONG_NUM);
  }
  
  public Board setQ_WrongNum(Object value)
  {
    return set(Q_WRONG_NUM, value);
  }
  
  public <T> T getR_SolvedTime()
  {
    return get(R_SOLVED_TIME);
  }
  
  public Board setR_SolvedTime(Object value)
  {
    return set(R_SOLVED_TIME, value);
  }
  
  public <T> T getR_WrongNum()
  {
    return get(R_WRONG_NUM);
  }
  
  public Board setR_WrongNum(Object value)
  {
    return set(R_WRONG_NUM, value);
  }
  
  public <T> T getS_SolvedTime()
  {
    return get(S_SOLVED_TIME);
  }
  
  public Board setS_SolvedTime(Object value)
  {
    return set(S_SOLVED_TIME, value);
  }
  
  public <T> T getS_WrongNum()
  {
    return get(S_WRONG_NUM);
  }
  
  public Board setS_WrongNum(Object value)
  {
    return set(S_WRONG_NUM, value);
  }
  
  public <T> T getT_SolvedTime()
  {
    return get(T_SOLVED_TIME);
  }
  
  public Board setT_SolvedTime(Object value)
  {
    return set(T_SOLVED_TIME, value);
  }
  
  public <T> T getT_WrongNum()
  {
    return get(T_WRONG_NUM);
  }
  
  public Board setT_WrongNum(Object value)
  {
    return set(T_WRONG_NUM, value);
  }
  
  public <T> T getU_SolvedTime()
  {
    return get(U_SOLVED_TIME);
  }
  
  public Board setU_SolvedTime(Object value)
  {
    return set(U_SOLVED_TIME, value);
  }
  
  public <T> T getU_WrongNum()
  {
    return get(U_WRONG_NUM);
  }
  
  public Board setU_WrongNum(Object value)
  {
    return set(U_WRONG_NUM, value);
  }
  
  public <T> T getV_SolvedTime()
  {
    return get(V_SOLVED_TIME);
  }
  
  public Board setV_SolvedTime(Object value)
  {
    return set(V_SOLVED_TIME, value);
  }
  
  public <T> T getV_WrongNum()
  {
    return get(V_WRONG_NUM);
  }
  
  public Board setV_WrongNum(Object value)
  {
    return set(V_WRONG_NUM, value);
  }
  
  public <T> T getW_SolvedTime()
  {
    return get(W_SOLVED_TIME);
  }
  
  public Board setW_SolvedTime(Object value)
  {
    return set(W_SOLVED_TIME, value);
  }
  
  public <T> T getW_WrongNum()
  {
    return get(W_WRONG_NUM);
  }
  
  public Board setW_WrongNum(Object value)
  {
    return set(W_WRONG_NUM, value);
  }
  
  public <T> T getX_SolvedTime()
  {
    return get(X_SOLVED_TIME);
  }
  
  public Board setX_SolvedTime(Object value)
  {
    return set(X_SOLVED_TIME, value);
  }
  
  public <T> T getX_WrongNum()
  {
    return get(X_WRONG_NUM);
  }
  
  public Board setX_WrongNum(Object value)
  {
    return set(X_WRONG_NUM, value);
  }
  
  public <T> T getY_SolvedTime()
  {
    return get(Y_SOLVED_TIME);
  }
  
  public Board setY_SolvedTime(Object value)
  {
    return set(Y_SOLVED_TIME, value);
  }
  
  public <T> T getY_WrongNum()
  {
    return get(Y_WRONG_NUM);
  }
  
  public Board setY_WrongNum(Object value)
  {
    return set(Y_WRONG_NUM, value);
  }
  
  public <T> T getZ_SolvedTime()
  {
    return get(Z_SOLVED_TIME);
  }
  
  public Board setZ_SolvedTime(Object value)
  {
    return set(Z_SOLVED_TIME, value);
  }
  
  public <T> T getZ_WrongNum()
  {
    return get(Z_WRONG_NUM);
  }
  
  public Board setZ_WrongNum(Object value)
  {
    return set(Z_WRONG_NUM, value);
  }
  
}
