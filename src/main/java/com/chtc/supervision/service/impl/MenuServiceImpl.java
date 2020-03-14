package com.chtc.supervision.service.impl;

import com.chtc.supervision.entity.Menu;
import com.chtc.supervision.entity.Role;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.repository.UserRepository;
import com.chtc.supervision.service.IMenuService;
import com.chtc.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.TreeSet;
@Service
public class MenuServiceImpl implements IMenuService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public Set<Menu> showMenus(HttpServletRequest request) {
        Set<Menu> menus = new TreeSet<>();
        User user = SecurityUtil.getUser(userRepository);
        for (Role role:user.getRoles()){
            for (Menu menu:role.getMenus()){
                menus.add(menu);
            }
        }
        return menus;
    }
}
