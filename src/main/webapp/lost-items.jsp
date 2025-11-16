<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>失物信息 - 失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="index.jsp">失物招领平台</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="index.jsp">首页</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="lost-items">失物信息</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="found-items">招领信息</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item">
                                <a class="nav-link" href="#">欢迎, ${sessionScope.user.username}</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="logout">退出</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="login.jsp">登录</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="register.jsp">注册</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <div class="d-flex justify-content-between align-items-center mt-4">
        <h2>失物信息</h2>
        <c:if test="${sessionScope.user != null}">
            <a href="lost-items/new" class="btn btn-primary">发布失物信息</a>
        </c:if>
    </div>

    <!-- 搜索和筛选表单 -->
    <div class="card mt-4">
        <div class="card-body">
            <form class="row g-3" method="get" action="lost-items">
                <div class="col-md-4">
                    <label for="keyword" class="form-label">关键词</label>
                    <input type="text" class="form-control" id="keyword" name="keyword" placeholder="标题或描述" value="${param.keyword}">
                </div>
                <div class="col-md-3">
                    <label for="category" class="form-label">分类</label>
                    <select class="form-select" id="category" name="category">
                        <option value="">全部分类</option>
                        <option value="证件" ${param.category == '证件' ? 'selected' : ''}>证件</option>
                        <option value="电子设备" ${param.category == '电子设备' ? 'selected' : ''}>电子设备</option>
                        <option value="衣物" ${param.category == '衣物' ? 'selected' : ''}>衣物</option>
                        <option value="书籍" ${param.category == '书籍' ? 'selected' : ''}>书籍</option>
                        <option value="其他" ${param.category == '其他' ? 'selected' : ''}>其他</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="location" class="form-label">丢失地点</label>
                    <input type="text" class="form-control" id="location" name="location" placeholder="地点" value="${param.location}">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">搜索</button>
                </div>
            </form>
        </div>
    </div>

    <c:if test="${empty lostItems}">
        <div class="alert alert-info mt-4" role="alert">
            暂无失物信息。
        </div>
    </c:if>

    <div class="row mt-4">
        <c:forEach var="item" items="${lostItems}">
            <div class="col-md-4 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <h5 class="card-title">${item.title}</h5>
                        <p class="card-text">${item.description}</p>
                        <ul class="list-unstyled">
                            <li><strong>分类:</strong> ${item.category}</li>
                            <li><strong>丢失地点:</strong> ${item.lostLocation}</li>
                            <li><strong>丢失时间:</strong> ${item.lostTime}</li>
                            <li><strong>联系方式:</strong> ${item.contactInfo}</li>
                        </ul>
                    </div>
                    <div class="card-footer">
                        <small class="text-muted">发布于 ${item.createdAt}</small>
                        <a href="lost-items/detail?id=${item.id}" class="btn btn-sm btn-outline-primary float-end">查看详情</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    
    <!-- 分页 -->
    <nav aria-label="Page navigation" class="mt-4">
        <ul class="pagination justify-content-center">
            <li class="page-item disabled">
                <a class="page-link" href="#" tabindex="-1">上一页</a>
            </li>
            <li class="page-item active"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item">
                <a class="page-link" href="#">下一页</a>
            </li>
        </ul>
    </nav>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>