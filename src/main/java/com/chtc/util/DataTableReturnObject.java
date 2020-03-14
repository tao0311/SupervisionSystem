package com.chtc.util;

import java.util.List;

public class DataTableReturnObject {
    private long iTotalRecords; //数据的总量
    private long iTotalDisplayRecords; //要展示到页面的数据总量
    private String sEcho;//请求的次数
    private List aaData;// 每行记录


    public DataTableReturnObject() {
        super();
    }

    public long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public List getAaData() {
        return aaData;
    }

    public void setAaData(List aaData) {
        this.aaData = aaData;
    }

}
