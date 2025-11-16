<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">失物招领平台</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" href="index.jsp">首页</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="lost-items">失物信息</a>
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
                            <c:if test="${sessionScope.user.role == 'admin'}">
                                <li class="nav-item">
                                    <a class="nav-link" href="admin/">管理员</a>
                                </li>
                            </c:if>
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

    <div class="jumbotron mt-4">
        <h1 class="display-4">欢迎来到失物招领平台</h1>
        <p class="lead">在这里您可以发布失物信息或寻找您丢失的物品。</p>
        <hr class="my-4">
        <div class="row">
            <div class="col-md-6">
                <h3>最新失物信息</h3>
                <p>查看最新的失物信息，寻找您可能丢失的物品。</p>
                <a class="btn btn-primary btn-lg" href="lost-items" role="button">查看失物信息</a>
            </div>
            <div class="col-md-6">
                <h3>最新招领信息</h3>
                <p>查看最新的招领信息，也许有人捡到了您的物品。</p>
                <a class="btn btn-success btn-lg" href="found-items" role="button">查看招领信息</a>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
