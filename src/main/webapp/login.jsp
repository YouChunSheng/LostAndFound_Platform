<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户登录 - 失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">失物招领平台</a>
        </div>
    </nav>

    <div class="row justify-content-center mt-5">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">用户登录</h3>
                </div>
                <div class="card-body">
                    <% if (request.getParameter("error") != null) { %>
                        <div class="alert alert-danger" role="alert">
                            <% if (request.getParameter("error").equals("invalid_credentials")) { %>
                                用户名或密码错误
                            <% } else if (request.getParameter("error").equals("database_error")) { %>
                                数据库错误，请稍后再试
                            <% } %>
                        </div>
                    <% } %>

                    <form action="<%=request.getContextPath()%>/login" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">用户名</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">密码</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">登录</button>
                        </div>
                    </form>
                    <div class="mt-3 text-center">
                        <p>还没有账号？<a href="<%=request.getContextPath()%>/register.jsp">立即注册</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>