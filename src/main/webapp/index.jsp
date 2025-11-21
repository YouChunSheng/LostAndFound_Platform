<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>失物招领平台 - Lost and Found Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            font-family: "Microsoft YaHei", Arial, sans-serif;
        }
        
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
            position: relative;
            overflow: hidden;
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
        
        /* 英文元素样式 */
        .en-text {
            font-family: Arial, sans-serif;
            font-size: 0.75em;
            color: rgba(0, 0, 0, 0.5);
            display: block;
            margin-top: 0.25rem;
        }
        
        .en-text-light {
            font-family: Arial, sans-serif;
            font-size: 0.75em;
            color: rgba(255, 255, 255, 0.7);
            display: block;
            margin-top: 0.25rem;
        }
        
        .en-text-muted {
            font-family: Arial, sans-serif;
            font-size: 0.75em;
            color: rgba(108, 117, 125, 0.7);
            display: block;
            margin-top: 0.25rem;
        }
        
        /* 导航栏英文文本样式 */
        .navbar-nav .nav-link small {
            font-family: Arial, sans-serif;
            font-size: 0.7em;
            color: rgba(255, 255, 255, 0.7);
            display: block;
            margin-top: 0.1rem;
        }
        
        .navbar-nav .nav-link:hover small {
            color: rgba(255, 255, 255, 0.9);
        }
        
        /* 按钮中的英文文本样式 */
        .btn small {
            font-family: Arial, sans-serif;
            font-size: 0.65em;
            display: block;
            margin-top: 0.2rem;
        }
        
        /* 标题中的英文文本样式 */
        h1 small, h2 small, h3 small, h4 small, h5 small {
            font-family: Arial, sans-serif;
            font-size: 0.6em;
            display: block;
            margin-top: 0.25rem;
        }
        
        h1 small {
            font-size: 0.5em;
        }
        
        h2 small {
            font-size: 0.55em;
        }
        
        h3 small {
            font-size: 0.6em;
        }
        
        h5 small {
            font-size: 0.65em;
        }
        
        /* First 和 Second 文字样式 */
        .first-text {
            position: absolute;
            top: 10px;
            left: 10px;
            font-family: Arial, sans-serif;
            font-size: 5rem;
            font-weight: bold;
            color: rgba(128, 128, 128, 0); /* 灰色，完全透明 */
            transform: rotate(135deg);
            z-index: 0;
            pointer-events: none;
        }
        
        .second-text {
            position: absolute;
            top: 10px;
            left: 10px;
            font-family: Arial, sans-serif;
            font-size: 5rem;
            font-weight: bold;
            color: rgba(128, 128, 128, 0); /* 灰色，完全透明 */
            transform: rotate(135deg);
            z-index: 0;
            pointer-events: none;
        }
    </style>
</head>
<body>
    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
        <div class="container">
            <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">
                <i class="fas fa-search-location me-2"></i>失物招领平台
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="#index">首页<br><small>Home</small></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#information">统计数据<br><small>Statistics</small></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#operator">主要功能<br><small>Features</small></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#world">使用流程<br><small>How It Works</small></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#more">更多内容<br><small>More</small></a>
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
                                        <li><a class="dropdown-item" href="<%=request.getContextPath()%>/admin/">管理员面板</a></li>
                                        <li><hr class="dropdown-divider"></li>
                                    </c:if>
                                    <li><a class="dropdown-item" href="<%=request.getContextPath()%>/logout">退出登录</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="btn btn-outline-light me-2" href="<%=request.getContextPath()%>/login.jsp">登录</a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-warning" href="<%=request.getContextPath()%>/register.jsp">注册</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- 主页横幅 -->
    <section class="hero-section" id="index">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <h1 class="display-4 fw-bold mb-3">找回您的失物<br><small class="text-light">Recover Your Lost Items</small></h1>
                    <p class="lead mb-4">连接失主与拾主，让每一份善意都不被辜负<br>Connecting owners and finders, ensuring no kindness goes unrewarded</p>
                    <div class="d-flex gap-3">
                        <a href="<%=request.getContextPath()%>/lost-items/new" class="btn btn-light btn-lg">
                            <i class="fas fa-plus-circle me-2"></i>发布失物信息<br><small>Post Lost Item</small>
                        </a>
                        <a href="<%=request.getContextPath()%>/found-items/new" class="btn btn-outline-light btn-lg">
                            <i class="fas fa-hand-holding-heart me-2"></i>发布招领信息<br><small>Post Found Item</small>
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
    <section class="container my-5" id="information">
        <div class="row g-4">
            <div class="col-md-4">
                <div class="stats-card">
                    <div class="display-4 text-primary">1,234</div>
                    <h5>已找回物品<br><small class="text-muted">Items Recovered</small></h5>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stats-card">
                    <div class="display-4 text-success">567</div>
                    <h5>正在寻找<br><small class="text-muted">Items Searching</small></h5>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stats-card">
                    <div class="display-4 text-info">8,901</div>
                    <h5>热心用户<br><small class="text-muted">Active Users</small></h5>
                </div>
            </div>
        </div>
    </section>

    <!-- 主要功能 -->
    <section class="container my-5" id="operator">
        <div class="row g-4">
            <div class="col-md-6">
                <div class="card feature-card h-100 border-primary">
                    <div class="first-text">First</div>
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center mb-3">
                            <div class="bg-primary bg-opacity-10 p-3 rounded-circle me-3">
                                <i class="fas fa-search text-primary fs-3"></i>
                            </div>
                            <h3 class="card-title mb-0">查找失物</h3>
                        </div>
                        <p class="card-text">浏览最新的失物信息，寻找您可能丢失的物品。可以通过关键词、分类、地点等条件筛选。</p>
                        <a href="<%=request.getContextPath()%>/lost-items" class="btn btn-primary">查看失物信息</a>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card feature-card h-100 border-success">
                    <div class="second-text">Second</div>
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center mb-3">
                            <div class="bg-success bg-opacity-10 p-3 rounded-circle me-3">
                                <i class="fas fa-hand-holding-heart text-success fs-3"></i>
                            </div>
                            <h3 class="card-title mb-0">发布招领</h3>
                        </div>
                        <p class="card-text">捡到物品？在这里发布招领信息，帮助失主找回他们的物品，传递社会正能量。</p>
                        <a href="<%=request.getContextPath()%>/found-items" class="btn btn-success">查看招领信息</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- 使用流程 -->
    <section class="container my-5" id="world">
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
    <footer class="bg-dark text-light py-4 mt-5" id="more">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h5>失物招领平台<br><span class="en-text-light">Lost and Found Platform</span></h5>
                    <p>连接失主与拾主，传递社会正能量<br><span class="en-text-light">Connecting owners and finders, spreading positive social energy</span></p>
                </div>
                <div class="col-md-3">
                    <h5>快速链接<br><span class="en-text-light">Quick Links</span></h5>
                    <ul class="list-unstyled">
                        <li><a href="index.jsp" class="text-light text-decoration-none">首页<br><span class="en-text-light">Home</span></a></li>
                        <li><a href="lost-items" class="text-light text-decoration-none">失物信息<br><span class="en-text-light">Lost Items</span></a></li>
                        <li><a href="found-items" class="text-light text-decoration-none">招领信息<br><span class="en-text-light">Found Items</span></a></li>
                        <c:if test="${sessionScope.user != null && sessionScope.user.role == 'admin'}">
                            <li><a href="admin/" class="text-light text-decoration-none">管理员面板<br><span class="en-text-light">Admin Panel</span></a></li>
                        </c:if>
                    </ul>
                </div>
                <div class="col-md-3">
                    <h5>联系我们<br><span class="en-text-light">Contact Us</span></h5>
                    <ul class="list-unstyled">
                        <li><i class="fas fa-envelope me-2"></i> support@lostandfound.com</li>
                        <li><i class="fas fa-phone me-2"></i> 400-123-4567</li>
                    </ul>
                </div>
            </div>
            <hr>
            <div class="text-center">
                <p>&copy; 2025 失物招领平台. 保留所有权利.<br><span class="en-text-light">&copy; 2025 Lost and Found Platform. All rights reserved.</span></p>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
    <script>
        // 实现平滑滚动和URL hash更新功能
        document.addEventListener('DOMContentLoaded', function() {
            // 获取所有导航链接
            const navLinks = document.querySelectorAll('.navbar-nav a[href^="#"]');
            
            // 为每个导航链接添加点击事件监听器
            navLinks.forEach(link => {
                link.addEventListener('click', function(e) {
                    // 阻止默认的跳转行为
                    e.preventDefault();
                    
                    // 获取目标元素的ID
                    const targetId = this.getAttribute('href');
                    
                    // 如果目标是首页或者空hash，则滚动到顶部
                    if (targetId === '#' || targetId === '#index') {
                        window.scrollTo({
                            top: 0,
                            behavior: 'smooth'
                        });
                        updateActiveNavLink(this);
                        history.pushState(null, null, ' ');
                        return;
                    }
                    
                    // 获取目标元素
                    const targetElement = document.querySelector(targetId);
                    
                    if (targetElement) {
                        // 计算目标元素距离顶部的距离
                        const offsetTop = targetElement.offsetTop;
                        
                        // 平滑滚动到目标元素
                        window.scrollTo({
                            top: offsetTop,
                            behavior: 'smooth'
                        });
                        
                        // 更新活动导航项
                        updateActiveNavLink(this);
                        
                        // 更新URL hash
                        history.pushState(null, null, targetId);
                    }
                });
            });
            
            // 监听滚动事件，根据当前位置更新活动导航项
            let ticking = false;
            const sections = document.querySelectorAll('section[id], footer[id]');
            
           