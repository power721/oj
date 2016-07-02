package com.power.oj.api;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.power.oj.admin.AdminService;
import com.power.oj.core.OjConfig;
import com.power.oj.core.OjController;
import com.power.oj.shiro.ShiroKit;
import com.power.oj.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Before(AdminInterceptor.class)
public class AdminApiController extends OjController {

    public void problemList() {
        int iDisplayStart = getParaToInt("iDisplayStart", 0);
        int pageSize = getParaToInt("iDisplayLength", OjConfig.problemPageSize);
        int pageNumber = iDisplayStart / pageSize + 1;
        int iSortCol = getParaToInt("iSortCol_0", 0);
        String sSortDir = getPara("sSortDir_0");
        String sSortName = getPara("mDataProp_" + iSortCol);
        String sSearch = getPara("sSearch");

        renderJson(problemService.getProblemPageDataTables(pageNumber, pageSize, sSortName, sSortDir, sSearch.trim()));
    }

    public void userList() {
        int iDisplayStart = getParaToInt("iDisplayStart", 0);
        int pageSize = getParaToInt("iDisplayLength", OjConfig.userPageSize);
        int pageNumber = iDisplayStart / pageSize + 1;
        int iSortCol = getParaToInt("iSortCol_0", 0);
        String sSortDir = getPara("sSortDir_0");
        String sSortName = getPara("mDataProp_" + iSortCol);
        String sSearch = getPara("sSearch");

        renderJson(userService.getUserListDataTables(pageNumber, pageSize, sSortName, sSortDir, sSearch.trim()));
    }

    public void userRoleList() {
        int iDisplayStart = getParaToInt("iDisplayStart", 0);
        int pageSize = getParaToInt("iDisplayLength", OjConfig.userPageSize);
        int pageNumber = iDisplayStart / pageSize + 1;
        int iSortCol = getParaToInt("iSortCol_0", 0);
        String sSortDir = getPara("sSortDir_0");
        String sSortName = getPara("mDataProp_" + iSortCol);
        String sSearch = getPara("sSearch");

        renderJson(userService.getUserRoleListDataTables(pageNumber, pageSize, sSortName, sSortDir, sSearch.trim()));
    }

    public void roleList() {
        List<Record> records = ojService.getRoleList();
        Map<Integer, String> rolse = new HashMap<>();
        for (Record record : records) {
            Integer id = record.getInt("id");
            if (id == UserService.ROOT_ROLE_ID && !ShiroKit.hasRole(UserService.ROOT_ROLE_NAME)) {
                continue;
            }
            rolse.put(id, record.getStr("name"));
        }
        renderJson(rolse);
    }

    public void addMember() {
        Integer uid = getParaToInt("uid");
        int result = userService.addMember(uid);

        renderJson("result", result);
    }

    public void removeMember() {
        Integer uid = getParaToInt("uid");
        int result = userService.removeMember(uid);

        renderJson("result", result);
    }

    public void changeUserRole() {
        int uid = getParaToInt("pk");
        int rid = getParaToInt("value");
        userService.changeUserRole(uid, rid);
    }

    public void contestList() {
        int iDisplayStart = getParaToInt("iDisplayStart", 0);
        int pageSize = getParaToInt("iDisplayLength", OjConfig.contestPageSize);
        int pageNumber = iDisplayStart / pageSize + 1;
        int iSortCol = getParaToInt("iSortCol_0", 0);
        String sSortDir = getPara("sSortDir_0");
        String sSortName = getPara("mDataProp_" + iSortCol);
        String sSearch = getPara("sSearch");

        renderJson(contestService.getContestListDataTables(pageNumber, pageSize, sSortName, sSortDir, sSearch.trim()));
    }

    public void updateProblem() {
        Integer pid = getParaToInt("pk");
        String name = getPara("name");
        String value = getPara("value");

        if (problemService.updateProblemByField(pid, name, value)) {
            renderNull();
        } else {
            renderError(400);
        }
    }

    public void updateConfig() {
        String name = getPara("name");
        String value = getPara("value");
        String type = getPara("type", "string");
        int result = AdminService.me().updateConfig(name, value, type);

        if (result == 0) {
            renderNull();
        } else {
            renderJson("success:false, result:" + result);
        }
    }

    public void reload() {
        String type = getPara(0, "all");
        AdminService.me().reloadConfig(type);
        renderNull();
    }

}
