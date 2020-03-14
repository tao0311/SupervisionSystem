package com.chtc.supervision.service.impl;

import com.chtc.supervision.entity.Semester;
import com.chtc.supervision.repository.SemesterRepository;
import com.chtc.supervision.service.ISemesterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class SemesterServiceImpl implements ISemesterService{

    @Autowired
    SemesterRepository semesterRepository;

    /**
     * 返回所有不重复的学年（供前台显示学年信息）
     * @return 不重复的学年
     */
    @Override
    public Set<String> findAllSemester() {
        List<Semester> semesterList = semesterRepository.findAll();
        Set<String> allSemester = new TreeSet<>();

        for(Semester semester : semesterList){
            String semester_year = semester.getStartYear() + "-" +semester.getEndYear() + "学年";
            allSemester.add(semester_year);
        }
        return allSemester;
    }

    @Override
    public Semester findSemster(String startYear, String endYear, int i) {
        return semesterRepository.findSemster(startYear,endYear,i);
    }

    @Override
    public Semester save(Semester semester) {
        Semester save = semesterRepository.save(semester);
        return save;
    }
}
