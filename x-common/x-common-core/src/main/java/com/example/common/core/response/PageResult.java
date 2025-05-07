package com.example.common.core.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PageResult<T> {
    private List<T> list;
    private long total;
    private int pageNum;
    private int pageSize;

    public static <T> PageResult<T> of(List<T> list, long total, int pageNum, int pageSize) {
        return new PageResult<T>()
                .setList(list)
                .setTotal(total)
                .setPageNum(pageNum)
                .setPageSize(pageSize);
    }
} 