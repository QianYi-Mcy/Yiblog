package com.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("article")
public class Article {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String content;

    private String summary;

    private String coverImage;

    private Integer categoryId;

    private Integer userId;

    private Integer views;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
