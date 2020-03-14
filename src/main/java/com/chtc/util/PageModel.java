package com.chtc.util;

import java.util.List;

/**
 * Created by admin on 2017/2/7.
 */
public class PageModel {
    /*翻页数据*/
    @SuppressWarnings("rawtypes")
    private List datas;
    /*当前页*/
    private int currPage;
    /*每页显示的记录条数*/
    private int pageSize = 20;//默认每页显示20条记录
    /*总记录条数*/
    private int totals;
    /*总页数*/
    private int pageCount;
    /*后一页*/
    private int nextPage;
    /*前一页*/
    private int prevPage;
    /*翻页的地址*/
    private String url;

    public PageModel(){

    }

    /**
     * 根据翻页要求构造翻页模型数据，默认每页显示20条
     * @param totals
     *          总记录数
     * @param datas
     *          翻页数据
     * @param currPage
     *          当前页
     */
    @SuppressWarnings("rawtypes")
    public PageModel(int totals, List datas, int currPage){
        this(totals, datas, 20, currPage);
    }

    /**
     * 根据翻页的要求来构造翻页模型
     * @param totals
     *          总记录数
     * @param datas
     *          翻页数据
     * @param pageSize
     *          每页显示的条数
     * @param currPage
     *          当前页
     */
    @SuppressWarnings("rawtypes")
    public PageModel(int totals, List datas, int pageSize, int currPage){
        this.totals = totals;
        this.datas = datas;
        this.pageSize = pageSize;
        this.currPage = currPage;
        if(totals % pageSize==0){
            this.pageCount = totals / pageSize;
        }else{
            this.pageCount = totals / pageSize + 1;
        }
        this.nextPage = this.currPage + 1 > this.pageCount ? this.pageCount : this.currPage + 1;
        this.prevPage = this.currPage - 1 < 1 ? 1:this.currPage - 1;

    }

    @SuppressWarnings("rawtypes")
    public List getDatas() {
        return datas;
    }

    @SuppressWarnings("rawtypes")
    public void setDatas(List datas) {
        this.datas = datas;
    }
    public int getCurrPage() {
        return currPage;
    }
    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }
    public int getPageSize() {
        return pageSize;
    }
    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public int getPageCount() {
        return pageCount;
    }
    public int getNextPage() {
        return nextPage;
    }
    public int getPrevPage() {
        return prevPage;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
