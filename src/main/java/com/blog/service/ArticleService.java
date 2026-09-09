package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {

    // 获取所有已发布文章（按创建时间倒序）
    List<Article> getPublishedArticles();

    // 根据ID获取文章详情
    Article getArticleById(Integer id);
}
