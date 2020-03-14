package com.chtc.supervision.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;
@Service
public class MySecurityFilter extends AbstractSecurityInterceptor implements Filter {
    //注入资源管理器
    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;

    //注入访问决策器
    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    //注入认证管理器
    @Autowired
    private AuthenticationManager myAuthenticationManager;

    @PostConstruct
    public void init() {
        //  MySecurityFilter init
        super.setAuthenticationManager(myAuthenticationManager);
        super.setAccessDecisionManager(myAccessDecisionManager);
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request,response,chain);
        invoke(fi);
    }
    /**
     * 登陆后，每次访问资源都会被这个拦截器拦截，会执行doFilter这个方法，
     * 这个方法调用了invoke方法，其中fi断点显示是一个url（可能重写了toString方法吧，但是里面还有一些方法的），
     * 最重要的是beforeInvocation这个方法，它首先会调用MyInvocationSecurityMetadataSource类的getAttributes方法获取被拦截url所需的权限，
     * 在调用MyAccessDecisionManager类decide方法判断用户是否够权限。弄完这一切就会执行下一个拦截器。
     */
    public void invoke(FilterInvocation fi) throws IOException, ServletException{
        InterceptorStatusToken token = null;
        try{
            token = super.beforeInvocation(fi);
        }catch (Exception e){
            if (e instanceof AccessDeniedException){
                throw new AccessDeniedException("无权限访问该页面");
            }
            return;
        }
        //执行下一个浏览器
        try {
            fi.getChain().doFilter(fi.getRequest(),fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.mySecurityMetadataSource;
    }
}
