package com.chtc.supervision.controller;

import com.chtc.supervision.entity.Role;
import com.chtc.supervision.service.IRoleService;
import com.chtc.supervision.service.impl.RoleServiceImpl;
import com.chtc.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 角色管理
 */
@Controller
@RequestMapping("/role")
public class RoleController{
    @Autowired
    private IRoleService roleService;

    @RequestMapping(value = "/selectRoleName", method= RequestMethod.GET)
    @ResponseBody
    public Json selectRoleName() {
        List<Role> roleNames =roleService.getRoleNames();
        Json json = new Json();
        json.setObj(roleNames);
        return json;
    }
}
