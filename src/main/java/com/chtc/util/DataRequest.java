package com.chtc.util;

public class DataRequest {
    //当前页码
    private int page;
    //页面可显示行数  
    private int rows;
    //用于排序的列名  
    private String sidx;
    //排序的方式desc/asc  
    private String sord;
    //是否是搜索请求  
    private String search;
    //已经发送的请求的次数  
    private String nd;
    //传的参数
    private String type;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
