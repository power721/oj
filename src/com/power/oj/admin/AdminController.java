package com.power.oj.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.power.oj.core.OjController;

@RequiresPermissions("admin")
public class AdminController extends OjController
{

  public void index()
  {
    render("index.html");
  }
  
}
