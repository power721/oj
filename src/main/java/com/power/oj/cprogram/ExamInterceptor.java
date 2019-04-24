package com.power.oj.cprogram;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.power.oj.contest.ContestService;
import com.power.oj.contest.model.ContestModel;
import com.power.oj.user.UserService;

/**
 * Created by w703710691d on 17-6-21.
 */
public class ExamInterceptor implements Interceptor  {

    public static boolean CanAccess(Integer cid) {
//        if(CProgramService.isTeacher()) return true;
//        ContestModel contest = ContestService.me().getContest(cid);
//        if(contest.getType() <= ContestModel.TYPE_EXPERIMENT) {
//            return true;
//        }
//        else {
//            Integer uid = UserService.me().getCurrentUid();
//            if(uid == null) return false;
//            Integer id = Db.queryInt("select id from contest_user where uid = ? and cid = ?", uid, cid);
//            return  id != null;
//        }
        return true;
    }
    @Override
    public void intercept(Invocation invocation) {
        Controller con = invocation.getController();
        Integer cid = con.getParaToInt(0);
        if(cid == null) {
            cid = con.getParaToInt("cid");
        }
        if(cid == null) {
            cid = con.getParaToInt("solution.cid");
        }
        if(CanAccess(cid)) {
            invocation.invoke();
        }
        else {
            con.redirect("/cprogram/password/" + cid);
        }
    }
}
