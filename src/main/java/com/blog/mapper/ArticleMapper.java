package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 文章 Mapper，含仪表盘统计所需的聚合查询。
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /** 文章总数 */
    @Select("SELECT COUNT(*) FROM article")
    long countTotalArticles();

    /** 阅读量 TOP N 文章，返回 list[{title, views}] */
    @Select("SELECT title, views FROM article ORDER BY views DESC LIMIT #{limit}")
    List<Map<String, Object>> selectTopArticles(@Param("limit") int limit);
}
