<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>发布失物信息 - 失物招领平台</title>
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

    <div class="row justify-content-center mt-5">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">发布失物信息</h3>
                </div>
                <div class="card-body">
                    <% if (request.getParameter("error") != null) { %>
                        <div class="alert alert-danger" role="alert">
                            <% if (request.getParameter("error").equals("creation_failed")) { %>
                                信息发布失败，请稍后再试
                            <% } else if (request.getParameter("error").equals("database_error")) { %>
                                数据库错误，请稍后再试
                            <% } %>
                        </div>
                    <% } %>

                    <form action="lost-items" method="post">
                        <input type="hidden" name="action" value="create">
                        <div class="mb-3">
                            <label for="title" class="form-label">标题</label>
                            <input type="text" class="form-control" id="title" name="title" required>
                        </div>
                        <div class="mb-3">
                            <label for="description" class="form-label">描述</label>
                            <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="category" class="form-label">分类</label>
                            <select class="form-control" id="category" name="category" required>
                                <option value="">请选择分类</option>
                                <option value="证件">证件</option>
                                <option value="电子设备">电子设备</option>
                                <option value="衣物">衣物</option>
                                <option value="书籍">书籍</option>
                                <option value="其他">其他</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="lostLocation" class="form-label">丢失地点</label>
                            <input type="text" class="form-control" id="lostLocation" name="lostLocation" required>
                        </div>
                        <div class="mb-3">
                            <label for="lostTime" class="form-label">丢失时间</label>
                            <input type="datetime-local" class="form-control" id="lostTime" name="lostTime" required>
                        </div>
                        <div class="mb-3">
                            <label for="contactInfo" class="form-label">联系方式</label>
                            <input type="text" class="form-control" id="contactInfo" name="contactInfo" required>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">发布</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>