package com.power.oj.admin;

import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjConstants;
import com.power.oj.core.OjController;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresPermissions("admin")
@RequiresAuthentication
public class UserAdminController extends OjController {

    private static final AdminService adminService = AdminService.me();

    public void index() {

    }

    public void role() {
        List<Record> roleList = ojService.getRoleList();

        setAttr("roleList", roleList);
    }

    public void members() {
        List<Record> membersList = adminService.getMembers();

        setAttr("members", membersList);
    }

    public void create() {
        setAttr(OjConstants.PROGRAM_LANGUAGES, OjConfig.languageName);
    }

    public void permission() {
        List<Record> roleList = ojService.getRoleList();
        List<Record> permissionList = ojService.getPermissionList();
        Map<Integer, Integer> deepTree = new HashMap<Integer, Integer>();

        deepTree.put(1, 0);
        for (Record permission : permissionList) {
            Integer id = permission.getInt("id");
            Integer deep = deepTree.get(permission.getInt("parentID"));
            if (deep == null) {
                log.info(id + " " + permission.getInt("parentID"));
                deep = 0;
            }
            deepTree.put(id, deep + 1);
        }

        setAttr("roleList", roleList);
        setAttr("deepTree", deepTree);
        setAttr("permissionList", permissionList);
    }

    @RequiresUser
    public void loginlog() {
        int pageNumber = getParaToInt(0, 1);
        int pageSize = getParaToInt(1, OjConfig.userPageSize);

        setAttr("pageSize", OjConfig.userPageSize);
        setAttr("logs", adminService.getLoginlog(pageNumber, pageSize));
        setTitle(getText("user.loginlog.title"));
    }

    @RequiresPermissions("user:online")
    public void online() {
        setAttr("loginUserNum", sessionService.getUserNumber());
        setAttr(OjConstants.USER_LIST, sessionService.getAccessLog());

        setTitle(getText("user.online.title"));
    }

}
