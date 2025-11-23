<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户登录 - 失物招领平台</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">失物招领平台</a>
        </div>
    </nav>

    <div class="row justify-content-center mt-5">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">用户登录</h3>
                </div>
                <div class="card-body">
                    <% if (request.getParameter("error") != null) { %>
                        <div class="alert alert-danger" role="alert">
                            <% if (request.getParameter("error").equals("invalid_credentials")) { %>
                                用户名或密码错误
                            <% } else if (request.getParameter("error").equals("database_error")) { %>
                                数据库错误，请稍后再试
                            <% } %>
                        </div>
                    <% } %>

                    <form action="<%=request.getContextPath()%>/login" method="post" id="loginForm">
                        <div class="mb-3">
                            <label for="username" class="form-label">用户名</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">密码</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">登录</button>
                        </div>
                    </form>
                    
                    <!-- 预设快速登录选项 -->
                    <div class="mt-4">
                        <p class="text-center">快速登录选项：</p>
                        <div class="d-grid gap-2">
                            <button type="button" class="btn btn-outline-secondary" onclick="fillLoginForm('admin', '88888')">
                                1. 以管理员账户密码登录 admin，88888
                            </button>
                            <button type="button" class="btn btn-outline-secondary" onclick="fillLoginForm('youchunsheng', '123456')">
                                2. 以测试账户密码登录 youchunsheng ，123456
                            </button>
                        </div>
                    </div>
                    
                    <!-- 自定义快速登录账户 -->
                    <div class="mt-4">
                        <p class="text-center">自定义快速登录：</p>
                        <div id="customAccounts" class="d-grid gap-2">
                            <!-- 自定义账户按钮将在这里动态添加 -->
                        </div>
                        <div class="mt-3">
                            <button type="button" class="btn btn-sm btn-outline-primary" onclick="showAddAccountModal()">添加自定义账户</button>
                        </div>
                    </div>
                    
                    <div class="mt-3 text-center">
                        <p>还没有账号？<a href="<%=request.getContextPath()%>/register.jsp">立即注册</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- 添加自定义账户模态框 -->
    <div class="modal fade" id="addAccountModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">添加自定义账户</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="addAccountForm">
                        <div class="mb-3">
                            <label for="customUsername" class="form-label">用户名</label>
                            <input type="text" class="form-control" id="customUsername" required>
                        </div>
                        <div class="mb-3">
                            <label for="customPassword" class="form-label">密码</label>
                            <input type="password" class="form-control" id="customPassword" required>
                        </div>
                        <div class="mb-3">
                            <label for="customLabel" class="form-label">标签（可选）</label>
                            <input type="text" class="form-control" id="customLabel" placeholder="例如：我的账户">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" onclick="addCustomAccount()">添加</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function fillLoginForm(username, password) {
        document.getElementById('username').value = username;
        document.getElementById('password').value = password;
        // 自动提交表单
        document.getElementById('loginForm').submit();
    }
    
    function showAddAccountModal() {
        // 清空表单
        document.getElementById('addAccountForm').reset();
        // 显示模态框
        var addAccountModal = new bootstrap.Modal(document.getElementById('addAccountModal'));
        addAccountModal.show();
    }
    
    function addCustomAccount() {
        const username = document.getElementById('customUsername').value.trim();
        const password = document.getElementById('customPassword').value.trim();
        const label = document.getElementById('customLabel').value.trim();
        
        if (!username || !password) {
            alert('用户名和密码不能为空');
            return;
        }
        
        // 获取现有的自定义账户
        let customAccounts = JSON.parse(localStorage.getItem('customLoginAccounts') || '[]');
        
        // 检查是否已存在相同的用户名
        if (customAccounts.some(account => account.username === username)) {
            if (!confirm('该用户名已存在，是否覆盖？')) {
                return;
            }
            // 移除已存在的账户
            customAccounts = customAccounts.filter(account => account.username !== username);
        }
        
        // 添加新账户
        customAccounts.push({
            username: username,
            password: password,
            label: label || username
        });
        
        // 保存到localStorage
        localStorage.setItem('customLoginAccounts', JSON.stringify(customAccounts));
        
        // 关闭模态框
        bootstrap.Modal.getInstance(document.getElementById('addAccountModal')).hide();
        
        // 更新显示
        displayCustomAccounts();
        
        // 显示成功消息
        alert('自定义账户添加成功');
    }
    
    function removeCustomAccount(username) {
        if (!confirm('确定要删除这个自定义账户吗？')) {
            return;
        }
        
        let customAccounts = JSON.parse(localStorage.getItem('customLoginAccounts') || '[]');
        customAccounts = customAccounts.filter(account => account.username !== username);
        localStorage.setItem('customLoginAccounts', JSON.stringify(customAccounts));
        
        displayCustomAccounts();
    }
    
    function displayCustomAccounts() {
        const container = document.getElementById('customAccounts');
        const customAccounts = JSON.parse(localStorage.getItem('customLoginAccounts') || '[]');
        
        // 清空现有内容
        container.innerHTML = '';
        
        // 如果没有自定义账户，显示提示信息
        if (customAccounts.length === 0) {
            container.innerHTML = '<p class="text-muted text-center">暂无自定义账户</p>';
            return;
        }
        
        // 为每个自定义账户创建按钮
        customAccounts.forEach((account, index) => {
            const button = document.createElement('div');
            button.className = 'd-flex gap-2';
            button.innerHTML = `
                <button type="button" class="btn btn-outline-info flex-grow-1" onclick="fillLoginForm('${account.username}', '${account.password}')">
                    ${index + 3}. ${account.label} (${account.username})
                </button>
                <button type="button" class="btn btn-outline-danger" onclick="removeCustomAccount('${account.username}')">
                    &times;
                </button>
            `;
            container.appendChild(button);
        });
    }
    
    // 页面加载时显示自定义账户
    document.addEventListener('DOMContentLoaded', function() {
        displayCustomAccounts();
    });
</script>
</body>
</html>