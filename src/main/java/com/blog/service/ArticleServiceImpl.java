package com.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public List<Article> getPublishedArticles() {
        return list(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, 1)
                .orderByDesc(Article::getCreateTime));
    }

    @Override
    public Article getArticleById(Integer id) {
        return getById(id);
    }

    @Override
    public void increaseViews(Integer id) {
        update(new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, id)
                .setSql("views = views + 1"));
    }
}
