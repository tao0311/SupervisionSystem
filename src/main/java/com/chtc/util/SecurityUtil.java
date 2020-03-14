package com.chtc.util;

import com.chtc.supervision.entity.Role;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.entity.UserDetail;
import com.chtc.supervision.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by maofn on 2017/3/16.
 *
 * 有关springsecurity 的工具类
 */
public class SecurityUtil {

    /**
     * 到SecurityContextHolder中获取用户信息
     * @param userRepository
     * @return
     */
    public static User getUser(UserRepository userRepository) {
        if (SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            return null;
        }
        UserDetail userDetail = (UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUserName(userDetail.getUsername());
        return user;
    }

    public static String getCode(User user){
        for (Role role:user.getRoles()){
            return role.getCode();
        }
        return null;
    }
}
