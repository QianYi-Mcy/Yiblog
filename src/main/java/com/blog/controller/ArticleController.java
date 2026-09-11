package com.blog.controller;

import com.blog.common.Result;
import com.blog.entity.Article;
import com.blog.entity.User;
import com.blog.service.ArticleService;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 获取文章列表
    @GetMapping("/list")
    public Result<List<Article>> list() {
        return Result.ok(articleService.getPublishedArticles());
    }

    // 获取文章详情
    @GetMapping("/{id}")
    public Result<Article> detail(@PathVariable Integer id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            return Result.fail(404, "文章不存在");
        }
        articleService.increaseViews(id);
        article.setViews((article.getViews() == null ? 0 : article.getViews()) + 1);
        return Result.ok(article);
    }
    // 新增文章
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Article article, HttpSession session) {
        User loginUser = currentUser(session);
        if (loginUser == null) {
            return Result.fail(401, "请先登录");
        }
        article.setId(null);
        article.setUserId(loginUser.getId());
        if (article.getStatus() == null) {
            article.setStatus(1);
        }
        article.setViews(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        return articleService.save(article)
                ? Result.success("发布成功")
                : Result.fail("发布失败");
    }
    // 更新文章
    @PostMapping("/update")
    public Result<Void> update(@RequestBody Article article, HttpSession session) {
        if (currentUser(session) == null) {
            return Result.fail(401, "请先登录");
        }
        if (article.getId() == null) {
            return Result.fail(400, "缺少文章ID");
        }
        article.setUpdateTime(LocalDateTime.now());
        return articleService.updateById(article)
                ? Result.success("更新成功")
                : Result.fail("更新失败");
    }
    // 删除文章
    @PostMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Integer id, HttpSession session) {
        if (currentUser(session) == null) {
            return Result.fail(401, "请先登录");
        }
        return articleService.removeById(id)
                ? Result.success("删除成功")
                : Result.fail("删除失败");
    }

    // 提取当前登录用户
    private User currentUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }
}

