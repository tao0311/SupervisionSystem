package com.chtc.supervision.controller;

import com.chtc.supervision.dto.CourseDTO;
import com.chtc.supervision.service.ICourseService;
import com.chtc.util.CourseQueryUtil;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import com.chtc.util.DataTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private ICourseService courseService;

    @RequestMapping(value = "/coursePage/paging",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String getCoursePage(HttpServletRequest request){
        String sEcho = request.getParameter("sEcho");
        DataRequest dr = DataTableUtil.trans(request);
        CourseQueryUtil courseQuery = CourseQueryUtil.trans(request);
        DataTableReturnObject dro = courseService.getCoursePageMode(dr,courseQuery);
        return DataTableUtil.transToJsonStr(sEcho,dro);
    }

    /**
     * 跳转到添加课程的页面
     * @return
     */
    @RequestMapping(value = "/addCoursePage",method = RequestMethod.GET)
    public String addCoursePage() {
        return "course.addCourse";
    }

    /**
     * 跳转到课程列表的页面
     * @return
     */
    @RequestMapping(value = "/courseListPage",method = RequestMethod.GET)
    public String courseListPage(){
        return "course.courseListPage";
    }

    /**
     * 将需要的课程信息封装起来，放入request中，跳转到更新课程页面
     * @param id 课程id
     * @param request request请求
     * @return 更新课程页面
     */
    @RequestMapping("/updateCoursePage")
    public String updateCoursePage(String id, HttpServletRequest request){
        CourseDTO course = courseService.queryCourseById(id);
        request.setAttribute("course", course);
        return "course.updateCoursePage";
    }

    /**
     * 根据课程编号查询课程
     * @param code 课程编号
     * @return JSON格式的课程信息
     */
    @RequestMapping(value = "/queryCourseByCode", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String queryCourseById(String code) {
        return courseService.queryCourseByCode(code);
    }

    /**
     * 手动添加课表
     */
    @ResponseBody
    @PostMapping(value = "/addCourseManual", produces = "text/html;charset=utf-8")
    public String addCourseManual(CourseDTO course) {
        return courseService.addCourseManual(course);
    }

    /**
     * 通过文件导入课表
     * @param excel_file 传入的文件
     * @return 处理成功与否信息
     */
    @ResponseBody
    @RequestMapping(value = "/addCourseByFile", produces = "text/html;charset=utf-8")
    public String addCourseByFile(MultipartFile excel_file) throws IOException {
        return courseService.addCourseByFile(excel_file);
    }

    /**
     * 更新课程
     * @param course 更新后的课程信息封装类
     * @return 反馈信息
     */
    @ResponseBody
    @PostMapping(value = "/updateCourse", produces = "text/html;charset=utf-8")
    public String updateCourse(CourseDTO course) {
        return courseService.updateCourse(course);
    }

    /**
     * 删除课程
     * @param ids 要删除的课程数组
     * @return 反馈信息
     */
    @RequestMapping(value = "/deleteCourse", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String deleteCourse(String[] ids) {
        return courseService.deleteCourse(ids);
    }

}
