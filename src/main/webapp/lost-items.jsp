<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>失物信息 - 失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
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
                    <div class="col-md-4">
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
                    <div class="col-md-3">
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
                        <input type="text" class="form-control" id="location" name="location" placeholder="地点" value="${param.location}" list="locations">
                        <datalist id="locations">
                            <optgroup label="一、二期区域 - 学生生活与运动区">
                                <option value="学生宿舍8栋">
                                <option value="学生宿舍9栋">
                                <option value="学生宿舍10栋">
                                <option value="学生宿舍11栋">
                                <option value="学生宿舍12栋">
                                <option value="学生宿舍13栋">
                                <option value="第二饭堂">
                                <option value="运动场">
                                <option value="运动场看台">
                                <option value="篮球场">
                                <option value="北门2">
                            </optgroup>
                            <optgroup label="未开放区域">
                                <option value="未开放区域">
                                <option value="未开放水域">
                            </optgroup>
                            <optgroup label="中部区域 - 教学与科研区">
                                <option value="图书馆">
                                <option value="公共实验楼">
                                <option value="软件学院">
                                <option value="健康学院">
                                <option value="护理学院">
                                <option value="公共教学楼">
                                <option value="行政楼">
                                <option value="北正门">
                            </optgroup>
                            <optgroup label="一期区域 - 学生生活与功能区">
                                <option value="学生宿舍1栋">
                                <option value="学生宿舍2栋">
                                <option value="学生宿舍3栋">
                                <option value="学生宿舍4栋">
                                <option value="学生宿舍5栋">
                                <option value="学生宿舍6栋">
                                <option value="学生宿舍7栋">
                                <option value="第一饭堂">
                                <option value="南药中心">
                                <option value="创新创业大楼">
                                <option value="联盟大楼交流中心">
                                <option value="会议中心">
                                <option value="排球场">
                                <option value="网球场">
                                <option value="篮球场">
                                <option value="西门">
                                <option value="北门1">
                                <option value="广东药科大学公交站">
                            </optgroup>
                            <optgroup label="其他">
                                <option value="其他地点">
                            </optgroup>
                        </datalist>
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">搜索</button>
                    </div>
                </form>
            </div>
        </div>

        <c:if test="${empty lostItems}">
            <div class="alert alert-info mt-4" role="alert">
                暂无失物信息。
            </div>
        </c:if>

        <div class="row mt-4">
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
</body>
</html>