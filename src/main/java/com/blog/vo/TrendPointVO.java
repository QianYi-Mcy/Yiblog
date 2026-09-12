package com.blog.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 趋势图上的一个数据点：日期 + 数量。
 */
@Data
public class TrendPointVO implements Serializable {

    /** 日期，如 2026-01-01 */
    private String date;

    /** 数量 */
    private long count;

    public TrendPointVO() {
    }

    public TrendPointVO(String date, long count) {
        this.date = date;
        this.count = count;
    }
}
