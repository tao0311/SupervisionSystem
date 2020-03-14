package com.chtc.supervision.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

/**
 * 访问决策器，将用户拥有的权限与要访问的url所需要的权限作比较
 */
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {
    /**
     * 该方法：需要比较权限和权限配置
     * configAttributes是url所需要的权限
     * object参数是一个 URL
     * authentication是登录用户所有的权限
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //如果访问的url不需要权限，则直接返回
        if (configAttributes == null){
            return;
        }
        //用迭代器遍历权限
        Iterator<ConfigAttribute> configAttributeIterator = configAttributes.iterator();
        while (configAttributeIterator.hasNext()){
            //获取url所需要的权限
            ConfigAttribute ca = configAttributeIterator.next();
            String needRole = ((SecurityConfig)ca).getAttribute();
            for (GrantedAuthority authority:authentication.getAuthorities()){
                if (needRole.trim().equals(authority.getAuthority().trim())){
                    return;
                }
            }
        }
        throw new  AccessDeniedException("Access Denied");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
