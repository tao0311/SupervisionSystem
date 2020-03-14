package com.chtc.util;

import javax.servlet.http.HttpServletRequest;

public class CourseQueryUtil {
    private String searchCourseInfo;

    private String semesterYear;

    private String semesterNum;

    private String searchCourseDate;


    public static CourseQueryUtil trans(HttpServletRequest request){
        CourseQueryUtil courseQuery = new CourseQueryUtil();
        courseQuery.setSearchCourseInfo(request.getParameter("searchCourseInfo"));
        courseQuery.setSemesterYear(request.getParameter("semesterYear"));
        courseQuery.setSemesterNum(request.getParameter("semesterNum"));
        courseQuery.setSearchCourseDate(request.getParameter("searchCourseDate"));
        return courseQuery;
    }

    public String getSearchCourseInfo() {
        return searchCourseInfo;
    }

    public void setSearchCourseInfo(String searchCourseInfo) {
        this.searchCourseInfo = searchCourseInfo;
    }

    public String getSemesterYear() {
        return semesterYear;
    }

    public void setSemesterYear(String semesterYear) {
        this.semesterYear = semesterYear;
    }

    public String getSemesterNum() {
        return semesterNum;
    }

    public void setSemesterNum(String semesterNum) {
        this.semesterNum = semesterNum;
    }

    public String getSearchCourseDate() {
        return searchCourseDate;
    }

    public void setSearchCourseDate(String searchCourseDate) {
        this.searchCourseDate = searchCourseDate;
    }
}
