package com.blog.controller;

import com.blog.entity.Article;
import com.blog.entity.User;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ArticleService articleService;

    // 后台首页（文章管理）
    @GetMapping
    public String index(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        List<Article> articles = articleService.list();
        model.addAttribute("articles", articles);
        model.addAttribute("user", loginUser);
        return "admin/index";
    }

    // 写新文章页面
    @GetMapping("/article/add")
    public String addPage(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        return "admin/article-edit";
    }

    // 编辑文章页面
    @GetMapping("/article/edit/{id}")
    public String editPage(@PathVariable Integer id, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        Article article = articleService.getById(id);
        model.addAttribute("article", article);
        return "admin/article-edit";
    }

    // 删除文章
    @GetMapping("/article/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        articleService.removeById(id);
        return "redirect:/admin";
    }
}
