package com.nhcar.entity;

import java.util.List;

public class ECarNewsResult {
    private int pageSize;   //每页行数
    private int dataCount;  //总行数
    private int start;  //开始序号
    private int pageCount;  //总页数
    private int page;   //当前页码
    private List<ECarNews> dataResult;  //当前页数据

    public ECarNewsResult() {
    }

    public ECarNewsResult(int pageSize, int dataCount, int start, int pageCount, int page, List<ECarNews> dataResult) {
        this.pageSize = pageSize;
        this.dataCount = dataCount;
        this.start = start;
        this.pageCount = pageCount;
        this.page = page;
        this.dataResult = dataResult;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ECarNews> getDataResult() {
        return dataResult;
    }

    public void setDataResult(List<ECarNews> dataResult) {
        this.dataResult = dataResult;
    }
}
