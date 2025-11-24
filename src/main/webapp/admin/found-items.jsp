<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>招领信息管理 - 失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%=request.getContextPath()%>/admin/">管理员后台</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/admin/">仪表盘</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/admin/users">用户管理</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/admin/lost-items">失物管理</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<%=request.getContextPath()%>/admin/found-items">招领管理</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="batchDeleteDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            批量删除
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="batchDeleteDropdown">
                            <li><a class="dropdown-item" href="<%=request.getContextPath()%>/admin/batch-delete-lost-items.jsp">批量删除失物信息</a></li>
                            <li><a class="dropdown-item" href="<%=request.getContextPath()%>/admin/batch-delete-found-items.jsp">批量删除招领信息</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/index.jsp">返回首页</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%=request.getContextPath()%>/logout">退出</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="row mt-4">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    <h3>招领信息管理</h3>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>标题</th>
                                    <th>分类</th>
                                    <th>拾取地点</th>
                                    <th>拾取时间</th>
                                    <th>状态</th>
                                    <th>发布者ID</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${foundItems}">
                                    <tr>
                                        <td>${item.id}</td>
                                        <td>${item.title}</td>
                                        <td>${item.category}</td>
                                        <td>${item.foundLocation}</td>
                                        <td>${item.foundTime}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.status == 'claimed'}">
                                                    <span class="badge bg-success">已认领</span>
                                                </c:when>
                                                <c:when test="${item.status == 'unclaimed'}">
                                                    <span class="badge bg-warning">未认领</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${item.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${item.userId}</td>
                                        <td>
                                            <a href="<%=request.getContextPath()%>/found-items/detail?id=${item.id}" class="btn btn-sm btn-primary">查看</a>
                                            <c:if test="${item.status == 'claimed'}">
                                                <form action="<%=request.getContextPath()%>/found-items" method="post" style="display: inline;">
                                                    <input type="hidden" name="action" value="revokeClaim">
                                                    <input type="hidden" name="id" value="${item.id}">
                                                    <button type="submit" class="btn btn-sm btn-warning" 
                                                            onclick="return confirm('确定要撤销这条招领信息的认领状态吗？')">撤销认领</button>
                                                </form>
                                            </c:if>
                                            <form action="<%=request.getContextPath()%>/found-items" method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${item.id}">
                                                <button type="submit" class="btn btn-sm btn-danger" 
                                                        onclick="return confirm('确定要删除这条招领信息吗？')">删除</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>