package com.chtc.util;

import java.util.List;

public class HighChartJson {
    List<String> categories;//category是x 的坐标

    public List<String> getCourseName() {
        return courseName;
    }

    public void setCourseName(List<String> courseName) {
        this.courseName = courseName;
    }

    List<String> courseName;
    String text;//标题
    HighChartSeries highChartSeries;


    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HighChartSeries getHighChartSeries() {
        return highChartSeries;
    }

    public void setHighChartSeries(HighChartSeries highChartSeries) {
        this.highChartSeries = highChartSeries;
    }
}
