package com.blog.controller;

import com.blog.config.SiteConfig;
import com.blog.entity.Article;
import com.blog.entity.User;
import com.blog.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ArticleService articleService;
    private final SiteConfig siteConfig;

    public AdminController(ArticleService articleService, SiteConfig siteConfig) {
        this.articleService = articleService;
        this.siteConfig = siteConfig;
    }

    // 所有后台页面共享站点配置
    @ModelAttribute("site")
    public SiteConfig site() {
        return siteConfig;
    }

    // 后台首页（文章管理）
    @GetMapping
    public String index(HttpSession session, Model model) {
        User loginUser = currentUser(session);
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
        if (currentUser(session) == null) {
            return "redirect:/login";
        }
        return "admin/article-edit";
    }

    // 编辑文章页面
    @GetMapping("/article/edit/{id}")
    public String editPage(@PathVariable Integer id, HttpSession session, Model model) {
        if (currentUser(session) == null) {
            return "redirect:/login";
        }
        Article article = articleService.getById(id);
        if (article == null) {
            return "redirect:/admin";
        }
        model.addAttribute("article", article);
        return "admin/article-edit";
    }

    // 删除文章（POST，避免被爬虫/预取误删）
    @PostMapping("/article/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        if (currentUser(session) == null) {
            return "redirect:/login";
        }
        if (articleService.removeById(id)) {
            ra.addFlashAttribute("message", "文章已删除");
        } else {
            ra.addFlashAttribute("message", "删除失败，文章可能已不存在");
        }
        return "redirect:/admin";
    }

    // 提取当前登录用户
    private User currentUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }
}
