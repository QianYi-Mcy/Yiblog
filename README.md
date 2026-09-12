# 个人博客系统（SpringBoot3 + Vue3 前后端分离）

一个基于 **Spring Boot 3 + MyBatis-Plus + MySQL** 的后端、**Vue 3 + Element Plus + ECharts** 的后台管理前端的全栈博客系统，并采用 **JWT** 做认证。
> 毕业设计项目

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 后端 | Spring Boot 3.3.2、Java 21 |
| 持久层 | MyBatis-Plus 3.5.7 |
| 数据库 | MySQL 8 |
| 认证 | JWT（jjwt 0.12.6） |
| 前端 | Vue 3 + Vite + Element Plus + ECharts |
| 构建 | Maven（后端）/ npm（前端） |
## 功能概述

### 前台（原有 Thymeleaf 页面）
- 博客首页：文章列表、摘要、浏览量
- 文章详情、浏览量原子自增
- 主题切换（绿 / 红）

### 后台管理（Vue3 单页应用）
- JWT 登录 / 退出
- 左侧可折叠导航（13 个菜单）、顶部面包屑 + 用户菜单
- **仪表盘**：
  - 7 个统计卡片（总浏览量、总访客数、今日浏览、今日新访客、文章总数、评论总数、待审评论）
  - 4 张 ECharts 图表（浏览量趋势、访客趋势、TOP10 文章、省份分布）
  - 底部展示「系统已稳定运行 XXX 天」
## 快速开始

### 1. 数据库

创建数据库并导入表结构（含可选的测试数据存储过程）：

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS blogdb DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -p blogdb < src/main/resources/db/dashboard.sql
```

配置见 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/blogdb?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=123456
```

> 请将用户名 / 密码改成你本地 MySQL 的账号密码后再启动。

### 2. 启动后端

```bash
mvn spring-boot:run
```

后端运行在 <http://localhost:8080>

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 <http://localhost:5173>（已配置 `/api` 代理到后端 8080，无跨域烦恼）。

默认管理员账号：**admin / admin123**（见建表脚本，请登录后修改）。

## 仪表盘接口一览

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/admin/auth/login` | 登录，返回 JWT |
| GET | `/api/admin/stats/dashboard` | 仪表盘 7 个统计数字 |
| GET | `/api/admin/stats/visitTrend?days=7` | 浏览量趋势 |
| GET | `/api/admin/stats/visitorTrend?days=7` | 访客趋势 |
| GET | `/api/admin/stats/topArticles?limit=10` | TOP10 文章阅读量 |
| GET | `/api/admin/stats/provinceDistribution` | 访客省份分布 |
| GET | `/api/admin/stats/runDays` | 系统运行天数 |

> 除登录接口外，`/api/admin/**` 均需在请求头携带 `Authorization: Bearer <token>`。

## 目录结构

```
├── src/main/java/com/blog
│   ├── common/         统一返回结构 Result
│   ├── config/         JWT 配置、拦截器、跨域、站点配置
│   ├── controller/     页面控制器 + Admin 接口控制器
│   ├── entity/         实体类（Article/User/Comment/Message/VisitRecord...）
│   ├── mapper/         MyBatis-Plus Mapper（含仪表盘聚合查询）
│   ├── service/        业务接口与实现（含 StatsService 统计逻辑）
│   ├── util/           JwtUtil
│   └── vo/             仪表盘返回对象
├── src/main/resources
│   ├── application.properties
│   ├── db/dashboard.sql       建表 + 测试数据脚本
│   ├── static/ · templates/   原有 Thymeleaf 前台
└── frontend/               Vue3 后台管理前端
    └── src/
        ├── api/           接口封装
        ├── layout/        后台布局（侧栏 + 顶栏）
        ├── router/        路由 + 登录守卫
        ├── utils/         axios 封装（JWT + 401 处理）
        └── views/         Dashboard / Login / 文章·分类·评论·留言管理 / Placeholder
```

## 说明

- JWT 密钥、过期时间、CORS 白名单、上线日期均可在 `application.properties` 中配置。
- 数据库默认账号密码为本地开发环境配置，正式部署请自行修改。

