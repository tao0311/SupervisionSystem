package com.chtc.supervision.controller;

import com.alibaba.fastjson.JSON;
import com.chtc.supervision.service.IMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MajorController {

    @Autowired
    IMajorService majorService;

    @RequestMapping(value = "/findAllMajor", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String findAllMajor(){
        return JSON.toJSONString(majorService.findAllMajor());
    }
}
