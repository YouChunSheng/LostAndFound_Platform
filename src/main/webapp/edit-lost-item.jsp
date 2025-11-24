<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>编辑失物信息 - 失物招领平台</title>
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
                        <a class="nav-link active" href="<%=request.getContextPath()%>/lost-items">失物信息</a>
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
                    <h3 class="text-center">编辑失物信息</h3>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>
                    
                    <form action="<%=request.getContextPath()%>/lost-items" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="${lostItem.id}">
                        
                        <div class="mb-3">
                            <label for="title" class="form-label">标题</label>
                            <input type="text" class="form-control" id="title" name="title" value="${lostItem.title}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">描述</label>
                            <textarea class="form-control" id="description" name="description" rows="3" required>${lostItem.description}</textarea>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="category" class="form-label">分类</label>
                                    <select class="form-select" id="category" name="category" required>
                                        <option value="证件" ${lostItem.category == '证件' ? 'selected' : ''}>证件</option>
                                        <option value="电子设备" ${lostItem.category == '电子设备' ? 'selected' : ''}>电子设备</option>
                                        <option value="衣物" ${lostItem.category == '衣物' ? 'selected' : ''}>衣物</option>
                                        <option value="书籍" ${lostItem.category == '书籍' ? 'selected' : ''}>书籍</option>
                                        <option value="其他" ${lostItem.category == '其他' ? 'selected' : ''}>其他</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="lostLocation" class="form-label">丢失地点</label>
                                    <input type="text" class="form-control" id="lostLocation" name="lostLocation" value="${lostItem.lostLocation}" list="locations" required>
                                    <datalist id="locations">
                                        <option value="教学楼">
                                        <option value="实验楼">
                                        <option value="图书馆">
                                        <option value="宿舍">
                                        <option value="食堂">
                                        <option value="体育馆">
                                        <option value="校医院">
                                        <option value="行政楼">
                                        <option value="超市">
                                        <option value="校门">
                                        <option value="其他">
                                    </datalist>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="lostTime" class="form-label">丢失时间</label>
                                    <input type="datetime-local" class="form-control" id="lostTime" name="lostTime" 
                                           value="${lostItem.lostTime.toString().replace(' ', 'T').substring(0, 16)}" required>
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="contactInfo" class="form-label">联系方式</label>
                                    <input type="text" class="form-control" id="contactInfo" name="contactInfo" value="${lostItem.contactInfo}" required>
                                </div>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="image" class="form-label">图片</label>
                            <input type="file" class="form-control" id="image" name="image" accept="image/*">
                            <c:if test="${not empty lostItem.imageUrl}">
                                <div class="mt-2">
                                    <p>当前图片:</p>
                                    <img src="<%=request.getContextPath()%>/${lostItem.imageUrl}" alt="当前图片" style="max-width: 200px; max-height: 200px;">
                                    <input type="hidden" name="currentImage" value="${lostItem.imageUrl}">
                                </div>
                            </c:if>
                        </div>
                        
                        <div class="d-grid">
                            <button type="button" class="btn btn-primary" onclick="submitEditLostItemForm()">更新信息</button>
                            <a href="<%=request.getContextPath()%>/lost-items/detail?id=${lostItem.id}" class="btn btn-secondary mt-2">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function submitEditLostItemForm() {
        const form = document.querySelector('form');
        
        // 前端验证
        if (!form.checkValidity()) {
            // 如果表单验证失败，显示浏览器默认的验证提示
            form.reportValidity();
            return;
        }
        
        const formData = new FormData(form);
        
        // 禁用提交按钮防止重复提交
        const submitButton = form.querySelector('button[type="button"]');
        submitButton.disabled = true;
        submitButton.textContent = '更新中...';
        
        fetch('<%=request.getContextPath()%>/lost-items', {
            method: 'POST',
            body: formData
        }).then(response => {
            console.log('Response status:', response.status);
            // 更新成功后跳转到失物信息列表页面
            window.location.href = '<%=request.getContextPath()%>/lost-items?message=更新成功';
        }).catch(error => {
            console.error('Error:', error);
            alert('提交过程中发生错误，请稍后再试');
            // 重新启用提交按钮
            submitButton.disabled = false;
            submitButton.textContent = '更新信息';
        });
    }
</script>
</body>
</html>