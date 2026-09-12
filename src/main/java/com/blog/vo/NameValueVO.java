package com.blog.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用「名称-数值」数据点，用于饼图 / 柱状图。
 */
@Data
public class NameValueVO implements Serializable {

    private String name;

    private long value;

    public NameValueVO() {
    }

    public NameValueVO(String name, long value) {
        this.name = name;
        this.value = value;
    }
}
