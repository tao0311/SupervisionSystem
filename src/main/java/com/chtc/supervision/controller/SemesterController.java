package com.chtc.supervision.controller;

import com.alibaba.fastjson.JSON;
import com.chtc.supervision.service.ISemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * 学期管理
 */
@Controller
public class SemesterController {

    @Autowired
    ISemesterService semesterService;

    /**
     * 从数据库查询所有的学期
     * @return
     */
    @RequestMapping(value = "/findAllSemester", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String findAllSemester() {
        Set<String> list = semesterService.findAllSemester();
        return JSON.toJSONString(list);
    }

}
