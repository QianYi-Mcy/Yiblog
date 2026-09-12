package com.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论表 comment。
 */
@Data
@TableName("comment")
public class Comment implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联文章 ID */
    private Long articleId;

    /** 评论者昵称 */
    private String nickname;

    /** 评论内容 */
    private String content;

    /** 状态：0-待审核，1-已通过，2-已拒绝 */
    private Integer status;

    /** 评论时间 */
    private LocalDateTime createTime;
}
