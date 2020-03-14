package com.chtc.supervision.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * 听课记录
 */
@Entity
@Table(name = "course_record")
public class CourseRecord extends BaseEntity implements java.io.Serializable {

    //听课老师的人数
    private int recordCount;

    //应到人数
    private Integer totalCount;

    //实到人数
    private Integer presentCount;

    //得分
    private Float score;

    //听课时间
    private String recordTime;

    //
    private int checkedCount;

    //审核状态
    private int state;

    //课程
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(name = "course_id")
    private Course course;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(Integer presentCount) {
        this.presentCount = presentCount;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public int getCheckedCount() {
        return checkedCount;
    }

    public void setCheckedCount(int checkedCount) {
        this.checkedCount = checkedCount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
