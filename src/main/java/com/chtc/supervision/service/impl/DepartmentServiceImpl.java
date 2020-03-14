package com.chtc.supervision.service.impl;

import com.chtc.supervision.entity.Department;
import com.chtc.supervision.repository.DepartmentRepository;
import com.chtc.supervision.service.IDepartmentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements IDepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public List<Department> findAllDepartment() {
        return departmentRepository.findAll();
    }

}
