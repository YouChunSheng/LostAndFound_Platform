#!/usr/bin/env python
# -*- coding: utf-8 -*-

try:
    import urllib.request
    import urllib.parse
    import urllib.error
    import http.cookiejar
    import argparse
    import ssl
    import sys
    import time
    import re
except ImportError as e:
    print("导入模块失败: " + str(e))
    print("请确保您使用的是Python 3.x版本")
    sys.exit(1)

def delete_found_items(base_url="https://localhost:8090/LostAndFound_Platform/", username="admin", password="88888", count=5):
    """
    自动删除前N个招领信息
    
    Args:
        base_url: 应用基础URL
        username: 登录用户名
        password: 登录密码
        count: 要删除的项目数量
    """
    try:
        # 创建一个不验证SSL证书的上下文
        ssl_context = ssl.create_default_context()
        ssl_context.check_hostname = False
        ssl_context.verify_mode = ssl.CERT_NONE
        
        # 创建cookie处理器以保持会话
        cookie_jar = http.cookiejar.CookieJar()
        cookie_handler = urllib.request.HTTPCookieProcessor(cookie_jar)
        https_handler = urllib.request.HTTPSHandler(context=ssl_context)
        
        # 创建opener
        opener = urllib.request.build_opener(cookie_handler, https_handler)
        opener.addheaders = [('User-Agent', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36')]
        
        print(f"正在连接到 {base_url}...")
        
        # 先访问主页
        homepage_request = urllib.request.Request(f"{base_url}/index.jsp")
        homepage_response = opener.open(homepage_request, timeout=10)
        if homepage_response.getcode() != 200:
            print(f"无法访问主页，状态码: {homepage_response.getcode()}")
            return False
            
        # 登录
        print(f"正在使用账户 {username} 登录...")
        login_data = urllib.parse.urlencode({
            "username": username,
            "password": password
        }).encode('utf-8')
        
        login_request = urllib.request.Request(f"{base_url}/login", data=login_data)
        login_response = opener.open(login_request, timeout=10)
        
        # 检查是否登录成功
        response_url = login_response.geturl()
        
        if "login=success" not in response_url and "error=invalid_credentials" in response_url:
            print("登录失败：用户名或密码错误")
            return False
        elif login_response.getcode() != 200 and "login=success" not in response_url:
            print(f"登录失败，状态码: {login_response.getcode()}")
            return False
            
        print("登录成功")
        
        # 访问招领信息页面
        print("正在获取招领信息列表...")
        found_items_request = urllib.request.Request(f"{base_url}/found-items")
        found_items_response = opener.open(found_items_request, timeout=10)
        
        if found_items_response.getcode() != 200:
            print(f"获取招领信息失败，状态码: {found_items_response.getcode()}")
            return False
            
        # 解析页面，提取招领信息ID
        response_text = found_items_response.read().decode('utf-8')
        
        # 使用正则表达式查找所有包含招领信息详情链接的元素
        item_links = re.findall(r'found-items/detail\?id=(\d+)', response_text)
        item_ids = list(set(item_links))  # 去重
        
        if not item_ids:
            print("未找到任何可删除的招领信息")
            return True
            
        print(f"找到 {len(item_ids)} 个招领信息")
        
        # 删除前count个物品
        deleted_count = 0
        for i, item_id in enumerate(item_ids[:count]):
            print(f"正在删除第 {i+1} 个招领信息 (ID: {item_id})...")
            
            # 执行删除操作 - 直接POST到found-items路径
            delete_data = urllib.parse.urlencode({
                "action": "delete",
                "id": str(item_id)
            }).encode('utf-8')
            
            try:
                delete_request = urllib.request.Request(f"{base_url}/found-items", data=delete_data)
                delete_response = opener.open(delete_request, timeout=10)
                print(f"  成功删除招领信息，ID: {item_id}")
                deleted_count += 1
            except urllib.error.HTTPError as e:
                # 处理302重定向等HTTP错误
                if e.code in [302, 303]:
                    print(f"  成功删除招领信息，ID: {item_id} (重定向响应)")
                    deleted_count += 1
                else:
                    print(f"  删除失败，ID: {item_id}，错误代码: {e.code}")
            except Exception as e:
                print(f"  删除失败，ID: {item_id}，错误: {str(e)}")
            
            # 添加延迟避免请求过于频繁
            time.sleep(0.5)
        
        print(f"总共尝试删除 {min(count, len(item_ids))} 个物品，成功删除 {deleted_count} 个物品")
        return deleted_count > 0
        
    except Exception as e:
        print(f"发生异常: {str(e)}")
        return False

def main():
    parser = argparse.ArgumentParser(description='自动删除失物招领平台的前N个招领信息')
    parser.add_argument('-n', '--number', type=int, default=5, help='要删除的招领信息数量 (默认: 5)')
    parser.add_argument('-u', '--username', default="admin", help='登录用户名 (默认: admin)')
    parser.add_argument('-p', '--password', default="88888", help='登录密码 (默认: 88888)')
    parser.add_argument('--url', default="https://localhost:8090/LostAndFound_Platform/", help='应用基础URL (默认: https://localhost:8090/LostAndFound_Platform/)')
    
    args = parser.parse_args()
    
    success = delete_found_items(
        base_url=args.url,
        username=args.username,
        password=args.password,
        count=args.number
    )
    
    # 根据执行结果设置退出码
    sys.exit(0 if success else 1)

if __name__ == "__main__":
    main()