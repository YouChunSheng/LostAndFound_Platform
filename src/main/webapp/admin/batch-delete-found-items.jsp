<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>批量删除招领信息 - 失物招领平台</title>
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
                        <a class="nav-link" href="<%=request.getContextPath()%>/found-items">招领信息</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<%=request.getContextPath()%>/admin/">管理员</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item">
                                <a class="nav-link" href="#">欢迎, ${sessionScope.user.username}</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="<%=request.getContextPath()%>/logout">退出</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="<%=request.getContextPath()%>/login.jsp">登录</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <div class="row justify-content-center mt-5">
        <div class="col-md-10">
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">批量删除招领信息</h3>
                </div>
                <div class="card-body">
                    <form id="batchDeleteForm">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>
                                            <input type="checkbox" id="selectAll">
                                        </th>
                                        <th>标题</th>
                                        <th>分类</th>
                                        <th>拾取地点</th>
                                        <th>拾取时间</th>
                                        <th>状态</th>
                                        <th>发布者</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${foundItems}">
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="ids" value="${item.id}">
                                            </td>
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
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="d-grid">
                            <button type="button" class="btn btn-danger" id="deleteButton">批量删除</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // 全选/取消全选功能
    document.getElementById('selectAll').addEventListener('change', function() {
        const checkboxes = document.querySelectorAll('input[name="ids"]');
        checkboxes.forEach(checkbox => {
            checkbox.checked = this.checked;
        });
    });

    // 批量删除功能
    document.getElementById('deleteButton').addEventListener('click', function() {
        const selectedIds = document.querySelectorAll('input[name="ids"]:checked');
        
        if (selectedIds.length === 0) {
            alert('请至少选择一项进行删除');
            return;
        }
        
        if (!confirm(`确定要删除选中的 ${selectedIds.length} 项招领信息吗？此操作不可恢复！`)) {
            return;
        }
        
        const ids = Array.from(selectedIds).map(cb => cb.value);
        
        fetch('<%=request.getContextPath()%>/admin/batch-delete-found-items', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'ids=' + ids.join('&ids=')
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                // 删除成功后刷新页面
                location.reload();
            } else {
                alert('删除失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('删除过程中发生错误');
        });
    });
</script>
</body>
</html>