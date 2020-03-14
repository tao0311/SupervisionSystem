package com.chtc.supervision.dto;

public class SemesterDto implements Comparable<SemesterDto>{

    private String id;

    private String startYear;

    private String semesterInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getSemesterInfo() {
        return semesterInfo;
    }

    public void setSemesterInfo(String semesterInfo) {
        this.semesterInfo = semesterInfo;
    }

    @Override
    public int compareTo(SemesterDto o) {
        return this.startYear.compareTo(o.getStartYear());
    }
}
