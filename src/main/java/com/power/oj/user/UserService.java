package com.power.oj.user;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.power.oj.api.oauth.WebLoginModel;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.bean.ResultType;
import com.power.oj.core.service.OjService;
import com.power.oj.core.service.SessionService;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.solution.SolutionModel;
import com.power.oj.util.FileKit;
import com.power.oj.util.Tool;
import jodd.io.FileUtil;
import jodd.io.ZipUtil;
import jodd.util.BCrypt;
import jodd.util.HtmlEncoder;
import jodd.util.StringUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private static final UserModel dao = UserModel.dao;
    private static final UserService me = new UserService();
    private static final OjService ojService = OjService.me();
    public static final int ROOT_ROLE_ID = 1;
    public static final String ROOT_ROLE_NAME = "root";

    private UserService() {
    }

    public static UserService me() {
        return me;
    }

    /**
     * User login with name and password.
     *
     * @param name       user name.
     * @param password   user password.
     * @param rememberMe ture if remember user.
     * @return true if login successfully, otherwise false.
     */
    public boolean login(String name, String password, boolean rememberMe) {
        boolean success = true;
        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        token.setRememberMe(rememberMe);

        try {
            // currentUser.logout();
            currentUser.login(token);

            SessionService.me().updateLogin();
        } catch (AuthenticationException e) {
            if (OjConfig.isDevMode()) {
                log.debug("User signin failed: " + name, e);
            } else {
                log.warn("User signin failed: " + name);
            }

            success = false;
        }
        updateLogin(name, success);

        return success;
    }

    public boolean autoLogin(UserModel userModel, boolean rememberMe) {
        boolean success = true;
        String name = userModel.getName();
        String password = userModel.getPassword();
        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        token.setRememberMe(rememberMe);

        try {
            currentUser.login(token);

            SessionService.me().updateLogin();
        } catch (AuthenticationException e) {
            if (OjConfig.isDevMode()) {
                log.debug("User signin failed: " + name, e);
            } else {
                log.warn("User signin failed: " + name);
            }

            success = false;
        }
        updateLogin(name, success);

        return success;
    }

    /**
     * User logout in Shiro session.
     */
    public void logout() {
        UserModel userModel = getCurrentUser();
        if (userModel != null) {
            evictCache(userModel.getUid());
        }

        ShiroKit.getSubject().logout();
    }

    /**
     * user signup
     *
     * @param userModel
     * @return
     * @throws Exception
     */
    public boolean signup(UserModel userModel) {
        String name = HtmlEncoder.text(userModel.getName());
        String password = BCrypt.hashpw(userModel.getPassword(), BCrypt.gensalt());
        String email = userModel.getEmail();
        String verifyEmailToken = UUID.randomUUID().toString();

        int ctime = OjConfig.timeStamp;
        UserModel newUser = new UserModel();
        newUser.setName(name).setPassword(password).setEmail(email).setRegEmail(email);
        newUser.setToken(verifyEmailToken).setCtime(ctime).setMtime(ctime);

        if (newUser.save()) {
            int uid = newUser.getUid();
            Db.update("INSERT INTO user_role (rid,uid) SELECT id,? FROM role WHERE name='user'", uid);

            UserExtModel userExt = new UserExtModel();
            userExt.setUid(uid).save();
            // password = userModel.getStr("pass");
            // return login(name, password, false);

            ojService.sendVerifyEmail(name, email, verifyEmailToken);
            return true;
        }

        return false;
    }

    /**
     * signup with external account
     *
     * @param email
     * @param webLogin
     * @return user model
     * @throws Exception
     */
    public UserModel signup(String email, WebLoginModel webLogin) throws Exception {
        String name = HtmlEncoder.text(email);
        String pass = Tool.randomPassword(9);
        String password = BCrypt.hashpw(pass, BCrypt.gensalt());
        String avatar = webLogin.getAvatar();
        String verifyEmailToken = UUID.randomUUID().toString();
        int ctime = OjConfig.timeStamp;

        UserModel newUser = new UserModel();
        newUser.setName(name).setPassword(password).setEmail(email).setRegEmail(email).setToken(verifyEmailToken);
        newUser.setNick(HtmlEncoder.text(webLogin.getNick())).setAvatar(avatar).setCtime(ctime).setMtime(ctime);

        if (newUser.save()) {
            webLogin.setStatus(true).update();

            Integer uid = newUser.getUid();
            Calendar cal = Calendar.getInstance();
            name = webLogin.getType() + cal.get(Calendar.YEAR) + uid;
            newUser.setName(name).update();

            Db.update("INSERT INTO user_role (rid,uid) SELECT id,? FROM role WHERE name='user'", uid);

            UserExtModel userExt = new UserExtModel();
            userExt.setUid(uid).save();

            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put(OjConstants.BASE_URL, OjConfig.getBaseURL());
            paras.put(OjConstants.SITE_TITLE, OjConfig.siteTitle);
            paras.put("nick", webLogin.getNick());
            paras.put("type", webLogin.getType());
            paras.put("name", name);
            paras.put("token", verifyEmailToken);
            paras.put("password", pass);
            paras.put("ctime", OjConfig.timeStamp);
            paras.put("expires", OjConstants.VERIFY_EMAIL_EXPIRES_TIME / OjConstants.MINUTE_IN_MILLISECONDS);

            ojService.sendVerifyEmail(name, email, paras);
        }

        return newUser;
    }

    /**
     * update user
     *
     * @param userModel
     * @return true if success
     */
    public boolean updateUser(UserModel userModel) {
        UserModel newUser = getCurrentUser();
        String password = userModel.getPassword();

        if (StringUtil.isNotBlank(password)) {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
            newUser.setPassword(password);
        }

        newUser.setUid(getCurrentUid());
        if (ShiroKit.hasPermission("user:sp:nick")) {
            newUser.setNick(userModel.getNick());
        } else {
            newUser.setNick(HtmlEncoder.text(userModel.getNick()));
        }
        newUser.setSchool(HtmlEncoder.text(userModel.getSchool()));
        newUser.setRealName(HtmlEncoder.text(userModel.getRealName()));
        newUser.setBlog(HtmlEncoder.text(userModel.getBlog()));
        newUser.setEmail(HtmlEncoder.text(userModel.getEmail()));
        newUser.setPhone(HtmlEncoder.text(userModel.getPhone()));
        newUser.setGender(HtmlEncoder.text(userModel.getGender()));
        newUser.setLanguage(userModel.getLanguage());
        newUser.setQQ(HtmlEncoder.text(userModel.getQQ()));
        newUser.setMtime(OjConfig.timeStamp);
        updateCache(newUser);

        return newUser.update();
    }

    /**
     * Update user login time and loginlog.
     *
     * @param name    user name.
     * @param success true if user login sucessfully.
     * @return true if success
     */
    public boolean updateLogin(String name, boolean success) {
        Record loginLog = new Record();
        String ip = SessionService.me().getHost();

        if (success) {
            UserModel userModel = getCurrentUser();
            if (userModel.getEmailVerified()) {
                userModel.setToken(null); // resetPassword token
            }
            userModel.setLoginTime(OjConfig.timeStamp).setLoginIP(ip).update();
            updateCache(userModel);

            loginLog.set("uid", userModel.getUid());
        }

        loginLog.set("name", name).set("ip", ip).set("ctime", OjConfig.timeStamp).set("success", success);
        return Db.save("loginlog", loginLog);
    }

    /**
     * @param userModel
     * @param email
     * @return
     * @throws Exception
     */
    public boolean updateEmail(UserModel userModel, String email) throws Exception {
        String name = userModel.getName();
        String token = UUID.randomUUID().toString();

        userModel.setEmail(email).setEmailVerified(false);
        userModel.setToken(token).setMtime(OjConfig.timeStamp);
        updateCache(userModel);

        ojService.sendVerifyEmail(name, email, token);

        return userModel.update();
    }

    /* for user center */
    private void addExp(UserExtModel userExtModel, int incExp) {
        int exp = userExtModel.getExperience() + incExp;
        int credit = userExtModel.getCredit() + incExp;
        int level = Arrays.binarySearch(OjConfig.level.toArray(), exp);
        level = level < 0 ? -level : level + 2;

        userExtModel.setCredit(credit).setExperience(exp).setLevel(level); // no
        // update
    }

    /**
     * @param userExtModel
     * @return
     */
    public int checkin(UserExtModel userExtModel) {
        int timestamp = Tool.getDayTimestamp();
        int checkin = userExtModel.getCheckin();
        int checkinTimes = userExtModel.getCheckinTimes();
        int level = userExtModel.getLevel();

        if (checkin < timestamp) {
            checkinTimes = checkin + OjConstants.DAY_TIMESTAMP < timestamp ? 1 : checkinTimes + 1;
            int incExp = Math.min(checkinTimes, level);
            int totalCheckin = userExtModel.getTotalCheckin() + 1;

            addExp(userExtModel, incExp);
            userExtModel.setCheckin(OjConfig.timeStamp).setCheckinTimes(checkinTimes).setTotalCheckin(totalCheckin)
                .update();
            return incExp;
        }

        return 0;
    }

    /**
     * @param userModel
     * @return
     */
    public boolean isCheckin(UserModel userModel) {
        int checkin = userModel.getInt("checkin"); // UserModel does not have
        // "checkin"

        if (checkin < Tool.getDayTimestamp()) {
            return false;
        } else {
            userModel.put("isCheckin", checkin); // use lastCheckin?
            return true;
        }
    }

    /**
     * @param srcFile
     * @return
     * @throws IOException
     */
    public String saveAvatar(File srcFile) throws IOException {
        UserModel userModel = getCurrentUser();
        //        String rootPath = PathKit.getWebRootPath() + File.separator;
        String srcFileName = srcFile.getAbsolutePath();
        String ext = FileKit.getFileType(srcFileName);
        String destFileName = OjConfig.userAvatarPath + File.separator + userModel.getUid() + ext;

        File destFile = new File(destFileName);

        log.info(srcFile.getAbsolutePath() + " --> " + destFile.getAbsolutePath());
        FileUtil.moveFile(srcFile, destFile);
        if (!destFile.exists()) {
            throw new IOException("save avatar to " + destFile.getAbsolutePath() + " failed!");
        }

        destFileName = destFileName.substring(OjConfig.webRootPath.length()).replace("\\", "/");
        userModel.setAvatar(destFileName).update();
        updateCache(userModel);

        return destFileName;
    } /* for user center end */

	/* for solution */

    /**
     * @param uid
     * @return
     */
    public boolean incSubmission(Integer uid) {
        UserModel userModel = getUser(uid);
        userModel.setSubmission(userModel.getSubmission() + 1);
        updateCache(userModel);

        return userModel.update();
    }

    /**
     * @param solutionModel
     * @return
     */
    public boolean incAccepted(SolutionModel solutionModel) {
        Integer pid = solutionModel.getPid();
        Integer sid = solutionModel.getSid();
        Integer uid = solutionModel.getUid();
        UserModel userModel = getUser(uid);
        userModel.setAccepted(userModel.getAccepted() + 1);
        Integer lastAccepted =
            Db.queryInt("SELECT sid FROM solution WHERE pid=? AND uid=? AND sid<? AND result=? AND status=1 LIMIT 1",
                pid, uid, sid, ResultType.AC);
        if (lastAccepted == null) {
            userModel.setSolved(userModel.getSolved() + 1);
        }
        updateCache(userModel);

        return userModel.update();
    }

	/* for rejudge */

    /**
     * @param solutionModel
     * @return
     */
    public boolean revertAccepted(SolutionModel solutionModel) {
        if (solutionModel.getResult() != ResultType.AC) {
            return false;
        }

        Integer pid = solutionModel.getPid();
        Integer sid = solutionModel.getSid();
        Integer uid = solutionModel.getUid();
        UserModel userModel = getUser(uid);
        userModel.setAccepted(userModel.getAccepted() - 1);
        Integer lastAccepted =
            Db.queryInt("SELECT sid FROM solution WHERE pid=? AND uid=? AND sid<? AND result=? AND status=1 LIMIT 1",
                pid, uid, sid, ResultType.AC);
        if (lastAccepted == null) {
            userModel.setSolved(userModel.getSolved() - 1);
        }
        updateCache(userModel);

        return userModel.update();
    } /* for solution end */

    /**
     * Build user statistics.
     *
     * @param userModel the user.
     * @return true if success
     */
    public boolean build(UserModel userModel) {
        Integer uid = userModel.getUid();

        userModel
            .set("submission", Db.queryLong("SELECT COUNT(*) FROM solution WHERE uid=? AND status=1 LIMIT 1", uid));
        userModel.set("accepted",
            Db.queryLong("SELECT COUNT(*) FROM solution WHERE uid=? AND result=? AND status=1 LIMIT 1", uid,
                ResultType.AC));
        userModel.set("solved",
            Db.queryLong("SELECT COUNT(DISTINCT pid) FROM solution WHERE uid=? AND result=? AND status=1 LIMIT 1", uid,
                ResultType.AC));
        updateCache(userModel);

        return userModel.update();
    }

    /**
     * Reset user password for recover account.
     *
     * @param name     user name.
     * @param password new password.
     * @return
     */
    public boolean resetPassword(String name, String password) {
        UserModel userModel = dao.getUserByName(name);

        userModel.setToken(null).setEmailVerified(true).setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        updateCache(userModel);
        return userModel.update();
    }

    /**
     * generate token for reset password.
     *
     * @param name user name
     * @return the token
     */
    public String genToken(String name) {
        String token = UUID.randomUUID().toString();
        UserModel userModel = getUserByName(name);

        userModel.setToken(token).setMtime(OjConfig.timeStamp).update();
        updateCache(userModel);

        return token;
    }

    /**
     * Check if the token is valid for reset password.
     *
     * @param name  user name.
     * @param token reset token.
     * @return true if the token is valid.
     */
    public boolean checkResetToken(String name, String token) {
        UserModel userModel = dao.getUserByName(name);

        if (userModel != null && token != null && token.equals(userModel.getToken())) {
            if (OjConfig.timeStamp - userModel.getMtime() <= OjConstants.RESET_PASSWORD_EXPIRES_TIME) {
                return true;
            } else {
                userModel.setToken(null).update();
                updateCache(userModel);
            }
        }

        return false;
    }

    /**
     * @param name
     * @param token
     * @return
     */
    public boolean verifyEmail(String name, String token) {
        UserModel userModel = dao.getUserByName(name);

        if (userModel != null && token != null && token.equals(userModel.getToken()) && !userModel.getEmailVerified()) {
            if (OjConfig.timeStamp - userModel.getMtime() <= OjConstants.VERIFY_EMAIL_EXPIRES_TIME) {
                log.info(String.valueOf(OjConfig.timeStamp - userModel.getMtime()));
                userModel.setToken(null).setEmailVerified(true).update();
                updateCache(userModel);

                return true;
            } else {
                userModel.setToken(null).update();
                updateCache(userModel);

                log.info("token expires");
                return false;
            }
        }

        log.info("token invlidate: " + name + "  " + token);
        if (OjConfig.isDevMode()) {
            log.info(userModel.toString());
        }

        return false;
    }

    /**
     * Search user by key word in scope.
     *
     * @param scope "all", "name", "nick", "school", "email".
     * @param word  key word.
     * @return the list of users.
     */
    public Page<UserModel> searchUser(int pageNumber, int pageSize, String scope, String word) {
        String select = "SELECT uid,name,nick,school,solved,submission";
        Page<UserModel> userList = null;
        List<Object> paras = new ArrayList<Object>();

        if (StringUtil.isBlank(word)) {
            word = "No Such User!";
        }

        word = "%" + word + "%";
        StringBuilder sb = new StringBuilder("FROM user WHERE (");

        if (StringUtil.isNotBlank(scope)) {
            String scopes[] = {"name", "nick", "school", "email"};
            if (StringUtil.equalsOneIgnoreCase(scope, scopes) == -1) {
                return null;
            }
            sb.append(scope).append(" LIKE ? ");
            paras.add(word);
        } else {
            sb.append("name LIKE ? OR nick LIKE ? OR school LIKE ? OR email LIKE ?");
            for (int i = 0; i < 4; ++i) {
                paras.add(word);
            }
        }
        sb.append(") AND status=1 ORDER BY solved DESC,submission,uid");

        userList = dao.paginate(pageNumber, pageSize, select, sb.toString(), paras.toArray());

        return userList;
    }

    /**
     * get user profile by name.
     *
     * @param name string of user name.
     * @return UseModel with submitted problems.
     */
    public UserModel getUserProfile(String name) {
        UserModel userModel = null;

        if (name == null) {
            userModel = getCurrentUser();
        } else {
            userModel = getUserByName(name);
        }

        if (userModel != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userModel.put("createTime_t", sdf.format(new Date(userModel.getCtime() * 1000L)));
            userModel.put("loginTime_t", sdf.format(new Date(userModel.getLoginTime() * 1000L)));
            userModel.put("rank", getUserRank(userModel.getUid()));
            userModel.put("problems", getSubmittedProblems(userModel.getUid()));
        }

        return userModel;
    }

    /**
     * get user's login logs.
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public Page<Record> getLoginlog(int pageNumber, int pageSize) {
        UserModel userModel = getCurrentUser();
        Integer uid = userModel.getUid();
        String name = userModel.getName();

        Page<Record> logs =
            Db.paginate(pageNumber, pageSize, "SELECT *", "FROM loginlog WHERE uid=? OR name=? ORDER BY ctime DESC",
                uid, name);
        return logs;
    }

    /**
     * archive user source code.
     *
     * @return zip file.
     * @throws IOException
     */
    public File archiveCode() throws IOException {
        UserModel userModel = getCurrentUser();
        Integer uid = userModel.getUid();
        List<Record> codes = getSolvedProblems(uid);
        String userDir = OjConfig.downloadPath + File.separator + userModel.getName();
        File userDirFile = new File(userDir);
        FileUtil.mkdirs(userDirFile);

        for (Record code : codes) {
            String problemDir = userDir + File.separator + code.get("pid");
            FileUtil.mkdirs(problemDir);

            String ext = OjConfig.languageType.get(code.get("language")).getExt();
            StringBuilder sb = new StringBuilder(10);
            sb.append(problemDir).append(File.separator).append(code.get("sid")).append("_");
            sb.append(code.get("time")).append("MS_").append(code.get("memory")).append("KB").append("").append(ext);

            File file = new File(sb.toString());
            if (file.createNewFile() == false) {
                log.info("Create file failed: " + sb.toString());
                continue;
            }

            FileUtil.writeString(file, code.getStr("source"));
        }

        ZipUtil.zip(userDirFile);
        File zipFile = new File(userDirFile.getAbsolutePath() + ".zip");

        return zipFile;
    }

    /**
     * Get current uid form Shiro.
     *
     * @return the uid of current user or null.
     */
    public Integer getCurrentUid() {
        Subject currentUser = ShiroKit.getSubject();
        if (currentUser == null) {
            return null;
        }

        Object principal = currentUser.getPrincipal();
        if (principal == null) {
            return null;
        }

        return (Integer) principal;
    }

    /**
     * @param uid
     * @return
     */
    public UserModel getUser(Integer uid) {
        UserModel userModel = null;
        if (OjConfig.isDevMode()) {
            userModel = getUserByUid(uid);
        } else {
            userModel = dao.findFirstByCache("user", uid, "SELECT * FROM user WHERE uid=?", uid);
        }
        if (userModel != null) {
            userModel.remove("token").remove("password").remove("data");
        }

        return userModel;
    }

    /**
     * Get current user by uid.
     *
     * @return current user or null.
     */
    public UserModel getCurrentUser() {
        return getUser(getCurrentUid());
    }

    /**
     * @return user
     */
    public UserModel getCurrentUserExt() {
        UserModel userModel = dao.getUserExt(getCurrentUid());
        int exp = userModel.getInt("experience"); // UserModel does not have
        // experience
        int level = userModel.getInt("level"); // UserModel does not have level
        int lastExp = 0;

        if (level > 1) {
            lastExp = OjConfig.level.get(level - 2);
        }

        int nextExp = (1 << 31) - 1;
        if (level - 1 < OjConfig.level.size()) {
            nextExp = OjConfig.level.get(level - 1);
        }

        userModel.put("nextExp", nextExp);
        userModel.put("percent", (int) ((exp - lastExp) / (double) (nextExp - lastExp) * 100));
        // TODO cache?

        return userModel;
    }

    public UserModel getUserByUid(Integer uid) {
        return dao.findById(uid);
    }

    public UserModel getUserByName(String name) {
        return dao.getUserByName(name);
    }

    public Integer getUidByName(String name) {
        UserModel userModel = getUserByName(name);

        if (userModel != null) {
            return userModel.getUid();
        }
        return null;
    }

    public UserModel getUserByEmail(String email) {
        return dao.getUserByEmail(email);
    }

    public UserModel getUserByNameAndEmail(String name, String email) {
        return dao.getUserByNameAndEmail(name, email);
    }

    public UserModel getUserInfoByName(String name) {
        return dao.getUserInfoByName(name);
    }

    public UserModel getUserInfoByUid(Integer uid) {
        return dao.getUserInfoByUid(uid);
    }

    public int getUserRank(Integer uid) {
        return dao.getUserRank(uid);
    }

    public Page<UserModel> getUserRankList(int pageNumber, int pageSize) {
        return dao.getUserRankList(pageNumber, pageSize);
    }

    public Page<UserModel> getUserListDataTables(int pageNumber, int pageSize, String sSortName, String sSortDir,
        String sSearch) {
        List<Object> param = new ArrayList<Object>();
        String sql = "SELECT *";
        StringBuilder sb = new StringBuilder().append("FROM user WHERE 1=1");

        if (StringUtil.isNotEmpty(sSearch)) {
            sb.append(" AND (name LIKE ? OR realName LIKE ?)");
            param.add("%" + sSearch + "%");
            param.add("%" + sSearch + "%");
        }
        sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", uid");

        return dao.paginate(pageNumber, pageSize, sql, sb.toString(), param.toArray());
    }

    public Page<UserModel> getUserRoleListDataTables(int pageNumber, int pageSize, String sSortName, String sSortDir,
        String sSearch) {
        List<Object> param = new ArrayList<Object>();
        String sql = "SELECT u.uid,u.name,u.nick,r.name AS role,u.ctime,r.id";
        StringBuilder sb = new StringBuilder().append("FROM user_role ur LEFT JOIN user u ON u.uid=ur.uid LEFT JOIN role r ON r.id=ur.rid WHERE 1=1");

        if (StringUtil.isNotEmpty(sSearch)) {
            sb.append(" AND (u.name LIKE ? OR u.realName LIKE ?)");
            param.add("%" + sSearch + "%");
            param.add("%" + sSearch + "%");
        }
        sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", u.uid");

        return dao.paginate(pageNumber, pageSize, sql, sb.toString(), param.toArray());
    }

    public void changeUserRole(int uid, int rid) {
        if (rid == ROOT_ROLE_ID && !ShiroKit.hasRole(ROOT_ROLE_NAME)) {
            return;
        }
        int id = Db.queryInt("SELECT rid FROM user_role WHERE uid=?", uid);
        if (id == ROOT_ROLE_ID && !ShiroKit.hasRole(ROOT_ROLE_NAME)) {
            return;
        }
        Db.update("UPDATE user_role SET rid=? WHERE uid=?", rid, uid);
//        Db.update("DELETE FROM user_role WHERE uid=? AND rid=?", uid);
//        Db.update("INSERT INTO user_role (uid,rid) VALUES(?,?)", uid, rid);
    }

    public List<Record> getSubmittedProblems(Integer uid) {
        return Db.find(
            "SELECT p.title, p.pid, MIN(result) AS result FROM solution s LEFT JOIN problem p ON p.pid=s.pid WHERE s.uid=? AND s.status=1 GROUP BY s.pid",
            uid);
    }

    public List<Record> getSolvedProblems(Integer uid) {
        return Db.find("SELECT * FROM solution WHERE uid=? AND result=? AND status=1 GROUP BY pid", uid, ResultType.AC);
    }

    public boolean checkPassword(Integer uid, String password) {
        String storedHash = dao.findById(uid).getPassword();
        return BCrypt.checkpw(password, storedHash);
    }

    public boolean containsEmail(String email) {
        return dao.getUserByEmail(email) != null;
    }

    public boolean containsUsername(String username) {
        return dao.getUserByName(username) != null;
    }

    public boolean isAdmin() {
        try {
            return ShiroKit.hasPermission("admin");
        } catch (Exception e) // occur in new thread when judge
        {
            if (OjConfig.isDevMode()) {
                log.debug("check admin permission", e);
            } else {
                log.warn(e.getLocalizedMessage());
            }
        }
        return false;
    }

    public boolean containsEmailExceptThis(Integer uid, String email) {
        return dao.containsEmailExceptThis(uid, email);
    }

    public Object getUserField(Integer uid, String name) {
        UserModel userModel = dao.getUserInfoByUid(uid);
        if (userModel == null) {
            return null;
        }

        return userModel.get(name);
    }

    private void updateCache(UserModel user) {
        CacheKit.put("user", user.getUid(), user);
    }

    private void evictCache(Integer uid) {
        CacheKit.remove("user", uid);
    }
}
