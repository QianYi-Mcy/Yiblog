package com.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 访问记录表 visit_record。
 */
@Data
@TableName("visit_record")
public class VisitRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 访客 IP */
    private String ip;

    /** 访客省份（由 IP 解析得到） */
    private String province;

    /** 被访问的文章 ID（可为空，表示访问首页/其他页面） */
    private Long articleId;

    /** 访问时间 */
    private LocalDateTime visitTime;
}
