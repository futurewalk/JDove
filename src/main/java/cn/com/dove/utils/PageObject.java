package cn.com.dove.utils;

import com.alibaba.fastjson.JSON;

/**
 * Created by Administrator on 2017/4/21.
 * 分页工具类
 * author:JackWang
 */
public class PageObject {
    /**
     * 当前页
     */
    private int pageNum = 1;
    /**
     * 显示记录数
     */
    private int pageSize = 10;
    /**
     * 总页数
     */
    private int pageCount;
    /**
     *总记录数
     */
    private int rowCount;
    /**
     * 排序字段
     */
    private String sort;
    public PageObject(){}
    public PageObject(int page, int size){
        this.pageNum = page<=0?1:page;
        this.pageSize = size;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}
