package com.example.common.core.utils;

import lombok.Data;
import java.util.List;

@Data
public class PageUtils<T> {
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 每页记录数
     */
    private long size;
    
    /**
     * 当前页数
     */
    private long current;
    
    /**
     * 总页数
     */
    private long pages;
    
    /**
     * 列表数据
     */
    private List<T> records;

    public PageUtils(List<T> list, long total, long size, long current) {
        this.records = list;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = (total + size - 1) / size;
    }
} 