package com.blog.controller;

import com.blog.config.SiteConfig;
import com.blog.entity.Article;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SiteConfig siteConfig;

    // 所有页面共享站点配置（标题、副标题、默认主题等）
    @ModelAttribute("site")
    public SiteConfig site() {
        return siteConfig;
    }

    // 博客首页
    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleService.getPublishedArticles();
        model.addAttribute("articles", articles);
        return "index";
    }

    // 文章详情页
    @GetMapping("/article/{id}")
    public String articleDetail(@PathVariable Integer id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "detail";
    }
}
