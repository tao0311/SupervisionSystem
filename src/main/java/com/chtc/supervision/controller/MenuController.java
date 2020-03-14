package com.chtc.supervision.controller;

import com.chtc.supervision.entity.Menu;
import com.chtc.supervision.service.impl.MenuServiceImpl;
import com.chtc.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 菜单管理
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuServiceImpl menuService;

    @RequestMapping(value = "/getMenus",method= RequestMethod.GET)
    @ResponseBody
    public Json showMenus(HttpServletRequest request){
        Set<Menu> menus = menuService.showMenus(request);
        Json json = new Json();
        json.setObj(menus);
        return json;
    }
}
