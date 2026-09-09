package com.blog.controller;

import com.blog.entity.Article;
import com.blog.entity.User;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // 获取文章列表
    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<Article> articles = articleService.getPublishedArticles();
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", articles);
        return result;
    }

    // 获取文章详情
    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        Article article = articleService.getArticleById(id);
        if (article != null) {
            // 浏览量+1
            article.setViews(article.getViews() + 1);
            articleService.updateById(article);
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", article);
        } else {
            result.put("code", 404);
            result.put("message", "文章不存在");
        }
        return result;
    }

    // 新增文章
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Article article, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }
        article.setUserId(loginUser.getId());
        if (article.getStatus() == null) {
            article.setStatus(1);
        }
        boolean success = articleService.save(article);
        if (success) {
            result.put("code", 200);
            result.put("message", "发布成功");
        } else {
            result.put("code", 500);
            result.put("message", "发布失败");
        }
        return result;
    }

    // 更新文章
    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Article article, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }
        boolean success = articleService.updateById(article);
        if (success) {
            result.put("code", 200);
            result.put("message", "更新成功");
        } else {
            result.put("code", 500);
            result.put("message", "更新失败");
        }
        return result;
    }

    // 删除文章
    @GetMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("code", 401);
            result.put("message", "请先登录");
            return result;
        }
        boolean success = articleService.removeById(id);
        if (success) {
            result.put("code", 200);
            result.put("message", "删除成功");
        } else {
            result.put("code", 500);
            result.put("message", "删除失败");
        }
        return result;
    }
}
