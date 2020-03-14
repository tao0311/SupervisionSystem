package com.chtc.supervision.controller;

import com.chtc.supervision.entity.Department;
import com.chtc.supervision.entity.Semester;
import com.chtc.supervision.entity.Task;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.service.ISemesterService;
import com.chtc.supervision.service.ITaskService;
import com.chtc.supervision.service.IUserService;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import com.chtc.util.DataTableUtil;
import com.chtc.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 任务管理
 */
@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    ITaskService taskService;
    @Autowired
    IUserService userService;
    @Autowired
    ISemesterService semesterService;

    @RequestMapping("/taskManager")
    public String taskManager() {
        return "task.manger";
    }

    /*
    * @param 用来接收设置任务的信息
    * 因为是异步提交的所以不需要刷新页面
    *supervisionCode:
    *courseTimes:任务量
    *startYear:
    *endYear:
    *semesterNum:
    *
    *
    *
    * */
    @RequestMapping(value = "/saveTask",method = RequestMethod.POST)
    public String saveTask(String supervisionCode, String courseTimes, String startYear, String endYear, String
            semesterNum,HttpServletRequest request) {
        Task task = new Task();
        task.setCourseTimes(Integer.parseInt(courseTimes));
        User byUserCode = userService.findByUserCode(supervisionCode);
        task.setUser(byUserCode);
        Semester semster = semesterService.findSemster(startYear, endYear, Integer.parseInt(semesterNum));
        if (semster != null) {
            //查询数据库中当前学期中所有任务查询当前用户的任务
            List<Task> bySemesterId = taskService.findBySemesterId(semster.getId());
            //遍历list 获取当前用户对应的任务
            if (bySemesterId != null) {
                for (Task task1 : bySemesterId) {
                    String code = task1.getUser().getUserCode();
                    if (code.equals(supervisionCode)) {
                        task1.setCourseTimes(Integer.parseInt(courseTimes));
                        task1.setUpdateBy(byUserCode.getNickName());
                        task1.setUpdateDate(new Date());
                        taskService.save(task1);
                        return null;
                    }
                }
            }
            task.setSemester(semster);

        }else {
            semster = new Semester();
            semster.setEndYear(endYear);
            semster.setStartYear(startYear);
            semster.setSemesterNum(Integer.parseInt(semesterNum));
            semster.setVersion(1);
            //获取当前登录用户的姓名
            //TODO 改
            SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                    .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
            String name = securityContextImpl.getAuthentication().getName();
            semster.setCreateBy(name);
            semster.setCreateDate(new Date());
            semster.setId(UUID.randomUUID().toString());
            Semester semester = semesterService.save(semster);
            task.setSemester(semester);
        }
        task.setId(UUID.randomUUID().toString());
        task.setVersion(1);
        task.setCreateBy(byUserCode.getNickName());
        task.setCreateDate(new Date());
        taskService.save(task);
        return null;
    }

    /*
    * @param code 使用户编号通过用户编号查询任务
    * @return 返回task的json数据
    * 查询的是某一个用的所有任务
    *
    * */
    @RequestMapping(value = "/showTask", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String showTask(String code, HttpServletRequest httpServletRequest) {
        //获取sEcho，sEcho表示请求的次数 需要原封不动的传回页面
        String sEcho = httpServletRequest.getParameter("sEcho");
        //将页面传来的参数进行处理，其中主要是分页所需的信息
        DataRequest dr = DataTableUtil.trans(httpServletRequest);
        httpServletRequest.getParameter("iDisplayLength");
        User byUserCode = userService.findByUserCode(code);
        DataTableReturnObject taskByUserId = taskService.getTaskByUserId(byUserCode.getId(), dr);
        return DataTableUtil.transToJsonStr(sEcho, taskByUserId);
    }

    /*
    * @param 通接收前台传来的参数来获取数据
    * @return 返回符合要求的督导员
    *
    *
    * */
    @RequestMapping(value = "/showSupervision", produces = "text/html;charset=utf-8",
            method = RequestMethod.GET)
    @ResponseBody
    public String showSupervision(HttpServletRequest request) throws Exception {

        DataRequest dr = DataTableUtil.trans(request);
        //获取sEcho，sEcho表示请求的次数 需要原封不动的传回页面
        String sEcho = request.getParameter("sEcho");
        //获得院系信息
        String departmentName = request.getParameter("departmentName");
        //获取用户编号
        String userCode = request.getParameter("userCode");
        //获取用户名称
        String keyName = request.getParameter("name");
//        if(!StringUtils.isEmpty(keyName)){
//            keyName= new String(keyName.getBytes("iso8859-1"),"utf-8");
//        }
        //进行查询
        DataTableReturnObject dro = userService.getSupervisionPageMode(dr, keyName, userCode, departmentName);
        //转为json返回0
        return DataTableUtil.transToJsonStr(sEcho, dro);
    }

    /*
    *从前台接收id 删除任务
    *
    * */
    @RequestMapping(value = "/deletTask",method = RequestMethod.GET)
    @ResponseBody
    public String deletTask(String id) {
        taskService.delete(id);
        return null;
    }

    @RequestMapping(value = "/findTaskByUserId",method = RequestMethod.GET)
    @ResponseBody
    public Json findTaskByUser(){
        User user = userService.getUser();
        String id = user.getId();
        Task task = taskService.findTaskByUserId(id);
        Json json = new Json();
        json.setObj(task);
        return json;
    }

}
