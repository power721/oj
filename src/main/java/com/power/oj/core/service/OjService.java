package com.power.oj.core.service;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.model.ResourceModel;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.RoleModel;
import com.power.oj.user.UserModel;
import com.power.oj.user.UserService;
import com.power.oj.util.Tool;
import com.power.oj.util.freemarker.FreemarkerKit;
import jodd.mail.EmailMessage;
import jodd.util.MimeTypes;
import jodd.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OjService {
    private static final Logger log = Logger.getLogger(OjService.class);
    private static final OjService me = new OjService();

    private OjService() {
    }

    public static OjService me() {
        return me;
    }

    public void loadVariable() {
        OjConfig.loadVariable();
    }

    public void loadLanguage() {
        OjConfig.loadLanguage();
    }

    public void initJudgeResult() {
        OjConfig.initJudgeResult();
    }

    public boolean checkEmailConf() {
        if (OjConfig.getString("adminEmail") == null) {
            return false;
        }

        String emailServer = OjConfig.getString("emailServer");
        String emailUser = OjConfig.getString("emailUser");
        String emailPass = OjConfig.getString("emailPass");
        if (emailServer == null || emailUser == null || emailPass == null) {
            return false;
        }
        return true;
    }

    public boolean sendVerifyEmail(String name, String email, String token) {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put(OjConstants.BASE_URL, OjConfig.getBaseURL());
        paras.put(OjConstants.SITE_TITLE, OjConfig.siteTitle);
        paras.put("name", name);
        paras.put("token", token);
        paras.put("ctime", OjConfig.timeStamp);
        paras.put("expires", OjConstants.VERIFY_EMAIL_EXPIRES_TIME / OjConstants.MINUTE_IN_MILLISECONDS);

        sendVerifyEmail(name, email, paras);

        log.info("Account recovery email send to " + email + " for user " + name);
        return true;
    }

    public boolean sendVerifyEmail(String name, final String email, Map<String, Object> paras) {
        final String adminEmail = OjConfig.getString("adminEmail");
        if (adminEmail == null) {
            return false;
        }

        String html = FreemarkerKit.processString("tpl/verifyEmail.html", paras);
        final EmailMessage htmlMessage = new EmailMessage(html, MimeTypes.MIME_TEXT_HTML);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Tool.sendEmail(adminEmail, email, "Confirm PowerOJ account!", htmlMessage);
            }
        }).start();

        log.info("Account recovery email send to user " + name);
        return true;
    }

    public boolean sendResetPasswordEmail(String name, final String email, String token) {
        final String adminEmail = OjConfig.getString("adminEmail");

        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put(OjConstants.BASE_URL, OjConfig.getBaseURL());
        paras.put(OjConstants.SITE_TITLE, OjConfig.siteTitle);
        paras.put("name", name);
        paras.put("token", token);
        paras.put("ctime", OjConfig.timeStamp);
        paras.put("expires", OjConstants.RESET_PASSWORD_EXPIRES_TIME / OjConstants.MINUTE_IN_MILLISECONDS);

        String html = FreemarkerKit.processString("/tpl/resetEmail.html", paras);
        final EmailMessage htmlMessage = new EmailMessage(html, MimeTypes.MIME_TEXT_HTML);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Tool.sendEmail(adminEmail, email, "Reset PowerOJ account!", htmlMessage);
            }
        }).start();

        log.info("Account recovery email send to user " + name);
        return true;
    }

    public List<Record> getUserRoles(int uid) {
        String sql =
            "SELECT r.name AS role, r.id AS rid FROM role r LEFT JOIN user_role ur ON ur.rid = r.id WHERE ur.uid = ?";
        List<Record> roleList = Db.find(sql, uid);

        return roleList;
    }

    public List<Record> getRoleList() {
        return Db.find("SELECT id,name FROM role ORDER BY id");
    }

    public Page<RoleModel> getRoleList(int pageNumber, int pageSize, String sSortName, String sSortDir,
        String sSearch) {
        List<Object> param = new ArrayList<Object>();
        String sql = "SELECT *";
        StringBuilder sb = new StringBuilder().append("FROM role WHERE 1=1");

        if (StringUtil.isNotEmpty(sSearch)) {
            sb.append(" AND name LIKE ? ");
            param.add(new StringBuilder(3).append("%").append(sSearch).append("%").toString());
        }
        sb.append(" ORDER BY ").append(sSortName).append(" ").append(sSortDir).append(", id");

        log.info(sb.toString());
        return RoleModel.dao.paginate(pageNumber, pageSize, sql, sb.toString(), param.toArray());
    }

    public List<Record> getRolePermission(int rid) {
        String sql =
            "SELECT p.name AS permission FROM permission p INNER JOIN role_permission rp ON rp.pid = p.id WHERE rp.rid = ?";
        List<Record> permissionList = Db.find(sql, rid);

        return permissionList;
    }

    public List<Record> getPermissionList() {
        return Db.find("SELECT * FROM permission WHERE id>1 ORDER BY id");
    }

    public List<Record> tagList() {
        return Db.find("SELECT tag FROM tag WHERE status=1 GROUP by tag ORDER BY COUNT(tag) DESC");
    }

    public List<ResourceModel> resourceList() {
        String sql;
        if (UserService.me().isAdmin()) {
            sql = "SELECT * FROM resource";
        } else if (ShiroKit.hasRole(UserService.MEMBER_ROLE_NAME)) {
            sql = "SELECT * FROM resource WHERE access='public' OR access='private'";
        } else {
            sql = "SELECT * FROM resource WHERE access='public'";
        }

        return ResourceModel.dao.find(sql);
    }

    public ResourceModel getResource(Integer id) {
        return ResourceModel.dao.findFirst("SELECT * FROM resource WHERE id=?", id);
    }

    public boolean addResource(ResourceModel resourceModel) {
        resourceModel.setCtime(OjConfig.timeStamp).setUid(UserService.me().getCurrentUid());
        return resourceModel.save();
    }

    public boolean updateResource(ResourceModel resourceModel) {
        ResourceModel newResource = ResourceModel.dao.findById(resourceModel.getId());
        newResource.setAccess(resourceModel.getAccess());
        newResource.setDescription(resourceModel.getDescription());
        newResource.setName(resourceModel.getName());
        newResource.setPath(resourceModel.getPath());
        return newResource.update();
    }

    public File getResourceFile(String name, Integer id) {
        String sql;
        if (UserService.me().isAdmin()) {
            sql = "SELECT * FROM resource WHERE name=? AND id=?";
        } else if (ShiroKit.hasRole(UserService.MEMBER_ROLE_NAME)) {
            sql = "SELECT * FROM resource WHERE (access='public' OR access='private') AND name=? AND id=?";
        } else {
            sql = "SELECT * FROM resource WHERE access='public' AND name=? AND id=?";
        }

        ResourceModel resourceModel = ResourceModel.dao.findFirst(sql, name, id);
        if (resourceModel == null) {
            return null;
        }

        String path = resourceModel.getPath();
        File file;
        if (path.charAt(0) == '/') {
            file = new File(path);
        } else {
            file = new File(OjConfig.downloadPath, path);
        }

        if (file.exists()) {
            resourceModel.setDownload(resourceModel.getDownload() + 1);
            resourceModel.update();
            return file;
        } else {
            return null;
        }
    }

}
