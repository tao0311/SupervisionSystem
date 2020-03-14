package com.chtc.supervision.controller;

import com.chtc.supervision.service.ICourseRecordService;
import com.chtc.supervision.service.IUserService;
import com.chtc.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 课程记录管理
 */
@Controller
@RequestMapping("/courseRecord")
public class CourseRecordController {

    @Autowired
    private ICourseRecordService courseRecordService;

    @RequestMapping(value = "/createCourseRecord",method = RequestMethod.GET)
    @ResponseBody
    public Json createCourseRecord(@RequestParam("id") String id) {
        courseRecordService.createCourseRecord(id);
        Json json  = new Json();
        json.setSuccess(true);
        return json;
    }

    @RequestMapping(value = "/courseRecordList",method = RequestMethod.GET)
    public String courseRecordList() {
        return "courseRecord.list";
    }

    //通过前台传过来的用户的id查询到听课记录
    @RequestMapping(value = "/fetchRecords",produces = "application/json; charset=utf-8")
    @ResponseBody
    public String fetchRecords(HttpServletRequest request) throws Exception{
        String sEcho = request.getParameter("sEcho");
//        String id = request.getParameter("id");
        String searchCode=request.getParameter("searchRecordsCode");//查询的课程编号
        String searchName=request.getParameter("searchRecordsName");//查询的课程名称
//        searchName=new String(searchName.getBytes("ISO-8859-1"),"utf-8");//因为DataTable传过来的参数编码格式不是UTF-8 需要重新编码
        String searchMajor=request.getParameter("searchRecordsMajor");//查询的课程名称
//        searchMajor=new String(searchMajor.getBytes("ISO-8859-1"),"utf-8");
        DataRequest dr = DataTableUtil.trans(request);
        DataTableReturnObject dro =  courseRecordService.findAllCourseRecordsById(dr,searchCode,searchName,searchMajor);
        return DataTableUtil.transToJsonStr(sEcho,dro);

    }
}
