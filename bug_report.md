# 失物招领平台BUG报告

## 概述

在对失物招领平台进行测试和代码审查过程中，我们发现了一些潜在的问题和可以改进的地方。这些问题可能会影响系统的稳定性、用户体验和安全性。

## 发现的问题

### 1. 日期时间解析问题
**问题描述**：在 [FoundItemServlet.java](file:///D:/Course/Software_Engineering/LostAndFound_Platform/src/main/java/com/lostandfound/controller/FoundItemServlet.java) 和 [LostItemServlet.java](file:///D:/Course/Software_Engineering/LostAndFound_Platform/src/main/java/com/lostandfound/controller/LostItemServlet.java) 中，代码直接使用 `LocalDateTime.parse()` 解析日期时间字符串，但前端传递的是 `datetime-local` 类型的值，格式为 `YYYY-MM-DDTHH:mm`，这可能会导致解析错误。

**影响**：可能导致用户提交的失物/拾物信息无法正确保存，出现500服务器错误。

**修复建议**：
- 添加更健壮的日期时间解析逻辑
- 增加异常处理机制
- 提供用户友好的错误提示

### 2. 文件上传路径问题
**问题描述**：在 [FileUploadUtil.java](file:///D:/Course/Software_Engineering/LostAndFound_Platform/src/main/java/com/lostandfound/utils/FileUploadUtil.java) 中，文件保存时可能因为父目录不存在而失败。

**影响**：用户上传的物品图片可能无法正确保存，导致信息不完整。

**修复建议**：
- 在保存文件前确保父目录存在
- 增加更完善的错误处理和日志记录

### 3. 视图切换功能问题
**问题描述**：在 [lost-items.jsp](file:///D:/Course/Software_Engineering/LostAndFound_Platform/src/main/webapp/lost-items.jsp) 中，视图切换功能虽然实现了，但刷新页面后会恢复到默认视图，用户体验不佳。

**影响**：用户每次刷新页面都需要重新选择偏好的视图模式，影响使用体验。

**修复建议**：
- 使用 localStorage 保存用户视图偏好设置
- 页面加载时恢复用户上次选择的视图

### 4. 异常处理不完整
**问题描述**：在 Servlet 的 doPost 方法中，虽然捕获了 SQLException 和 Exception，但在 catch 块中只是简单地重定向到错误页面，没有记录详细的错误信息。

**影响**：系统出现错误时难以调试和定位问题，增加维护成本。

**修复建议**：
- 添加详细的错误日志记录
- 提供更友好的错误页面和用户提示

### 5. SQL 注入风险
**问题描述**：虽然项目使用了 PreparedStatement，但在 [LostItemDAO.java](file:///D:/Course/Software_Engineering/LostAndFound_Platform/src/main/java/com/lostandfound/dao/LostItemDAO.java) 的搜索功能中，SQL 查询是动态构建的，虽然目前看起来是安全的，但随着功能增加可能会引入风险。

**影响**：随着功能扩展，可能存在SQL注入安全风险。

**修复建议**：
- 仔细审查所有动态SQL构建代码
- 确保所有用户输入都经过适当转义和验证
- 考虑使用更安全的查询构建方式

## 已修复的问题

以下问题已在本次修复中解决：

1. 日期时间解析问题 - 添加了更健壮的解析逻辑和异常处理
2. 文件上传路径问题 - 确保父目录存在后再保存文件
3. 视图切换功能问题 - 使用 localStorage 保存用户偏好设置

## 建议

1. 建立更完善的错误日志系统，便于问题追踪和系统维护
2. 增加更多的单元测试和集成测试，确保系统稳定性
3. 定期进行代码审查，及时发现和修复潜在问题
4. 考虑添加前端表单验证，提升用户体验