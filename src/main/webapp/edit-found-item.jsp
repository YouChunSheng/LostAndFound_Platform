<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>编辑招领信息 - 失物招领平台</title>
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

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger mt-3">${errorMessage}</div>
    </c:if>
    
    <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-danger mt-3">
            ${param.error}
        </div>
    <% } %>

    <div class="row justify-content-center mt-5">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">编辑招领信息</h3>
                </div>
                <div class="card-body">
                    <form id="editFoundItemForm" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="${foundItem.id}">
                        <input type="hidden" name="currentImage" value="${foundItem.imageUrl}">
                        
                        <div class="mb-3">
                            <label for="title" class="form-label">标题</label>
                            <input type="text" class="form-control" id="title" name="title" 
                                   value="${foundItem.title}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">描述</label>
                            <textarea class="form-control" id="description" name="description" rows="4" 
                                      required>${foundItem.description}</textarea>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="category" class="form-label">分类</label>
                                <select class="form-select" id="category" name="category" required>
                                    <option value="">请选择分类</option>
                                    <option value="证件" ${foundItem.category == '证件' ? 'selected' : ''}>证件</option>
                                    <option value="电子设备" ${foundItem.category == '电子设备' ? 'selected' : ''}>电子设备</option>
                                    <option value="衣物" ${foundItem.category == '衣物' ? 'selected' : ''}>衣物</option>
                                    <option value="书籍" ${foundItem.category == '书籍' ? 'selected' : ''}>书籍</option>
                                    <option value="其他" ${foundItem.category == '其他' ? 'selected' : ''}>其他</option>
                                </select>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="foundLocation" class="form-label">拾取地点</label>
                                <input type="text" class="form-control" id="foundLocation" name="foundLocation" 
                                       value="${foundItem.foundLocation}" list="locations" required>
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
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="foundTime" class="form-label">拾取时间</label>
                                <input type="datetime-local" class="form-control" id="foundTime" name="foundTime" 
                                       value="${foundItem.foundTime}" required>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="contactInfo" class="form-label">联系方式</label>
                                <input type="text" class="form-control" id="contactInfo" name="contactInfo" 
                                       value="${foundItem.contactInfo}" required>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="image" class="form-label">物品图片</label>
                            <input type="file" class="form-control" id="image" name="image" accept="image/*">
                            <c:if test="${not empty foundItem.imageUrl}">
                                <div class="mt-2">
                                    <p>当前图片:</p>
                                    <img src="<%=request.getContextPath()%>/${foundItem.imageUrl}" alt="当前图片" style="max-width: 200px; max-height: 200px;">
                                </div>
                            </c:if>
                        </div>
                        
                        <div class="d-grid">
                            <button type="button" class="btn btn-primary" onclick="submitEditFoundItemForm()">更新信息</button>
                            <a href="<%=request.getContextPath()%>/found-items/detail?id=${foundItem.id}" class="btn btn-secondary mt-2">取消</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function submitEditFoundItemForm() {
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
        
        fetch('<%=request.getContextPath()%>/found-items', {
            method: 'POST',
            body: formData
        }).then(response => {
            console.log('Response status:', response.status);
            // 更新成功后跳转到招领信息列表页面
            window.location.href = '<%=request.getContextPath()%>/found-items?message=更新成功';
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