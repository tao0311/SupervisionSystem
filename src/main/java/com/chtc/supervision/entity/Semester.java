package com.chtc.supervision.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 学期
 */
@Entity
@Table(name = "semester")
public class Semester extends BaseEntity implements java.io.Serializable {

    //开始年份
    private String startYear;

    //结束年份
    private String endYear;

    //第几学期
    private int semesterNum;

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public int getSemesterNum() {
        return semesterNum;
    }

    public void setSemesterNum(int semesterNum) {
        this.semesterNum = semesterNum;
    }
}
