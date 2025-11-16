# 失物招领平台 (Lost and Found Platform)

一个基于Java Web技术的失物招领平台，采用Servlet和JSP实现，使用MVC架构模式。

## 技术栈

- Java Servlet/JSP
- MySQL数据库
- Bootstrap前端框架
- Maven构建工具

## 功能特性

- 用户注册和登录
- 失物信息发布和浏览
- 招领信息发布和浏览
- 管理员后台管理
- 数据库搜索和筛选功能

## 部署

1. 确保已安装Tomcat服务器和MySQL数据库
2. 创建数据库并执行`src/main/resources/database.sql`脚本
3. 修改`src/main/java/com/lostandfound/utils/DatabaseConnection.java`中的数据库连接信息
4. 使用Maven构建项目：`mvn clean package`
5. 将生成的WAR文件部署到Tomcat服务器

## 开发

使用IntelliJ IDEA或其他支持Maven的IDE打开项目即可开始开发。

## 测试

项目包含基本的JUnit测试用例，可以通过`mvn test`命令运行测试。