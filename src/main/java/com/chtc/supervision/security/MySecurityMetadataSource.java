package com.chtc.supervision.security;

import com.chtc.supervision.entity.Menu;
import com.chtc.supervision.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 查询资源url以及所对应的权限
 */
@Service
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private MenuRepository menuRepository;

    //resourceMap就是保存的所有资源和权限的集合，URL为Key，权限作为Value
    private static HashMap<String, Collection<ConfigAttribute>> resourceMap = null;

    @PostConstruct //此注解表示会在项目启动的时候执行此方法
    public void init(){
        loadResourceDefine();
    }

    /**
     * 加载所有的资源以及对应的权限
     */
    public void loadResourceDefine() {
        List<Menu> menus = menuRepository.findAll();
        resourceMap = new HashMap<>();
        if ( menus!=null &&  menus.size()>0){
                for (Menu menu:menus){
                    //获取权限
                    String menuKey = menu.getMenuKey();
                    //封装成spring的权限属性
                    ConfigAttribute configAttribute = new SecurityConfig(menuKey);
                    //获取url
                    String url = menu.getMenuUrl();
                    //判断url是否存在，若存在则取出，将新的权限增加到原来的权限中
                    if (resourceMap.containsKey(url)){
                        Collection<ConfigAttribute> configAttributes = resourceMap.get(url);
                        configAttributes.add(configAttribute);
                        resourceMap.put(url,configAttributes);
                    }else {
                        //若不存在，则直接添加
                        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
                        configAttributes.add(configAttribute);
                        resourceMap.put(url,configAttributes);
                    }
                }
        }
    }

    /**
     * 根据url加载对应的权限
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取要访问的url
        String requestUrl = ((FilterInvocation)object).getRequestUrl();
        //如果resourceMap为空，则执行loadResourceDefine()去加载资源url与权限的对应关系
        if (resourceMap == null){
            loadResourceDefine();
        }
        //删除url中的参数
        if (requestUrl.contains("?")){
             requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        Collection<ConfigAttribute> configAttributes = resourceMap.get(requestUrl);
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
