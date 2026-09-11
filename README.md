# 个人博客系统（SpringBoot3 + JDK25）

一个基于 Spring Boot 3 + MyBatis-Plus + Thymeleaf 的轻量级个人博客系统，采用玻璃拟态（Glassmorphism）风格，支持浅色主题下的绿/红双配色切换。

> 毕业设计项目

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 语言 | Java 25 |
| 框架 | Spring Boot 3.3.2 |
| 持久层 | MyBatis-Plus 3.5.7 |
| 数据库 | MySQL 8 |
| 模板引擎 | Thymeleaf |
| 前端 | 原生 HTML / CSS / JavaScript |
| 构建 | Maven |

## 功能概述

- 博客首页：文章列表、摘要、浏览量、发布时间
- 文章详情：正文渲染、浏览量自增
- 后台管理：登录鉴权、文章增删改查、发布 / 草稿状态
- 主题切换：绿色 / 红色双配色，记忆用户选择
- 站点配置：标题、副标题、版权、主题色可通过配置文件调整
- 全局异常处理：统一友好的错误页

## 快速开始

### 1. 准备数据库

```sql
CREATE DATABASE blog DEFAULT CHARACTER SET utf8mb4;
```

并创建 `article`、`category`、`user` 等表（字段与 `com.blog.entity` 下实体一一对应）。

### 2. 修改配置

编辑 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/blog?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=你的密码
```

### 3. 启动

```bash
mvn spring-boot:run
```

浏览器访问：<http://localhost:8080>

- 前台首页：`/`
- 后台登录：`/login`

## 目录结构

```
src/main/java/com/blog
├── BlogApplication.java      启动类
├── common/                   通用返回结构
├── config/                   站点配置、全局异常处理
├── controller/               Web 控制器
├── entity/                   实体类
├── mapper/                   MyBatis-Plus Mapper
└── service/                  业务接口与实现

src/main/resources
├── application.properties    应用配置
├── static/                   CSS / JS
└── templates/                Thymeleaf 模板
```

## 接口一览

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/` | 博客首页 |
| GET | `/article/{id}` | 文章详情 |
| GET | `/login` / POST `/login` | 登录 |
| GET | `/logout` | 退出登录 |
| GET | `/admin` | 后台文章管理 |
| POST | `/api/article/add` | 新增文章 |
| POST | `/api/article/update` | 更新文章 |
| POST | `/api/article/delete/{id}` | 删除文章 |
| GET | `/api/article/list` | 文章列表 |
| GET | `/hello` | 健康检查 |

## 说明

本项目用于学习与毕业设计展示，数据库默认账号密码为本地开发环境配置，正式部署请自行修改。
