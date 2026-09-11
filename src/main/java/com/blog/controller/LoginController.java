package com.blog.controller;

import com.blog.config.SiteConfig;
import com.blog.entity.User;
import com.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;
    private final SiteConfig siteConfig;

    public LoginController(UserService userService, SiteConfig siteConfig) {
        this.userService = userService;
        this.siteConfig = siteConfig;
    }

    // 登录页面共享站点配置
    @ModelAttribute("site")
    public SiteConfig site() {
        return siteConfig;
    }

    // 登录页面
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 登录提交
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            // 脱敏：不把密码放进 session
            user.setPassword(null);
            session.setAttribute("loginUser", user);
            return "redirect:/admin";
        }
        model.addAttribute("error", "用户名或密码错误");
        model.addAttribute("username", username);
        return "login";
    }

    // 退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
