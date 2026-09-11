package com.blog.controller;

import com.blog.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查 / 状态接口，用于部署后快速验证服务是否正常。
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.ok("服务正常", "Hello SpringBoot3 + JDK25");
    }
}

