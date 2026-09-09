package com.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.blog.mapper")
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        System.out.println("========================================");
        System.out.println("  博客系统启动成功！");
        System.out.println("  访问地址：http://101.33.218.211:8080");
        System.out.println("========================================");
    }
}
