package com.chtc.mobile.controller;

import com.chtc.supervision.service.ICourseService;
import com.chtc.util.CourseQueryUtil;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import com.chtc.util.DataTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mobile/course")
public class MobileCourseController {

    @Autowired
    private ICourseService courseService;

    @RequestMapping(value = "/coursePage/paging",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String getCoursePage(HttpServletRequest request){
        String sEcho = request.getParameter("sEcho");
        DataRequest dr = DataTableUtil.trans(request);
        String searchCourseInfo = request.getParameter("searchCourseInfo");
        DataTableReturnObject dro = courseService.getCourseToMobile(dr,searchCourseInfo);
        return DataTableUtil.transToJsonStr(sEcho,dro);
    }

    @RequestMapping("/courseSelect")
    public String addComment(){
        return "mobile/courseSelect";
    }
}
