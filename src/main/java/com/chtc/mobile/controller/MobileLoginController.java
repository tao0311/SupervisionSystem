package com.chtc.mobile.controller;

import com.chtc.supervision.entity.User;
import com.chtc.supervision.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 手机端登录
 */
@Controller
@RequestMapping("/mobile")
public class MobileLoginController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "error",required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout
    ){
        ModelAndView model = new ModelAndView();
        if (error != null){
            model.addObject("msg","用户名或密码不正确");
        }
        if (logout != null){
            model.addObject("msg","您已经成功登出");
        }
        model.setViewName("mobile/login");
        return model;
    }
    @RequestMapping("/index")
    public String index(String username, String password, HttpServletRequest request){
        User user = userService.findByUserName(username);
        String passWord = new Md5PasswordEncoder().encodePassword(password,"");
        if (user != null && user.getPassWord().equals(passWord)){
            request.getSession().setAttribute("user",user);
            return "redirect:/mobile/course/courseSelect";
        }else {
            request.setAttribute("msg","用户名或密码不正确");
            return "mobile/login";
        }
    }

}
