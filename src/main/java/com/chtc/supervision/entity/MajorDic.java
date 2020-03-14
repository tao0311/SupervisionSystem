package com.chtc.supervision.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "major_dic")
public class MajorDic extends BaseEntity implements java.io.Serializable, Comparable<MajorDic>{

    //专业名
    @NotBlank(message = "此字段不能为空！")
    @Column(length = 60)
    private String majorName;

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    @Override
    public int compareTo(MajorDic m) {
        return majorName.compareTo(m.majorName);
    }
}
