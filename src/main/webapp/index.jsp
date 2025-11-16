<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .hero-section {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            color: white;
            padding: 4rem 0;
            margin-bottom: 2rem;
            border-radius: 0 0 1rem 1rem;
        }
        
        .feature-card {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            height: 100%;
        }
        
        .feature-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }
        
        .stats-card {
            text-align: center;
            padding: 1.5rem;
            border-radius: 0.5rem;
            background: white;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .how-it-works-step {
            text-align: center;
            padding: 1.5rem;
        }
        
        .step-icon {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            color: #0d6efd;
        }
    </style>
</head>
<body>
    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
        <div class="container">
            <a class="navbar-brand" href="index.jsp">
                <i class="fas fa-search-location me-2"></i>失物招领平台
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="index.jsp">首页</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="lost-items">失物信息</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="found-items">招领信息</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                                    <i class="fas fa-user-circle me-1"></i>欢迎, ${sessionScope.user.username}
                                </a>
                                <ul class="dropdown-menu">
                                    <c:if test="${sessionScope.user.role == 'admin'}">
                                        <li><a class="dropdown-item" href="admin/">管理员面板</a></li>
                                        <li><hr class="dropdown-divider"></li>
                                    </c:if>
                                    <li><a class="dropdown-item" href="logout">退出登录</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="btn btn-outline-light me-2" href="login.jsp">登录</a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-warning" href="register.jsp">注册</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- 主页横幅 -->
    <section class="hero-section">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <h1 class="display-4 fw-bold mb-3">找回您的失物</h1>
                    <p class="lead mb-4">连接失主与拾主，让每一份善意都不被辜负。快速发布失物信息，轻松找到遗失物品。</p>
                    <div class="d-flex gap-3">
                        <a href="lost-items/new" class="btn btn-light btn-lg">
                            <i class="fas fa-plus-circle me-2"></i>发布失物信息
                        </a>
                        <a href="found-items/new" class="btn btn-outline-light btn-lg">
                            <i class="fas fa-hand-holding-heart me-2"></i>发布招领信息
                        </a>
                    </div>
                </div>
                <div class="col-lg-6 d-none d-lg-block text-center">
                    <img src="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/icons/search.svg" width="200" height="200" alt="失物招领图标" class="img-fluid">
                </div>
            </div>
        </div>
    </section>

    <!-- 统计数据 -->
    <section class="container my-5">
        <div class="row g-4">
            <div class="col-md-4">
                <div class="stats-card">
                    <div class="display-4 text-primary">1,234</div>
                    <h5>已找回物品</h5>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stats-card">
                    <div class="display-4 text-success">567</div>
                    <h5>正在寻找</h5>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stats-card">
                    <div class="display-4 text-info">8,901</div>
                    <h5>热心用户</h5>
                </div>
            </div>
        </div>
    </section>

    <!-- 主要功能 -->
    <section class="container my-5">
        <div class="row g-4">
            <div class="col-md-6">
                <div class="card feature-card h-100 border-primary">
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center mb-3">
                            <div class="bg-primary bg-opacity-10 p-3 rounded-circle me-3">
                                <i class="fas fa-search text-primary fs-3"></i>
                            </div>
                            <h3 class="card-title mb-0">查找失物</h3>
                        </div>
                        <p class="card-text">浏览最新的失物信息，寻找您可能丢失的物品。可以通过关键词、分类、地点等条件筛选。</p>
                        <a href="lost-items" class="btn btn-primary">查看失物信息</a>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card feature-card h-100 border-success">
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center mb-3">
                            <div class="bg-success bg-opacity-10 p-3 rounded-circle me-3">
                                <i class="fas fa-hand-holding-heart text-success fs-3"></i>
                            </div>
                            <h3 class="card-title mb-0">发布招领</h3>
                        </div>
                        <p class="card-text">捡到物品？在这里发布招领信息，帮助失主找回他们的物品，传递社会正能量。</p>
                        <a href="found-items" class="btn btn-success">查看招领信息</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 使用流程 -->
    <section class="container my-5">
        <h2 class="text-center mb-5">如何使用</h2>
        <div class="row g-4">
            <div class="col-md-3">
                <div class="how-it-works-step">
                    <div class="step-icon">
                        <i class="fas fa-user-plus"></i>
                    </div>
                    <h5>注册账户</h5>
                    <p>创建您的个人账户，开始使用平台服务</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="how-it-works-step">
                    <div class="step-icon">
                        <i class="fas fa-plus-circle"></i>
                    </div>
                    <h5>发布信息</h5>
                    <p>发布失物或招领信息，详细描述物品特征</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="how-it-works-step">
                    <div class="step-icon">
                        <i class="fas fa-search"></i>
                    </div>
                    <h5>查找匹配</h5>
                    <p>系统会自动为您匹配相关信息</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="how-it-works-step">
                    <div class="step-icon">
                        <i class="fas fa-hands-helping"></i>
                    </div>
                    <h5>联系确认</h5>
                    <p>与对方联系确认，完成物品交接</p>
                </div>
            </div>
        </div>
    </section>

    <!-- 页脚 -->
    <footer class="bg-dark text-light py-4 mt-5">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h5>失物招领平台</h5>
                    <p>连接失主与拾主，传递社会正能量</p>
                </div>
                <div class="col-md-3">
                    <h5>快速链接</h5>
                    <ul class="list-unstyled">
                        <li><a href="index.jsp" class="text-light text-decoration-none">首页</a></li>
                        <li><a href="lost-items" class="text-light text-decoration-none">失物信息</a></li>
                        <li><a href="found-items" class="text-light text-decoration-none">招领信息</a></li>
                        <c:if test="${sessionScope.user != null && sessionScope.user.role == 'admin'}">
                            <li><a href="admin/" class="text-light text-decoration-none">管理员面板</a></li>
                        </c:if>
                    </ul>
                </div>
                <div class="col-md-3">
                    <h5>联系我们</h5>
                    <ul class="list-unstyled">
                        <li><i class="fas fa-envelope me-2"></i> support@lostandfound.com</li>
                        <li><i class="fas fa-phone me-2"></i> 400-123-4567</li>
                    </ul>
                </div>
            </div>
            <hr>
            <div class="text-center">
                <p>&copy; 2025 失物招领平台. 保留所有权利.</p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</body>
</html>