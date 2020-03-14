package com.chtc.supervision.controller;

import com.chtc.supervision.entity.CourseRecord;
import com.chtc.supervision.service.ICourseRecordService;
import com.chtc.util.*;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据分析管理
 */
@Controller
@RequestMapping("/analysis")
public class DataAnalysisController {

    @Autowired
    ICourseRecordService courseRecordService;

    @RequestMapping("/dataAnalysis")
    public String dataAnalysis() {
        return "data.analysis";
    }

    /*
    *
    * 根据前台传来的数据筛选数据
    *
    * */
    @RequestMapping(value = "/showComment", produces = "text/html;charset=utf-8",
                    method = RequestMethod.GET)
    @ResponseBody
    public String showComment(HttpServletRequest httpServletRequest) throws Exception {
        DataRequest dr = DataTableUtil.trans(httpServletRequest);
        //获取sEcho，sEcho表示请求的次数 需要原封不动的传回页面
        String sEcho = httpServletRequest.getParameter("sEcho");
        //将页面传来的参数进行处理，其中主要是分页所需的信息
        //获得院系信息
        String department = httpServletRequest.getParameter("de");
        //获取课程名字
       String courseName = httpServletRequest.getParameter("courseName");
       if(courseName!=null&&!"".equals(courseName)){
           courseName= new String(courseName.getBytes("iso8859-1"),"utf-8");
       }
        //获取用户名称
        String keyName = httpServletRequest.getParameter("name");
        if(keyName!=null&&!"".equals(keyName)){
            keyName= new String(keyName.getBytes("iso8859-1"),"utf-8");
        }
        //进行查询
        DataTableReturnObject dro = courseRecordService.getCourseRecordPageMode(dr, keyName, courseName, department);
        //转为json返回0
//        httpServletResponse.setCharacterEncoding("utf-8");
        return DataTableUtil.transToJsonStr(sEcho, dro);
    }

    /*
    *
    * 此方法用来下载Excel数据
    * */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView downloadExcel() {
        List<CourseRecord> all = courseRecordService.findAll();
        ModelAndView modelAndView = new ModelAndView(new MyExcel());
        modelAndView.addObject("all", all);
        return modelAndView;
    }

    /*
    * 用来展示教师听课的
    *
    * */
//    de:2
//    startYear:2011
//    endYear:2015
//    semesterNum:1
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    @ResponseBody
    public HighChartJson getAll(String department, String startYear, String endYear, String semesterNum) {
        String departmentName;
        List<CourseRecord> all = courseRecordService.findAll(department, startYear, endYear, semesterNum);
        HighChartSeries highChartSeries = new HighChartSeries();
        if (StringUtils.isNotEmpty(department)) {
            departmentName = courseRecordService.findDepartmentNameById(department);
        } else {
            departmentName = "信息工程学院";
        }

        List<String> xlist = new ArrayList<>();
        List<Yvalue> ylist = new ArrayList<>();
        for (CourseRecord courseRecord : all) {
            Yvalue yvalue = new Yvalue();
            String teacherName = courseRecord.getCourse().getUser().getNickName();
            xlist.add(teacherName);
            yvalue.setY(courseRecord.getScore());
            yvalue.setCourseName(courseRecord.getCourse().getCourseName());
            yvalue.setDate(courseRecord.getCourse().getSemester().getStartYear()
                    + "-" + courseRecord.getCourse().getSemester().getEndYear()
                    + "学期" + courseRecord.getCourse().getSemester().getSemesterNum());
            ylist.add(yvalue);
        }
        highChartSeries.setNames(departmentName + "听课记录评分");

        highChartSeries.setData(ylist);
        HighChartJson highChartJson = new HighChartJson();
        highChartJson.setCategories(xlist);
        highChartJson.setHighChartSeries(highChartSeries);
        highChartJson.setText("巢湖学院听课记录分数折线图");
        return highChartJson;
    }

    /*
    * @param 用来接收教师的姓名
    * @return 用来返回生成折线所需要的json数据
    * */
    @RequestMapping(value = "/getTeacher",method = RequestMethod.GET)
    @ResponseBody
    public HighChartJson getTeacher(String teacherName) {
        List<CourseRecord> byTeacherName = courseRecordService.findByTeacherName(teacherName);
        List<String> xlist = new ArrayList<>();
        List<Yvalue> ylist = new ArrayList<>();
        for (CourseRecord courseRecord : byTeacherName) {
            Yvalue yvalue = new Yvalue();
            String courseName = courseRecord.getCourse().getCourseName();
            xlist.add(courseName);
            yvalue.setY(courseRecord.getScore());
            yvalue.setDate(courseRecord.getCourse().getSemester().getStartYear()
                    + "-" + courseRecord.getCourse().getSemester().getEndYear()
                    + "学期" + courseRecord.getCourse().getSemester().getSemesterNum());
            ylist.add(yvalue);
        }
        HighChartSeries highChartSeries = new HighChartSeries();
        highChartSeries.setNames("同一教师历年课程评分");
        highChartSeries.setData(ylist);
        HighChartJson highChartJson = new HighChartJson();
        highChartJson.setCategories(xlist);
        highChartJson.setHighChartSeries(highChartSeries);
        highChartJson.setText("教师听课记录分数折线图");
        return highChartJson;
    }
}
