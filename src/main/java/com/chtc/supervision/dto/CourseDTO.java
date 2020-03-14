package com.chtc.supervision.dto;

import com.chtc.supervision.entity.Department;
import com.chtc.supervision.entity.MajorDic;
import com.chtc.supervision.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 课程的数据传输类
 */

public class CourseDTO {

    //课程id
    private String id;

    //课程编号
    private String courseCode;

    //课程名称
    private String courseName;

    //授课教室
    private String courseRoom;

    //单双周
    private String oddEven;

    //授课时间
    private String courseDate;

    //从第几节开始上课
    private int courseNode;

    //课程节数
    private int courseNum;

    //起始周
    private int startWeek;

    //结束周
    private int endWeek;

    //年级
    private int grade;

    //专业
    private String major;

    //班级
    private int classes;

    //学年
    private String semesterYear;

    //具体学期
    private int semesterNum;

    //教师id
    private String teacherId;

    //部门id
    private String departmentId;

    //所有的部门
    private List<Department> departments = new ArrayList<>();

    //该部门所有的老师
    private List<User> teachers = new ArrayList<>();

    //所有的学年信息
    private Set<SemesterDto> semesters = new TreeSet<>();

    //所有的专业
    private Set<MajorDic> majorDicSet = new TreeSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseRoom() {
        return courseRoom;
    }

    public void setCourseRoom(String courseRoom) {
        this.courseRoom = courseRoom;
    }

    public String getOddEven() {
        return oddEven;
    }

    public void setOddEven(String oddEven) {
        this.oddEven = oddEven;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String courseDate) {
        this.courseDate = courseDate;
    }

    public int getCourseNode() {
        return courseNode;
    }

    public void setCourseNode(int courseNode) {
        this.courseNode = courseNode;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(int courseNum) {
        this.courseNum = courseNum;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getClasses() {
        return classes;
    }

    public void setClasses(int classes) {
        this.classes = classes;
    }

    public String getSemesterYear() {
        return semesterYear;
    }

    public void setSemesterYear(String semesterYear) {
        this.semesterYear = semesterYear;
    }

    public int getSemesterNum() {
        return semesterNum;
    }

    public void setSemesterNum(int semesterNum) {
        this.semesterNum = semesterNum;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<User> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<User> teachers) {
        this.teachers = teachers;
    }

    public Set<SemesterDto> getSemesters() {
        return semesters;
    }

    public void setSemesters(Set<SemesterDto> semesters) {
        this.semesters = semesters;
    }

    public Set<MajorDic> getMajorDicSet() {
        return majorDicSet;
    }

    public void setMajorDicSet(Set<MajorDic> majorDicSet) {
        this.majorDicSet = majorDicSet;
    }
}
