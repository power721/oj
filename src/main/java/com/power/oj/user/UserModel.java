package com.power.oj.user;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class UserModel extends Model<UserModel> {
    public static final UserModel dao = new UserModel();
    public static final String TABLE_NAME = "user";
    public static final String UID = "uid";
    public static final String TID = "tid";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String NICK = "nick";
    public static final String REAL_NAME = "realName";
    public static final String REG_EMAIL = "regEmail";
    public static final String EMAIL = "email";
    public static final String EMAIL_VERIFIED = "emailVerified";
    public static final String LANGUAGE = "language";
    public static final String SCHOOL = "school";
    public static final String SOLVED = "solved";
    public static final String ACCEPTED = "accepted";
    public static final String SUBMISSION = "submission";
    public static final String ATIME = "atime";
    public static final String CTIME = "ctime";
    public static final String MTIME = "mtime";
    public static final String LOGIN_TIME = "loginTime";
    public static final String LOGIN_IP = "loginIP";
    public static final String PHONE = "phone";
    public static final String QQ = "qq";
    public static final String BLOG = "blog";
    public static final String GENDER = "gender";
    public static final String COME_FROM = "comeFrom";
    public static final String ONLINE = "online";
    public static final String AVATAR = "avatar";
    public static final String SIGNATURE = "signature";
    public static final String SHARE_CODE = "shareCode";
    public static final String STATUS = "status";
    public static final String TOKEN = "token";
    /**
     *
     */
    private static final long serialVersionUID = 7553341600472286034L;

    public Integer getUidByName(String name) {
        Integer uid = 0;
        UserModel userModel = findFirst("SELECT uid FROM user WHERE name=? LIMIT 1", name);
        if (userModel != null)
            uid = userModel.getUid();
        return uid;
    }

    public UserModel getUserExt(Integer uid) {
        return findFirst("SELECT * FROM user u LEFT JOIN user_ext ue ON u.uid=ue.uid WHERE u.uid=? LIMIT 1", uid);
    }

    public UserModel getUserByName(String name) {
        return findFirst("SELECT * FROM user WHERE name=? LIMIT 1", name);
    }

    public UserModel getUserByEmail(String email) {
        return findFirst("SELECT * FROM user WHERE email=? LIMIT 1", email);
    }

    public UserModel getUserByNameAndEmail(String name, String email) {
        return findFirst("SELECT * FROM user WHERE name=? AND email=? LIMIT 1", name, email);
    }

    public UserModel getUserInfoByName(String name) {
        return findFirst(
            "SELECT uid,name,nick,avatar,school,blog,online,shareCode,gender,submission,solved,loginTime,ctime,signature,comeFrom FROM user WHERE name=? LIMIT 1",
            name);
    }

    public UserModel getUserInfoByUid(Integer uid) {
        return findFirst(
            "SELECT uid,name,nick,avatar,school,blog,online,shareCode,gender,submission,solved,loginTime,ctime,signature,comeFrom FROM user WHERE uid=? LIMIT 1",
            uid);
    }

    public int getUserRank(Integer uid) {
        int userRank = 0;
        Object object = findFirst(
            "SELECT rk FROM (SELECT @rank:=@rank+1 AS rk,uid FROM user,(SELECT @rank:=0)r ORDER BY solved desc,submission)t_rank WHERE uid=? LIMIT 1",
            uid).get("rk");

        if (object instanceof Double) {
            double d = (Double) object;
            userRank = (int) d;
        } else if (object instanceof Long) {
            long l = (Long) object;
            userRank = (int) l;
        } else {
            userRank = (Integer) object;
        }
        return userRank;
    }

    public Page<UserModel> getUserRankList(int pageNumber, int pageSize) {
        Page<UserModel> userList =
            paginate(pageNumber, pageSize, "SELECT @rank:=@rank+1 AS rk,uid,name,nick,realName,solved,submission",
                "FROM user,(SELECT @rank:=?)r WHERE status=1 ORDER BY solved DESC,submission,uid",
                (pageNumber - 1) * pageSize);

        return userList;
    }


    /*
     * public List<Record> getUnsolvedProblems(Integer uid) { return Db.find(
     * "SELECT pid FROM solution WHERE uid=? AND result!=0 AND status=1 GROUP BY pid", uid); }
     */
    public boolean containsEmailExceptThis(Integer uid, String email) {
        return findFirst("SELECT email FROM user WHERE email=? AND uid!=? LIMIT 1", email, uid) != null;
    }

    public boolean containsUsernameExceptThis(Integer uid, String username) {
        return findFirst("SELECT name FROM user WHERE name=? AND uid!=? LIMIT 1", username, uid) != null;
    }

    public Integer getUid() {
        return getInt(UID);
    }

    public UserModel setUid(Integer value) {
        return set(UID, value);
    }

    public Integer getTid() {
        return getInt(TID);
    }

    public UserModel setTid(Integer value) {
        return set(TID, value);
    }

    public String getName() {
        return getStr(NAME);
    }

    public UserModel setName(String value) {
        return set(NAME, value);
    }

    public String getPassword() {
        return getStr(PASSWORD);
    }

    public UserModel setPassword(String value) {
        return set(PASSWORD, value);
    }

    public String getNick() {
        return getStr(NICK);
    }

    public UserModel setNick(String value) {
        return set(NICK, value);
    }

    public String getRealName() {
        return getStr(REAL_NAME);
    }

    public UserModel setRealName(String value) {
        return set(REAL_NAME, value);
    }

    public String getRegEmail() {
        return getStr(REG_EMAIL);
    }

    public UserModel setRegEmail(String value) {
        return set(REG_EMAIL, value);
    }

    public String getEmail() {
        return getStr(EMAIL);
    }

    public UserModel setEmail(String value) {
        return set(EMAIL, value);
    }

    public Boolean getEmailVerified() {
        return getBoolean(EMAIL_VERIFIED);
    }

    public UserModel setEmailVerified(Boolean value) {
        return set(EMAIL_VERIFIED, value);
    }

    public Integer getLanguage() {
        return getInt(LANGUAGE);
    }

    public UserModel setLanguage(Integer value) {
        return set(LANGUAGE, value);
    }

    public String getSchool() {
        return getStr(SCHOOL);
    }

    public UserModel setSchool(String value) {
        return set(SCHOOL, value);
    }

    public Integer getSolved() {
        return getInt(SOLVED);
    }

    public UserModel setSolved(Integer value) {
        return set(SOLVED, value);
    }

    public Integer getAccepted() {
        return getInt(ACCEPTED);
    }

    public UserModel setAccepted(Integer value) {
        return set(ACCEPTED, value);
    }

    public Integer getSubmission() {
        return getInt(SUBMISSION);
    }

    public UserModel setSubmission(Integer value) {
        return set(SUBMISSION, value);
    }

    public Integer getAtime() {
        return getInt(ATIME);
    }

    public UserModel setAtime(Integer value) {
        return set(ATIME, value);
    }

    public Integer getCtime() {
        return getInt(CTIME);
    }

    public UserModel setCtime(Integer value) {
        return set(CTIME, value);
    }

    public Integer getMtime() {
        return getInt(MTIME);
    }

    public UserModel setMtime(Integer value) {
        return set(MTIME, value);
    }

    public Integer getLoginTime() {
        return getInt(LOGIN_TIME);
    }

    public UserModel setLoginTime(Integer value) {
        return set(LOGIN_TIME, value);
    }

    public String getLoginIP() {
        return getStr(LOGIN_IP);
    }

    public UserModel setLoginIP(String value) {
        return set(LOGIN_IP, value);
    }

    public String getPhone() {
        return getStr(PHONE);
    }

    public UserModel setPhone(String value) {
        return set(PHONE, value);
    }

    public String getQQ() {
        return getStr(QQ);
    }

    public UserModel setQQ(String value) {
        return set(QQ, value);
    }

    public String getBlog() {
        return getStr(BLOG);
    }

    public UserModel setBlog(String value) {
        return set(BLOG, value);
    }

    public String getGender() {
        return getStr(GENDER);
    }

    public UserModel setGender(String value) {
        return set(GENDER, value);
    }

    public String getComeFrom() {
        return getStr(COME_FROM);
    }

    public UserModel setComeFrom(String value) {
        return set(COME_FROM, value);
    }

    public Integer getOnline() {
        return getInt(ONLINE);
    }

    public UserModel setOnline(Integer value) {
        return set(ONLINE, value);
    }

    public String getAvatar() {
        return getStr(AVATAR);
    }

    public UserModel setAvatar(String value) {
        return set(AVATAR, value);
    }

    public String getSignature() {
        return getStr(SIGNATURE);
    }

    public UserModel setSignature(String value) {
        return set(SIGNATURE, value);
    }

    public Boolean getShareCode() {
        return getBoolean(SHARE_CODE);
    }

    public UserModel setShareCode(Boolean value) {
        return set(SHARE_CODE, value);
    }

    public Integer getStatus() {
        return getInt(STATUS);
    }

    public UserModel setStatus(Integer value) {
        return set(STATUS, value);
    }

    public String getToken() {
        return getStr(TOKEN);
    }

    public UserModel setToken(String value) {
        return set(TOKEN, value);
    }

}
