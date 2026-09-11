package com.blog.controller;

import com.blog.config.SiteConfig;
import com.blog.entity.Article;
import com.blog.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PageController {

    private final ArticleService articleService;
    private final SiteConfig siteConfig;

    // 构造器注入：依赖清晰、便于测试
    public PageController(ArticleService articleService, SiteConfig siteConfig) {
        this.articleService = articleService;
        this.siteConfig = siteConfig;
    }

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
    public String articleDetail(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            ra.addFlashAttribute("message", "文章不存在或已被删除");
            return "redirect:/";
        }
        // 浏览量原子自增后再展示 +1 后的值
        articleService.increaseViews(id);
        article.setViews((article.getViews() == null ? 0 : article.getViews()) + 1);
        model.addAttribute("article", article);
        return "detail";
    }
}
