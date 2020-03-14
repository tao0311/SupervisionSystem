package com.chtc.util;

import java.util.List;

public class HighChartSeries {
    String names;//表示当前曲线的名字
    List<Yvalue> data;//教师的评分
    String teacherDe;

    public String getTeacherDe() {
        return teacherDe;
    }

    public void setTeacherDe(String teacherDe) {
        this.teacherDe = teacherDe;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public List<Yvalue>  getData() {
        return data;
    }

    public void setData(List<Yvalue> data) {
        this.data = data;
    }
}
