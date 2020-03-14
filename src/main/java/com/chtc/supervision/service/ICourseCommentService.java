package com.chtc.supervision.service;

import com.chtc.supervision.entity.CourseComment;
import com.chtc.util.DataRequest;
import com.chtc.util.DataTableReturnObject;
import com.chtc.util.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ICourseCommentService {

    Json createCourseComment(String id,HttpServletRequest request);

    CourseComment getCourseComment(String id);

    void saveCourseComment(CourseComment courseComment);

    void submitComment(String id);

    Json updateCheckedState(String[] arr, int state);

    String deleteCourseComment(String[] ids);

    Map<String, String> findCourseCommentByID(String id);

    DataTableReturnObject findAllCourseCommentsById(DataRequest dr, String id);

    DataTableReturnObject getCourseCommentPage(DataRequest dr, String searchRecordInfo, int state);

    List<CourseComment> findByTime(Date startTime, Date endTime,String createBy);

}
