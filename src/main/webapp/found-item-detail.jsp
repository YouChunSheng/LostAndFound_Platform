<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${foundItem.title} - 招领信息详情 - 失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">失物招领平台</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/index.jsp">首页</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/lost-items">失物信息</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<%=request.getContextPath()%>/found-items">招领信息</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item">
                                <a class="nav-link" href="#">欢迎, ${sessionScope.user.username}</a>
                            </li>
                            <c:if test="${sessionScope.user.role == 'admin'}">
                                <li class="nav-item">
                                    <a class="nav-link" href="<%=request.getContextPath()%>/admin/">管理员</a>
                                </li>
                            </c:if>
                            <li class="nav-item">
                                <a class="nav-link" href="<%=request.getContextPath()%>/logout">退出</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">登录</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="<%=request.getContextPath()%>/register.jsp">注册</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <div class="row mt-4">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h2>${foundItem.title}</h2>
                </div>
                <div class="card-body">
                    <c:if test="${foundItem.imageUrl != null && !empty foundItem.imageUrl}">
                        <div class="text-center mb-3">
                            <img src="${foundItem.imageUrl}" alt="物品图片" class="img-fluid" style="max-height: 300px;">
                        </div>
                    </c:if>
                    <p class="card-text">${foundItem.description}</p>
                    
                    <h5>详细信息</h5>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item"><strong>分类:</strong> ${foundItem.category}</li>
                        <li class="list-group-item"><strong>拾取地点:</strong> ${foundItem.foundLocation}</li>
                        <li class="list-group-item"><strong>拾取时间:</strong> ${foundItem.foundTime}</li>
                        <li class="list-group-item"><strong>联系方式:</strong> ${foundItem.contactInfo}</li>
                        <li class="list-group-item"><strong>状态:</strong> 
                            <c:choose>
                                <c:when test="${foundItem.status == 'unclaimed'}">未认领</c:when>
                                <c:when test="${foundItem.status == 'claimed'}">已认领</c:when>
                                <c:otherwise>未知</c:otherwise>
                            </c:choose>
                        </li>
                        <li class="list-group-item"><strong>发布时间:</strong> ${foundItem.createdAt}</li>
                    </ul>
                </div>
            </div>
        </div>
        
        <div class="col-md-4">
            <div class="card">
                <div class="card-header">
                    <h5>操作</h5>
                </div>
                <div class="card-body">
                    <c:if test="${sessionScope.user != null && sessionScope.user.id == foundItem.userId}">
                        <a href="#" class="btn btn-primary w-100 mb-2">编辑信息</a>
                        <a href="#" class="btn btn-danger w-100">删除信息</a>
                    </c:if>
                    <c:if test="${sessionScope.user != null && sessionScope.user.id != foundItem.userId}">
                        <a href="#" class="btn btn-success w-100">这是我的物品</a>
                    </c:if>
                    <!-- 管理员额外操作 -->
                    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'admin'}">
                        <div class="mt-3">
                            <h6>管理员操作</h6>
                            <a href="#" class="btn btn-warning w-100 mb-2">编辑信息(管理员)</a>
                            <a href="#" class="btn btn-danger w-100">删除信息(管理员)</a>
                        </div>
                    </c:if>
                </div>
            </div>
            
            <div class="card mt-3">
                <div class="card-header">
                    <h5>联系拾主</h5>
                </div>
                <div class="card-body">
                    <p>请联系拾主: ${foundItem.contactInfo}</p>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>