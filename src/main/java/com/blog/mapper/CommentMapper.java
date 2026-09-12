package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 评论 Mapper。
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /** 评论总数 */
    @Select("SELECT COUNT(*) FROM comment")
    long countTotalComments();

    /** 待审核评论数（status = 0） */
    @Select("SELECT COUNT(*) FROM comment WHERE status = 0")
    long countPendingComments();
}
