package com.chtc.supervision.controller;

import com.chtc.supervision.entity.Department;
import com.chtc.supervision.service.IDepartmentService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 院系管理
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    IDepartmentService departmentService;

    @RequestMapping(value = "/findAllDepartment",produces = "text/html;charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public String findAllDepartment(){
        List<Department> list = departmentService.findAllDepartment();
        JSONArray jsonArray = new JSONArray();
        for (Department department:list){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",department.getId());
            jsonObject.put("name",department.getDepartmentName());
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

}
