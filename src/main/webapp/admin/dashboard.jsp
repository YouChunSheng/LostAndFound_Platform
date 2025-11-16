<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>管理后台 - 失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="admin/">管理员后台</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="admin/">仪表盘</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="admin/users">用户管理</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="admin/lost-items">失物管理</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="admin/found-items">招领管理</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="../index.jsp">返回首页</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="../logout">退出</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="row mt-4">
        <div class="col-md-4">
            <div class="card text-white bg-primary mb-3">
                <div class="card-header">用户总数</div>
                <div class="card-body">
                    <h5 class="card-title">${userCount} 人</h5>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-success mb-3">
                <div class="card-header">失物信息</div>
                <div class="card-body">
                    <h5 class="card-title">${lostItemCount} 条</h5>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-info mb-3">
                <div class="card-header">招领信息</div>
                <div class="card-body">
                    <h5 class="card-title">${foundItemCount} 条</h5>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>