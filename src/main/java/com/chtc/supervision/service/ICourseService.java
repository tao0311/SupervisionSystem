package com.chtc.supervision.service;

import com.chtc.supervision.dto.CourseDTO;
import com.chtc.util.CourseQueryUtil;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import org.springframework.web.multipart.MultipartFile;

public interface ICourseService {

    String addCourseManual(CourseDTO course);

    String queryCourseByCode(String code);

    String addCourseByFile(MultipartFile excel_file);

    DataTableReturnObject getCoursePageMode(DataRequest dr, CourseQueryUtil courseQuery);

    CourseDTO queryCourseById(String id);

    String updateCourse(CourseDTO course);

    String deleteCourse(String[] ids);

    DataTableReturnObject getCourseToMobile(DataRequest dr, String searchCourseInfo);
}
