package com.chtc.mobile.controller;

import com.chtc.supervision.entity.CourseComment;
import com.chtc.supervision.entity.User;
import com.chtc.supervision.service.ICourseCommentService;
import com.chtc.util.DateUtil;
import com.chtc.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/mobile/courseComment")
public class MobileCommentController {

    @Autowired
    private ICourseCommentService courseCommentService;

    @RequestMapping("/createCourseComment")
    @ResponseBody
    public Json createCourseComment(@RequestParam("id") String id,HttpServletRequest request){
        Json json = courseCommentService.createCourseComment(id,request);
        return json;
    }

    @RequestMapping("/addCommentView")
    public String addCommentView(@RequestParam("id") String id, HttpServletRequest request){
        CourseComment courseComment = courseCommentService.getCourseComment(id);
        request.setAttribute("courseComment",courseComment);
        request.setAttribute("id",courseComment.getId());
        return "mobile/comment";
    }

    @RequestMapping("/findByTime")
    @ResponseBody
    public Json findByTime(HttpServletRequest request){
        Date startTime = new Date();
        Date endTime = DateUtil.getDateNextMinute(2);
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getId();
        List<CourseComment> list = courseCommentService.findByTime(startTime, endTime,createBy);
        Json json = new Json();
        if (list.size()==1){
            json.setCode(list.get(0).getId());
            json.setSuccess(true);
        }else {
            json.setSuccess(false);
        }
        return json;
    }
    @RequestMapping("/saveCourseComment")
    public String saveCourseComment(@ModelAttribute("courseComment") CourseComment courseComment){
        courseCommentService.saveCourseComment(courseComment);
        return "mobile/comment";
    }

    @RequestMapping("/submitComment")
    @ResponseBody
    public Json submitComment(@RequestParam("id") String id){
        courseCommentService.submitComment(id);
        Json json = new Json();
        json.setSuccess(true);
        return json;
    }

    @RequestMapping("/promptView")
    public String promptView(@RequestParam("id") String id,HttpServletRequest request){
        request.setAttribute("id",id);
        return "mobile/prompt";
    }
}
