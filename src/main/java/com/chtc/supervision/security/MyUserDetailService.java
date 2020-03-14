package com.chtc.supervision.security;

import com.chtc.supervision.entity.Menu;
import com.chtc.supervision.entity.Role;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.entity.UserDetail;
import com.chtc.supervision.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MyUserDetailService这个类负责的是只是获取登陆用户的详细信息（包括密码、角色等），
 * 不负责和前端传过来的密码对比，只需返回User对象，
 * 后会有其他类根据User对象对比密码的正确性（框架帮我们做）。
 */
@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException("用户"+username+"不存在");
        }
        Collection<GrantedAuthority> grantedAuthorities = obtionGrantedAuthorities(user);
        UserDetail userDetail = new UserDetail(user.getUserName(),user.getPassWord()==null?"":user.getPassWord(),true,true,true,true,
                grantedAuthorities);
        BeanUtils.copyProperties(user,userDetail);
        return userDetail;
    }

    /**
     * 获取当前登录用户所具有的权限
     * @param user
     * @return
     */
    public Collection<GrantedAuthority> obtionGrantedAuthorities(User user){
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<Role> roles = user.getRoles();
        if (roles !=null && roles.size() > 0){
            for (Role role:roles){
                for (Menu menu:role.getMenus()){
                    authorities.add(new SimpleGrantedAuthority(menu.getMenuKey()));
                }
            }
        }
        return authorities;
    }
}
