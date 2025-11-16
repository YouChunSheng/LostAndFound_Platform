<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>失物信息 - 失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            font-family: "Microsoft YaHei", Arial, sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
            min-height: 100vh;
        }
        
        .navbar {
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .hero-section {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
            border-radius: 0 0 1rem 1rem;
        }
        
        .card {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            height: 100%;
            border: none;
            border-radius: 0.5rem;
            overflow: hidden;
        }
        
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }
        
        .search-card {
            border-radius: 0.5rem;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            border: none;
        }
        
        .btn-primary:hover {
            background: linear-gradient(135deg, #2575fc 0%, #6a11cb 100%);
        }
        
        .page-item.active .page-link {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            border-color: #2575fc;
        }
        
        /* 瀑布流布局样式 */
        .masonry-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            grid-gap: 20px;
            margin-top: 20px;
        }
        
        .masonry-item {
            break-inside: avoid;
            margin-bottom: 20px;
        }
        
        .view-toggle {
            margin-bottom: 20px;
            text-align: right;
        }
        
        .view-btn {
            margin-left: 10px;
        }
        
        .masonry-card {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border: none;
            border-radius: 0.5rem;
            overflow: hidden;
        }
        
        .masonry-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.15);
        }
        
        .masonry-card img {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }
    </style>
</head>
<body>
    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
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

    <!-- 页面标题横幅 -->
    <section class="hero-section">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center">
                <h1>失物信息</h1>
                <c:if test="${sessionScope.user != null}">
                    <a href="lost-items/new" class="btn btn-light">发布失物信息</a>
                </c:if>
            </div>
        </div>
    </section>

    <div class="container">
        <!-- 搜索和筛选表单 -->
        <div class="card search-card mt-4">
            <div class="card-body">
                <form class="row g-3" method="get" action="lost-items">
                    <div class="col-md-3">
                        <label for="keyword" class="form-label">关键词</label>
                        <input type="text" class="form-control" id="keyword" name="keyword" placeholder="标题或描述" value="${param.keyword}" list="keywords">
                        <datalist id="keywords">
                            <option value="手机">
                            <option value="钱包">
                            <option value="钥匙">
                            <option value="身份证">
                            <option value="学生证">
                            <option value="银行卡">
                            <option value="耳机">
                            <option value="U盘">
                            <option value="笔记本电脑">
                            <option value="平板电脑">
                            <option value="书本">
                            <option value="眼镜">
                            <option value="手表">
                            <option value="外套">
                            <option value="雨伞">
                        </datalist>
                    </div>
                    <div class="col-md-2">
                        <label for="category" class="form-label">分类</label>
                        <select class="form-select" id="category" name="category">
                            <option value="">全部分类</option>
                            <option value="证件" ${param.category == '证件' ? 'selected' : ''}>证件</option>
                            <option value="电子设备" ${param.category == '电子设备' ? 'selected' : ''}>电子设备</option>
                            <option value="衣物" ${param.category == '衣物' ? 'selected' : ''}>衣物</option>
                            <option value="书籍" ${param.category == '书籍' ? 'selected' : ''}>书籍</option>
                            <option value="其他" ${param.category == '其他' ? 'selected' : ''}>其他</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label for="location" class="form-label">丢失地点</label>
                        <input type="text" class="form-control" id="location" name="location" placeholder="地点" value="${param.location}">
                    </div>
                    <div class="col-md-2">
                        <label for="dateFrom" class="form-label">丢失日期从</label>
                        <input type="date" class="form-control" id="dateFrom" name="dateFrom" value="${param.dateFrom}">
                    </div>
                    <div class="col-md-2">
                        <label for="dateTo" class="form-label">丢失日期至</label>
                        <input type="date" class="form-control" id="dateTo" name="dateTo" value="${param.dateTo}">
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">搜索</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- 视图切换按钮 -->
        <div class="view-toggle">
            <div class="btn-group" role="group">
                <button type="button" class="btn btn-outline-primary view-btn" id="list-view-btn">
                    <i class="fas fa-list"></i> 列表视图
                </button>
                <button type="button" class="btn btn-outline-primary view-btn" id="masonry-view-btn">
                    <i class="fas fa-th-large"></i> 瀑布流视图
                </button>
            </div>
        </div>

        <c:if test="${empty lostItems}">
            <div class="alert alert-info mt-4" role="alert">
                暂无失物信息。
            </div>
        </c:if>

        <!-- 列表视图 -->
        <div id="list-view" class="row mt-4">
            <c:forEach var="item" items="${lostItems}">
                <div class="col-md-4 mb-4">
                    <div class="card h-100">
                        <c:if test="${item.imageUrl != null && !empty item.imageUrl}">
                            <img src="${item.imageUrl}" class="card-img-top" alt="${item.title}" style="height: 200px; object-fit: cover;">
                        </c:if>
                        <div class="card-body">
                            <h5 class="card-title">${item.title}</h5>
                            <p class="card-text">${item.description}</p>
                            <ul class="list-unstyled">
                                <li><strong>分类:</strong> ${item.category}</li>
                                <li><strong>丢失地点:</strong> ${item.lostLocation}</li>
                                <li><strong>丢失时间:</strong> ${item.lostTime}</li>
                                <li><strong>联系方式:</strong> ${item.contactInfo}</li>
                            </ul>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">发布于 ${item.createdAt}</small>
                            <a href="lost-items/detail?id=${item.id}" class="btn btn-sm btn-outline-primary float-end">查看详情</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- 瀑布流视图 -->
        <div id="masonry-view" class="masonry-grid" style="display: none;">
            <c:forEach var="item" items="${lostItems}">
                <div class="masonry-item">
                    <div class="card masonry-card">
                        <c:if test="${item.imageUrl != null && !empty item.imageUrl}">
                            <img src="${item.imageUrl}" class="card-img-top" alt="${item.title}">
                        </c:if>
                        <div class="card-body">
                            <h5 class="card-title">${item.title}</h5>
                            <p class="card-text">${item.description}</p>
                            <ul class="list-unstyled">
                                <li><strong>分类:</strong> ${item.category}</li>
                                <li><strong>丢失地点:</strong> ${item.lostLocation}</li>
                                <li><strong>丢失时间:</strong> ${item.lostTime}</li>
                            </ul>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">发布于 ${item.createdAt}</small>
                            <a href="lost-items/detail?id=${item.id}" class="btn btn-sm btn-outline-primary float-end">查看详情</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <!-- 分页 -->
        <nav aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1">上一页</a>
                </li>
                <li class="page-item active"><a class="page-link" href="#">1</a></li>
                <li class="page-item"><a class="page-link" href="#">2</a></li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
                <li class="page-item">
                    <a class="page-link" href="#">下一页</a>
                </li>
            </ul>
        </nav>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // 视图切换功能
        document.getElementById('list-view-btn').addEventListener('click', function() {
            document.getElementById('list-view').style.display = 'flex';
            document.getElementById('masonry-view').style.display = 'none';
            this.classList.remove('btn-outline-primary');
            this.classList.add('btn-primary');
            document.getElementById('masonry-view-btn').classList.remove('btn-primary');
            document.getElementById('masonry-view-btn').classList.add('btn-outline-primary');
        });
        
        document.getElementById('masonry-view-btn').addEventListener('click', function() {
            document.getElementById('list-view').style.display = 'none';
            document.getElementById('masonry-view').style.display = 'grid';
            this.classList.remove('btn-outline-primary');
            this.classList.add('btn-primary');
            document.getElementById('list-view-btn').classList.remove('btn-primary');
            document.getElementById('list-view-btn').classList.add('btn-outline-primary');
        });
    </script>
</body>
</html>