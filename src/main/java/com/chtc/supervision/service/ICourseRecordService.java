package com.chtc.supervision.service;

import com.chtc.supervision.entity.CourseRecord;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;

import java.util.Date;
import java.util.List;

public interface ICourseRecordService {

    void createCourseRecord(String id);

    DataTableReturnObject getCourseRecordPageMode(DataRequest dr, String keyName, String code, String department);

    List<CourseRecord> findAll(String department, String start, String end, String seNumber );

    List<CourseRecord> findAll();

    String findDepartmentNameById(String id);

    List<CourseRecord> findByTeacherName(String teacherName);

    DataTableReturnObject findAllCourseRecordsById(DataRequest dr, String searchCode, String searchName, String searchMajor);
}
