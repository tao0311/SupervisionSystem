package com.chtc.mobile.controller;

import com.chtc.supervision.service.ICourseRecordService;
import com.chtc.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mobile/courseRecord")
public class MobileRecordController {
    @Autowired
    ICourseRecordService courseRecordService;

    @RequestMapping("/createCourseRecord")
    @ResponseBody
    public Json createCourseRecord(@RequestParam("id") String id) {
        courseRecordService.createCourseRecord(id);
        Json json  = new Json();
        json.setSuccess(true);
        return json;
    }
}
