package com.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan("com.blog.mapper")
public class BlogApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BlogApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        String baseUrl = env.getProperty("blog.base-url", "http://localhost:" + port);

        System.out.println("========================================");
        System.out.println("  博客系统启动成功！");
        System.out.println("  本地访问：http://localhost:" + port);
        System.out.println("  站点地址：" + baseUrl);
        System.out.println("========================================");
    }
}
