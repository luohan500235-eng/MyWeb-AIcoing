# MyWeb Blog

一个基于 React + Spring Boot + MySQL 的个人博客项目，包含公开博客前台和后台管理台。

## 项目结构

- `frontend-web`: 公开博客前台
- `frontend-admin`: 后台管理台
- `backend`: Spring Boot 后端 API
- `deploy`: 本地数据库与 Nginx 示例配置

## 当前完成内容

第一阶段 MVP 已完成这些核心能力：

- 管理员登录与 JWT 鉴权
- 文章 CRUD、草稿/发布状态、SEO 字段
- 分类与标签管理
- 评论提交与后台审核
- 站点配置管理
- 文件上传与本地静态访问
- 公开博客首页、列表、详情、归档、关于页
- Flyway 初始化建表和示例数据

## 默认账号

- 用户名: `admin`
- 密码: `Admin123456`

## 默认端口

- 后端: `8080`
- 博客前台: `5173`
- 后台管理: `5174`
- MySQL: `3306`

## 本地启动

### 1. 启动 MySQL

如果你本机已安装 MySQL，请创建数据库：

```sql
CREATE DATABASE my_blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

默认后端连接信息在 [application.yml](C:/Users/lh/Desktop/MyWeb-AIcoing/backend/src/main/resources/application.yml) 中：

- 用户名: `root`
- 密码: `root123456`

你也可以参考 [docker-compose.yml](C:/Users/lh/Desktop/MyWeb-AIcoing/deploy/docker/docker-compose.yml) 手动用 Docker 启一个 MySQL 8.4 实例。

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

第一次启动会自动执行 Flyway 建表，并初始化：

- 管理员账号
- 站点配置
- 示例分类 / 标签 / 文章 / 评论

### 3. 启动前台

```bash
cd frontend-web
npm install
npm run dev
```

### 4. 启动后台

```bash
cd frontend-admin
npm install
npm run dev
```

## 构建验证

当前项目已完成以下构建验证：

- `backend`: `mvn -q -DskipTests compile`
- `frontend-web`: `npm run build`
- `frontend-admin`: `npm run build`

说明：当前环境里没有安装 Docker，所以我无法在这里直接拉起 MySQL 做完整联机启动验证，但代码层面的编译和前端打包已经通过。

## 主要接口

### 公开接口

- `GET /api/public/site-config`
- `GET /api/public/categories`
- `GET /api/public/tags`
- `GET /api/public/posts`
- `GET /api/public/posts/{slug}`
- `GET /api/public/posts/latest`
- `GET /api/public/archives`
- `GET /api/public/comments?postId=1`
- `POST /api/public/comments`

### 后台接口

- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET/POST/PUT/DELETE /api/admin/posts`
- `GET/POST/PUT/DELETE /api/admin/categories`
- `GET/POST/PUT/DELETE /api/admin/tags`
- `GET /api/admin/comments`
- `PUT /api/admin/comments/{id}/status`
- `POST /api/admin/files/upload`
- `GET/PUT /api/admin/site-config`
- `GET /api/admin/dashboard/stats`

## 下一步建议
11111
- 接入更完整的 Markdown 编辑器
- 为后台增加表单校验提示和分页
- 引入 MinIO / OSS 替代本地上传
- 增加单元测试与接口测试
- 迁移到 Java 17+ 与 Spring Boot 3.x
