package com.chtc.supervision.controller;

import com.chtc.supervision.entity.User;
import com.chtc.supervision.service.IUserService;
import com.chtc.util.CaptchaUtil;
import com.chtc.util.Json;
import com.chtc.util.ValidateCode;
import javafx.scene.image.Image;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录管理
 */
@Controller
public class LoginController {
    @Autowired
    private IUserService userService;

//    /**
//     * 简单的登录验证
//     * @param username
//     * @param password
//     * @return
//     */
//    @RequestMapping(value="/ldap",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//    @ResponseBody
//    public String ldap(String username, String password, HttpServletRequest request){
//        String code = "0";
//        User user = userService.findByUserName(username);
//        if (user!=null  ){
//            code = "1";
////            request.getSession().setAttribute("user",user);
//        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("code",code);
//        return jsonObject.toString();
//    }

    @RequestMapping("/index")
    public String index(){
        return "default.main";
    }

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "error",required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              @RequestParam(value = "invalid", required = false) String invalid,
                              @RequestParam(value = "invalid", required = false) String notLogin
    ){
        ModelAndView model = new ModelAndView();
        if (error != null){
            model.addObject("msg","用户名或密码不正确");
        }
        if (logout != null){
            model.addObject("msg","您已经成功登出");
        }
        if (invalid != null){
            model.addObject("msg","当前账号已被他人登陆或者登陆超时！！");
        }
        if (notLogin != null){
            model.addObject("msg","当前系统未登录！！");
        }
        model.setViewName("login/login");
        return model;
    }

    /**
     * 生成验证码
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping("/createCode")
    public void createCode(HttpServletResponse response,HttpServletRequest request) throws IOException {
        // 通知浏览器不要缓存
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "-1");
        CaptchaUtil captchaUtil = CaptchaUtil.Instance();
        request.getSession().setAttribute("code",captchaUtil.getString());
        ImageIO.write(captchaUtil.getImage(),"jpg",response.getOutputStream());
        response.getOutputStream().close();
    }

    /**
     * 比对验证码
     * @param session
     * @param code
     * @return
     */
    @RequestMapping(value="/checkCode")
    @ResponseBody
    public Json checkCode(HttpSession session,String code){
        Json json = new Json();
        String codeSession = (String) session.getAttribute("code");
        if (StringUtils.isEmpty(code)){
            json.setMsg("验证码为空");
            json.setSuccess(false);
        }else if (codeSession.equalsIgnoreCase(code)){
            json.setSuccess(true);
        }else {
            json.setMsg("验证码错误");
            json.setSuccess(false);
        }
        return json;
    }
}
