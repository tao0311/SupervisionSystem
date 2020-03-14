package com.chtc.supervision.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * 听课记录具体内容
 */
@Entity
@Table(name = "course_comment")
public class CourseComment extends BaseEntity implements java.io.Serializable{

    //评论
    @Column(columnDefinition = "varchar(1500)")
    private String comment;

    //意见
    @Column(columnDefinition = "varchar(1500)")
    private String proposal;

    //得分
    private Float score;

    //班级总人数
    private Integer totalCount;

    //当前出勤人数
    private Integer presentCount;

    //审核状态
    private int state;

    //听课时间
    private String recordTime;

    //备注
    @Column(columnDefinition = "varchar(1500)")
    private String remark;

    //是否填写
    private int isEdit;

    //关联课程记录
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(name = "courseRecord_id")
    private CourseRecord courseRecord;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
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

    public CourseRecord getCourseRecord() {
        return courseRecord;
    }

    public void setCourseRecord(CourseRecord courseRecord) {
        this.courseRecord = courseRecord;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(int isEdit) {
        this.isEdit = isEdit;
    }
}
