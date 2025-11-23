# 失物招领平台 (Lost and Found Platform)

一个基于Java Web技术的失物招领平台，采用Servlet和JSP实现，使用MVC架构模式。该平台旨在帮助用户快速发布和查找失物信息，连接失主与拾主，促进社会正能量的传播。

## 技术栈

- **后端技术**: Java Servlet/JSP, JDBC
- **前端技术**: HTML5, CSS3, JavaScript, Bootstrap 5
- **数据库**: MySQL 8.0
- **构建工具**: Apache Maven
- **版本控制**: Git
- **测试框架**: JUnit 4
- **服务器**: Apache Tomcat 9+

## 功能特性

### 用户管理
- 用户注册和登录功能
- 基于Session的用户身份验证
- 用户角色区分（普通用户和管理员）

### 失物管理
- 失物信息发布、编辑和删除
- 失物信息浏览和详情查看
- 失物图片上传功能
- 失物信息搜索和筛选（按关键词、分类、地点等）

### 招领管理
- 招领信息发布、编辑和删除
- 招领信息浏览和详情查看
- 招领图片上传功能
- 招领信息搜索和筛选（按关键词、分类、地点等）

### 管理员功能
- 管理员后台面板
- 失物和招领信息审核
- 用户管理功能
- 系统数据统计和监控
- 批量删除失物信息功能
- 批量删除招领信息功能

### 界面设计
- 响应式设计，支持PC和移动设备
- 现代化的UI界面，提供良好的用户体验
- 中英文双语支持，提升国际化水平
- 平滑的动画效果和交互体验

## 系统架构

本项目采用经典的MVC（Model-View-Controller）架构模式，将业务逻辑、数据和界面显示分离：

- **Model层**: 包含实体类（User、LostItem、FoundItem等）和数据访问对象（DAO）
- **View层**: JSP页面负责展示数据和用户界面
- **Controller层**: Servlet控制器处理用户请求和业务逻辑

## 数据库设计

系统使用MySQL数据库，主要包含以下数据表：
- 用户表（users）：存储用户基本信息和权限
- 失物表（lost_items）：存储失物信息
- 招领表（found_items）：存储招领信息

## 部署

1. 确保已安装Tomcat服务器和MySQL数据库
2. 创建数据库并执行`src/main/resources/database.sql`脚本
3. 修改`src/main/java/com/lostandfound/utils/DatabaseConnection.java`中的数据库连接信息
4. 使用Maven构建项目：`mvn clean package`
5. 将生成的WAR文件部署到Tomcat服务器

## 开发

使用IntelliJ IDEA或其他支持Maven的IDE打开项目即可开始开发。

## 批量删除功能

管理员可以通过以下方式批量删除失物和招领信息：

1. 通过Web界面：访问管理员面板的批量删除页面
2. 通过命令行脚本：使用相应的脚本
3. 通过Shell脚本：使用相应的脚本（Linux/Mac）
4. 通过批处理脚本：使用相应的脚本（Windows）

## 测试

项目包含基本的JUnit测试用例，可以通过`mvn test`命令运行测试。

## 贡献

欢迎提交Issue和Pull Request来改进本项目。

## 许可证

本项目采用MIT许可证。