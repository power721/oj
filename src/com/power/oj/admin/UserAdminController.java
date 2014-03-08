package com.power.oj.admin;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.plugin.activerecord.Record;
import com.power.oj.core.OjController;
import com.power.oj.core.service.OjService;

@RequiresPermissions("admin")
public class UserAdminController extends OjController
{
  public void index()
  {
    
  }
  
  public void role()
  {
    List<Record> roleList = OjService.me().getRoleList();
    
    setAttr("roleList", roleList);
  }
  
  public void permission()
  {
    List<Record> roleList = OjService.me().getRoleList();
    List<Record> permissionList = OjService.me().getPermissionList();
    
    setAttr("roleList", roleList);
    setAttr("permissionList", permissionList);
  }
  
}
