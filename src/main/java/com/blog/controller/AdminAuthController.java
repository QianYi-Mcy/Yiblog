package com.blog.controller;

import com.blog.common.Result;
import com.blog.entity.User;
import com.blog.service.UserService;
import com.blog.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台登录认证接口（返回 JWT Token）。
 */
@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AdminAuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /** 登录请求体 */
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * 登录。成功返回 token 与用户信息。
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest req) {
        User user = userService.login(req.getUsername(), req.getPassword());
        if (user == null) {
            return Result.fail(401, "用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatar", user.getAvatar());
        data.put("user", userInfo);
        return Result.ok("登录成功", data);
    }
}
