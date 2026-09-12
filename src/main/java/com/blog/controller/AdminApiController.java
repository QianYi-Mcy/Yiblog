package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.Result;
import com.blog.entity.Article;
import com.blog.entity.Category;
import com.blog.entity.Comment;
import com.blog.entity.Message;
import com.blog.entity.User;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.CategoryMapper;
import com.blog.mapper.CommentMapper;
import com.blog.mapper.MessageMapper;
import com.blog.mapper.UserMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台管理通用接口：文章 / 评论 / 留言 / 分类 / 用户 的分页查询与增删改。
 * <p>所有接口均在 /api/admin/** 下，由 JwtInterceptor 统一鉴权。</p>
 */
@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;
    private final MessageMapper messageMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public AdminApiController(ArticleMapper articleMapper,
                              CommentMapper commentMapper,
                              MessageMapper messageMapper,
                              CategoryMapper categoryMapper,
                              UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.commentMapper = commentMapper;
        this.messageMapper = messageMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }

    // ===================== 文章管理 =====================

    @GetMapping("/articles")
    public Result<Map<String, Object>> pageArticles(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(Article::getTitle, keyword);
        }
        wrapper.orderByDesc(Article::getId);
        Page<Article> p = articleMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.ok(pageResult(p));
    }

    @GetMapping("/articles/{id}")
    public Result<Article> getArticle(@PathVariable Integer id) {
        Article article = articleMapper.selectById(id);
        return article == null ? Result.fail(404, "文章不存在") : Result.ok(article);
    }

    @PostMapping("/articles")
    public Result<Void> createArticle(@RequestBody Article article) {
        article.setId(null);
        if (article.getViews() == null) {
            article.setViews(0);
        }
        if (article.getStatus() == null) {
            article.setStatus(1);
        }
        LocalDateTime now = LocalDateTime.now();
        article.setCreateTime(now);
        article.setUpdateTime(now);
        return articleMapper.insert(article) > 0
                ? Result.success("发布成功") : Result.fail("发布失败");
    }

    @PutMapping("/articles/{id}")
    public Result<Void> updateArticle(@PathVariable Integer id, @RequestBody Article article) {
        article.setId(id);
        article.setUpdateTime(LocalDateTime.now());
        return articleMapper.updateById(article) > 0
                ? Result.success("更新成功") : Result.fail("更新失败");
    }

    @DeleteMapping("/articles/{id}")
    public Result<Void> deleteArticle(@PathVariable Integer id) {
        return articleMapper.deleteById(id) > 0
                ? Result.success("删除成功") : Result.fail("删除失败");
    }

    // ===================== 评论管理 =====================

    @GetMapping("/comments")
    public Result<Map<String, Object>> pageComments(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Comment::getStatus, status);
        }
        wrapper.orderByDesc(Comment::getId);
        Page<Comment> p = commentMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.ok(pageResult(p));
    }

    /** 审核评论：status 0待审 1通过 2拒绝 */
    @PutMapping("/comments/{id}/status")
    public Result<Void> auditComment(@PathVariable Long id, @RequestParam Integer status) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setStatus(status);
        return commentMapper.updateById(comment) > 0
                ? Result.success("操作成功") : Result.fail("操作失败");
    }

    @DeleteMapping("/comments/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        return commentMapper.deleteById(id) > 0
                ? Result.success("删除成功") : Result.fail("删除失败");
    }

    // ===================== 留言管理 =====================

    @GetMapping("/messages")
    public Result<Map<String, Object>> pageMessages(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Message::getId);
        Page<Message> p = messageMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.ok(pageResult(p));
    }

    @DeleteMapping("/messages/{id}")
    public Result<Void> deleteMessage(@PathVariable Long id) {
        return messageMapper.deleteById(id) > 0
                ? Result.success("删除成功") : Result.fail("删除失败");
    }

    // ===================== 分类管理 =====================

    @GetMapping("/categories")
    public Result<java.util.List<Category>> listCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        return Result.ok(categoryMapper.selectList(wrapper));
    }

    @PostMapping("/categories")
    public Result<Void> createCategory(@RequestBody Category category) {
        category.setId(null);
        category.setCreateTime(LocalDateTime.now());
        return categoryMapper.insert(category) > 0
                ? Result.success("新增成功") : Result.fail("新增失败");
    }

    @PutMapping("/categories/{id}")
    public Result<Void> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        category.setId(id);
        return categoryMapper.updateById(category) > 0
                ? Result.success("更新成功") : Result.fail("更新失败");
    }

    @DeleteMapping("/categories/{id}")
    public Result<Void> deleteCategory(@PathVariable Integer id) {
        return categoryMapper.deleteById(id) > 0
                ? Result.success("删除成功") : Result.fail("删除失败");
    }

    // ===================== 用户（个人资料/访客） =====================

    @GetMapping("/users")
    public Result<Map<String, Object>> pageUsers(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getId);
        Page<User> p = userMapper.selectPage(new Page<>(page, size), wrapper);
        p.getRecords().forEach(u -> u.setPassword(null));
        return Result.ok(pageResult(p));
    }

    @GetMapping("/users/{id}")
    public Result<User> getUser(@PathVariable Integer id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user == null ? Result.fail(404, "用户不存在") : Result.ok(user);
    }

    @PutMapping("/users/{id}")
    public Result<Void> updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        // 不允许通过该接口修改密码
        user.setPassword(null);
        return userMapper.updateById(user) > 0
                ? Result.success("更新成功") : Result.fail("更新失败");
    }

    /** 把 MyBatis-Plus 分页对象转换为前端友好的结构 */
    private Map<String, Object> pageResult(Page<?> page) {
        Map<String, Object> map = new HashMap<>();
        map.put("records", page.getRecords());
        map.put("total", page.getTotal());
        map.put("current", page.getCurrent());
        map.put("size", page.getSize());
        map.put("pages", page.getPages());
        return map;
    }
}
