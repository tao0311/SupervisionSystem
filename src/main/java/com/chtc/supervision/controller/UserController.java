package com.chtc.supervision.controller;

import com.chtc.supervision.entity.Department;
import com.chtc.supervision.entity.Role;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.service.IDepartmentService;
import com.chtc.supervision.service.IRoleService;
import com.chtc.supervision.service.IUserService;
import com.chtc.util.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/user")
public class UserController {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd";

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IDepartmentService departmentService;

    @RequestMapping("/userManage")
    public String adminPage(){
        return "user.manager";
    }

    /**
     * 用户分页查询
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/userManage/paging",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String userManagePaging(HttpServletRequest request, HttpServletResponse response){
        //获取sEcho，sEcho表示请求的次数 需要原封不动的传回页面
        String sEcho = request.getParameter("sEcho");
        //将页面传来的参数进行处理，其中主要是分页所需的信息
        DataRequest dr = DataTableUtil.trans(request);
        //获取查询条件的值
        String searchUserInfo = request.getParameter("searchUserInfo");
        String searchRoleName = request.getParameter("searchRoleName");
        //进行查询
        DataTableReturnObject dro = userService.getUserPageMode(dr,searchUserInfo,searchRoleName);
        //转为json返回
        return DataTableUtil.transToJsonStr(sEcho,dro);
    }

    /**
     * 添加用户页面
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/addUserView")
    public String userAdd(@ModelAttribute("user") User user,HttpServletRequest request){
        //获取全部角色
        List<Role> roles = roleService.getRoleNames();
        request.setAttribute("roles",roles);
        //获取全部院系
        List<Department> departments = departmentService.findAllDepartment();
        request.setAttribute("departments",departments);
        return "user.add";
    }

    /**
     * 用户更新页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateUserView",method = RequestMethod.GET)
    public String updateUserView(@RequestParam(value = "id") String id, HttpServletRequest request){

        User user = userService.getUser(id);
        for (Role role:user.getRoles()){
            user.setRoleIDs(role.getId());
        }
        if (!StringUtils.isEmpty(user.getBirthday())){
            user.setBirthdayTime(String.valueOf(user.getBirthday()));
        }else {
            user.setBirthdayTime("");
        }
        //获取全部角色
        request.setAttribute("roles",roleService.getRoleNames());
        //获取全部院系
        request.setAttribute("departments",departmentService.findAllDepartment());
        request.setAttribute("user",user);
        return "user.update";
    }

    @RequestMapping(value = "/saveOrUpdateUser",method = RequestMethod.POST)
    public String saveOrUpdateUser(@ModelAttribute("user") User user,@RequestParam("type") String type){
        if (!StringUtils.isEmpty(user.getBirthdayTime())){
            try {
                user.setBirthday(DateUtil.formatString2Date(user.getBirthdayTime(),DATE_TIME_FORMAT, Locale.US));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            user.setBirthday(null);
        }
            userService.saveOrUpdateUser(user);
        if (type.equalsIgnoreCase("add")){
            return "redirect:/user/addUserView";
        }else {
            return "redirect:/user/updateUserView?id="+user.getId();
        }
    }

    /**
     * 用户详情页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/userDetailView",method = RequestMethod.GET)
    public String viewUserDetail(@RequestParam(value = "id") String id,HttpServletRequest request){
        User user = userService.getUser(id);
        if (!StringUtils.isEmpty(user.getBirthday())){
            user.setBirthdayTime(String.valueOf(user.getBirthday()));
        }else {
            user.setBirthdayTime("");
        }
        request.setAttribute("user",user);
        return "user.detail";
    }

    /**
     * 逻辑删除用户
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET)
    @ResponseBody
    public Json deleteUser(@RequestParam("ids") String [] ids){
        userService.deleteUser(ids);
        Json json = new Json();
        json.setMsg("删除成功");
        return json;
    }

    /**
     * 管理员重置密码
     * @param id
     * @return
     */
    @RequestMapping(value = "/resetUserPassword",method = RequestMethod.GET)
    @ResponseBody
    public Json resetUserPassword(@RequestParam("id") String id){
        userService.resetUserPassword(id);
        Json json = new Json();
        json.setMsg("重置成功");
        return json;
    }

    /**
     * 通过院系查找用户
     * @param id
     * @return
     */
    @RequestMapping(value = "findUserByDepartmentId", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String findUserByDepartmentId(@RequestParam("id") String id){
        List<JSONObject> list =  userService.findUserByDepartmentId(id);
        return JSONObject.valueToString(list);
    }

    /**
     * 检查手机号码是否存在
     * @param tel
     * @param id
     * @return
     */
    @RequestMapping(value = "/checkMobilePhoneExist",method = RequestMethod.GET)
    @ResponseBody
    public Json checkMobilePhoneExist(@ModelAttribute("tel") String tel, @ModelAttribute("id") String id){
        Json json = new Json();
        User user = userService.findUserByMobilePhone(tel,id);
        if (user == null){
            json.setSuccess(true);
        }else {
            json.setSuccess(false);
            json.setMsg("该手机号码已经存在");
        }
        return json;
    }

    /**
     * 检查用户编号是否存在
     * @param userCode
     * @param id
     * @return
     */
    @RequestMapping(value = "/checkUserCodeExist",method = RequestMethod.GET)
    @ResponseBody
    public Json checkUserCodeExist(@ModelAttribute("userCode") String userCode, @ModelAttribute("id") String id){
        Json json = new Json();
        User user = userService.findUserByUserCode(userCode,id);
        if (user == null){
            json.setSuccess(true);
        }else {
            json.setSuccess(false);
            json.setMsg("该编号已经存在");
        }
        return json;
    }

    /**
     * 返回修改密码页面
     * @return
     */
    @RequestMapping("/userPasswordUpdateView")
    public String userPasswordEditView( ){
        return "user/userPasswordUpdate";
    }

    /**
     * 用户修改密码
     * @param userName
     * @param newPassWord
     * @param oldPassWord
     * @return
     */
    @RequestMapping(value = "/userPasswordUpdate",method = RequestMethod.POST)
    @ResponseBody
    public Json userPasswordUpdate(String userName, String newPassWord, String oldPassWord){
       return userService.updatePassword(userName,newPassWord,oldPassWord);
    }

    /**
     * 通过Excel表添加用户
     * @param excel_file
     * @return
     */
    @RequestMapping(value = "/addUserByFile",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String addUserByFile(MultipartFile excel_file){
        return userService.addUserByFile(excel_file);
    }
}
