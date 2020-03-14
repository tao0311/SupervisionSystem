package com.chtc.supervision.service;

import com.chtc.supervision.entity.Semester;

import java.util.Set;

public interface ISemesterService {

    Set<String> findAllSemester();

    Semester findSemster(String startYear, String endYear, int i);

    Semester save(Semester semster);
}
