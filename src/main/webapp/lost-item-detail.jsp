<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${lostItem.title} - 失物信息详情 - 失物招领平台</title>
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

    <c:if test="${not empty param.message}">
        <div class="alert alert-success alert-dismissible fade show mt-3" role="alert">
            ${param.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
            ${param.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row mt-4">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h2>${lostItem.title}</h2>
                </div>
                <div class="card-body">
                    <c:if test="${lostItem.imageUrl != null && !empty lostItem.imageUrl}">
                        <div class="text-center mb-3">
                            <img src="<%=request.getContextPath()%>/${lostItem.imageUrl}" alt="物品图片" class="img-fluid" style="max-height: 300px;">
                        </div>
                    </c:if>
                    <p class="card-text">${lostItem.description}</p>
                    
                    <h5>详细信息</h5>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item"><strong>分类:</strong> ${lostItem.category}</li>
                        <li class="list-group-item"><strong>丢失地点:</strong> ${lostItem.lostLocation}</li>
                        <li class="list-group-item"><strong>丢失时间:</strong> ${lostItem.lostTime}</li>
                        <li class="list-group-item"><strong>联系方式:</strong> ${lostItem.contactInfo}</li>
                        <li class="list-group-item"><strong>状态:</strong> 
                            <c:choose>
                                <c:when test="${lostItem.status == 'unclaimed'}">
                                    <span class="badge bg-warning">未认领</span>
                                </c:when>
                                <c:when test="${lostItem.status == 'claimed'}">
                                    <span class="badge bg-success">已认领</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">未知</span>
                                </c:otherwise>
                            </c:choose>
                        </li>
                        <li class="list-group-item"><strong>发布时间:</strong> ${lostItem.createdAt}</li>
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
                    <c:if test="${sessionScope.user != null && sessionScope.user.id == lostItem.userId}">
                        <a href="<%=request.getContextPath()%>/lost-items/detail?action=edit&id=${lostItem.id}" class="btn btn-primary w-100 mb-2">编辑信息</a>
                        <form id="userDeleteForm" action="<%=request.getContextPath()%>/lost-items/detail" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${lostItem.id}">
                            <button type="button" class="btn btn-danger w-100" onclick="submitUserDeleteForm()">删除信息</button>
                        </form>
                    </c:if>
                    <c:if test="${sessionScope.user != null && sessionScope.user.id != lostItem.userId && lostItem.status == 'unclaimed'}">
                        <form id="claimForm" style="display:inline;">
                            <input type="hidden" name="action" value="claim">
                            <input type="hidden" name="id" value="${lostItem.id}">
                            <button type="button" class="btn btn-info w-100" onclick="submitClaimForm()">认领物品</button>
                        </form>
                    </c:if>
                    <c:if test="${sessionScope.user != null && sessionScope.user.id != lostItem.userId && lostItem.status == 'claimed'}">
                        <button class="btn btn-secondary w-100" disabled>物品已被认领</button>
                        <form id="revokeClaimForm" style="display:inline;" class="mt-2">
                            <input type="hidden" name="action" value="revokeClaim">
                            <input type="hidden" name="id" value="${lostItem.id}">
                            <button type="button" class="btn btn-warning w-100" onclick="submitRevokeClaimForm()">撤销认领</button>
                        </form>
                    </c:if>
                    <!-- 管理员额外操作 -->
                    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'admin'}">
                        <div class="mt-3">
                            <h6>管理员操作</h6>
                            <a href="<%=request.getContextPath()%>/lost-items/detail?action=edit&id=${lostItem.id}" class="btn btn-warning w-100 mb-2">编辑信息(管理员)</a>
                        	<form id="adminDeleteForm" action="<%=request.getContextPath()%>/lost-items" method="post" style="display:inline;">
                        	    <input type="hidden" name="action" value="delete">
                        	    <input type="hidden" name="id" value="${lostItem.id}">
                        	    <button type="button" class="btn btn-danger w-100" onclick="submitAdminDeleteForm()">删除信息(管理员)</button>
                        	</form>
                        </div>
                    </c:if>
                </div>
            </div>
            
            <div class="card mt-3">
                <div class="card-header">
                    <h5>联系失主</h5>
                </div>
                <div class="card-body">
                    <p>请联系失主: ${lostItem.contactInfo}</p>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function submitClaimForm() {
        if (confirm('确定要认领这个物品吗？')) {
            const form = document.getElementById('claimForm');
            const formData = new FormData(form);
            
            fetch('<%=request.getContextPath()%>/lost-items', {
                method: 'POST',
                body: new URLSearchParams(formData)
            }).then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    window.location.reload();
                }
            }).catch(error => {
                console.error('Error:', error);
                window.location.href = '<%=request.getContextPath()%>/lost-items';
            });
        }
    }
    
    function submitRevokeClaimForm() {
        if (confirm('确定要撤销认领这个物品吗？')) {
            const form = document.getElementById('revokeClaimForm');
            const formData = new FormData(form);
            
            fetch('<%=request.getContextPath()%>/lost-items', {
                method: 'POST',
                body: new URLSearchParams(formData)
            }).then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    window.location.reload();
                }
            }).catch(error => {
                console.error('Error:', error);
                window.location.href = '<%=request.getContextPath()%>/lost-items';
            });
        }
    }
    
    function submitAdminDeleteForm() {
        if (confirm('确定要删除这条失物信息吗？')) {
            const form = document.getElementById('adminDeleteForm');
            const formData = new FormData(form);
            
            fetch('<%=request.getContextPath()%>/lost-items', {
                method: 'POST',
                body: new URLSearchParams(formData)
            }).then(response => {
                // 正确处理服务器返回的重定向
                if (response.redirected) {
                    window.location.href = response.url;
                } else {
                    // 如果没有重定向，则手动跳转到失物列表页面
                    window.location.href = '<%=request.getContextPath()%>/lost-items?message=删除成功';
                }
            }).catch(error => {
                console.error('Error:', error);
                window.location.href = '<%=request.getContextPath()%>/lost-items?error=删除失败';
            });
        }
    }
    
    function submitUserDeleteForm() {
        if (confirm('确定要删除这条失物信息吗？')) {
            const form = document.getElementById('userDeleteForm');
            const formData = new FormData(form);
            
            fetch('<%=request.getContextPath()%>/lost-items/detail', {
                method: 'POST',
                body: new URLSearchParams(formData)
            }).then(response => {
                // 删除成功后跳转到失物信息列表页面
                window.location.href = '<%=request.getContextPath()%>/lost-items?message=删除成功';
            }).catch(error => {
                console.error('Error:', error);
                window.location.href = '<%=request.getContextPath()%>/lost-items?error=删除失败';
            });
        }
    }
</script>
</body>
</html>