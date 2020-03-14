package com.chtc.supervision.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 学期任务数
 */
@Entity
@Table(name = "task")
public class Task extends BaseEntity implements java.io.Serializable{

    //任务数量
    @NotNull(message = "此字段不能为空")
    private int courseTimes;
    //TODO 命名修改
    //完成次数
    private int finishTimes;

    //学期
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(name = "semster_id")
    private Semester semester;

    //关联用户

    @ManyToOne( fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    public int getCourseTimes() {
        return courseTimes;
    }

    public void setCourseTimes(int courseTimes) {
        this.courseTimes = courseTimes;
    }

    public int getFinishTimes() {
        return finishTimes;
    }

    public void setFinishTimes(int finishTimes) {
        this.finishTimes = finishTimes;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
