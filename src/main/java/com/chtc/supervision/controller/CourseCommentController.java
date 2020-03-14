package com.chtc.supervision.controller;

import com.chtc.supervision.entity.CourseComment;
import com.chtc.supervision.service.ICourseCommentService;
import com.chtc.supervision.service.ITaskService;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import com.chtc.util.DataTableUtil;
import com.chtc.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 课程记录评价管理
 */
@Controller
@RequestMapping("/courseComment")
public class CourseCommentController {

    @Autowired
    private ICourseCommentService courseCommentService;

    @Autowired
    private ITaskService taskService;

    @RequestMapping(value = "/selectCourse",method = RequestMethod.GET)
    public String selectCourseView(){
        return "courseComment.select";
    }

    @RequestMapping(value = "/createCourseComment",method = RequestMethod.GET)
    @ResponseBody
    public Json createCourseComment(@RequestParam("id") String id,HttpServletRequest request){
        Json json = courseCommentService.createCourseComment(id,request);
        //修改任务量
        if(json.isSuccess()){
            taskService.updateTaskFinishTimes(json.getCode());
        }
        return json;
    }

    @RequestMapping(value = "/writeCourseComment",method = RequestMethod.GET)
    public String writeCourseCommentView(){
        return "courseComment.write";
    }

    @RequestMapping(value = "/courseCommentManage/paging",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String courseCommentManage(HttpServletRequest request){
        String sEcho = request.getParameter("sEcho");
        DataRequest dr = DataTableUtil.trans(request);
        String searchCourseInfo = request.getParameter("searchCourseInfo");
        int state = Integer.parseInt(request.getParameter("state"));
        DataTableReturnObject dro = courseCommentService.getCourseCommentPage(dr,searchCourseInfo,state);
        return DataTableUtil.transToJsonStr(sEcho,dro);
    }

    @RequestMapping(value = "/commentDetailView",method = RequestMethod.GET)
    public String commentDetailView(@RequestParam("id") String id,
                                    @RequestParam(value = "type",required = false) String type, Model model){
        CourseComment courseComment = courseCommentService.getCourseComment(id);
        model.addAttribute("courseComment",courseComment);
        model.addAttribute("type",type);
        return "courseComment.detail";
    }

    @RequestMapping(value = "/courseCommentEdit",method = RequestMethod.GET)
    public String courseCommentEdit(@RequestParam("id") String id,HttpServletRequest request){
        CourseComment courseComment = courseCommentService.getCourseComment(id);
        request.setAttribute("courseComment",courseComment);
        return "courseComment.edit";
    }

    @RequestMapping(value = "/saveCourseComment",method = RequestMethod.POST)
    public String saveCourseComment(@ModelAttribute("courseComment") CourseComment courseComment){
        courseCommentService.saveCourseComment(courseComment);
        return "courseComment.edit";
    }
    @RequestMapping(value = "/submitComment",method = RequestMethod.GET)
    @ResponseBody
    public Json submitComment(@RequestParam("id") String id){
        courseCommentService.submitComment(id);
        Json json = new Json();
        json.setSuccess(true);
        return json;
    }

    @RequestMapping("/commentChecked")
    public String commentCheckedView(){
        return "courseComment.checked";
    }
    @RequestMapping("/commentChecking")
    public String commentCheckingView(){
        return "courseComment.checking";
    }
    @RequestMapping("/commentUnThrough")
    public String commentUnThroughView(){
        return "courseComment.unThrough";
    }

    @RequestMapping(value = "/commentList",method = RequestMethod.GET)
    public String courseCommentList(@RequestParam("id") String id, Model model) {
        model.addAttribute("id",id);
        return "courseComment.list";
    }

    @RequestMapping(value = "/fetchComments",produces = "text/html; charset=utf-8")
    @ResponseBody
    public String fetchComments(HttpServletRequest request){
        String sEcho = request.getParameter("sEcho");
        String id = request.getParameter("id");
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro =  courseCommentService.findAllCourseCommentsById(dr,id);
        return DataTableUtil.transToJsonStr(sEcho,dro);
    }
}
