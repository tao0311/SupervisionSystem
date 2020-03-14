package com.chtc.supervision.service;

import com.chtc.supervision.entity.Menu;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface IMenuService {
    public Set<Menu> showMenus(HttpServletRequest request);
}
