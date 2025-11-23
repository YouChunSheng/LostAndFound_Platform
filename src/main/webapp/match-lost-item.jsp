<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>申请匹配失物 - 失物招领平台</title>
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

    <div class="row justify-content-center mt-5">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">申请匹配失物</h3>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>
                    
                    <div class="mb-4">
                        <h5>失物信息</h5>
                        <div class="border p-3 rounded">
                            <h6>${lostItem.title}</h6>
                            <p>${lostItem.description}</p>
                            <ul class="list-unstyled">
                                <li><strong>分类:</strong> ${lostItem.category}</li>
                                <li><strong>丢失地点:</strong> ${lostItem.lostLocation}</li>
                                <li><strong>丢失时间:</strong> ${lostItem.lostTime}</li>
                                <li><strong>联系方式:</strong> ${lostItem.contactInfo}</li>
                            </ul>
                            <c:if test="${not empty lostItem.imageUrl}">
                                <img src="${lostItem.imageUrl}" alt="物品图片" style="max-width: 200px; max-height: 200px;">
                            </c:if>
                        </div>
                    </div>
                    
                    <form id="matchForm">
                        <input type="hidden" name="action" value="submitMatch">
                        <input type="hidden" name="lostItemId" value="${lostItem.id}">
                        
                        <div class="mb-3">
                            <label for="message" class="form-label">留言信息</label>
                            <textarea class="form-control" id="message" name="message" rows="4" 
                                      placeholder="请描述您捡到的物品信息，以及如何联系您..." required></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label for="contactInfo" class="form-label">联系方式</label>
                            <input type="text" class="form-control" id="contactInfo" name="contactInfo" 
                                   value="${sessionScope.user != null ? sessionScope.user.email : ''}" required>
                        </div>
                        
                        <div class="d-grid">
                            <button type="button" class="btn btn-success" onclick="submitMatchForm()">提交匹配申请</button>
                            <a href="<%=request.getContextPath()%>/lost-items" class="btn btn-secondary mt-2">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function submitMatchForm() {
        const form = document.getElementById('matchForm');
        const formData = new FormData(form);
        
        // Check if all required fields are filled
        const message = formData.get('message');
        const contactInfo = formData.get('contactInfo');
        
        if (!message || !contactInfo) {
            alert('请填写所有必填字段');
            return;
        }
        
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
</script>
</body>
</html>