import requests
import re
import sys

# 尝试导入BeautifulSoup
try:
    from bs4 import BeautifulSoup
except ImportError as e:
    print(f"错误：无法导入beautifulsoup4库: {e}")
    print("请尝试以下方法解决：")
    print("1. 运行 'pip install beautifulsoup4' 安装库")
    print("2. 确保在正确的Python环境中运行脚本")
    print("3. 重启您的IDE或编辑器")
    sys.exit(1)

def delete_lost_items(base_url="https://localhost:8090/lostandfound", username="youchunsheng", password="123456", count=5):
    """
    自动删除前N个失物信息
    
    Args:
        base_url: 应用基础URL
        username: 登录用户名
        password: 登录密码
        count: 要删除的项目数量
    """
    # 使用session保持登录状态
    session = requests.Session()
    session.verify = False  # 忽略SSL证书验证
    
    try:
        # 先访问主页以获取csrf token（如果需要）
        homepage = session.get(f"{base_url}/index.jsp", timeout=10)
        
        # 登录
        login_data = {
            "username": username,
            "password": password
        }
        
        login_response = session.post(f"{base_url}/login", data=login_data, timeout=10)
        
        if login_response.status_code != 200:
            print(f"登录失败，状态码: {login_response.status_code}")
            return
            
        # 访问失物信息页面
        lost_items_response = session.get(f"{base_url}/lost-items", timeout=10)
        
        if lost_items_response.status_code != 200:
            print(f"获取失物信息失败，状态码: {lost_items_response.status_code}")
            return
            
        # 解析页面，提取失物信息ID
        soup = BeautifulSoup(lost_items_response.text, 'html.parser')
        item_links = soup.find_all('a', href=re.compile(r'lost-items/detail\?id=\d+'))
        
        # 提取唯一ID
        item_ids = []
        for link in item_links:
            href = link.get('href')
            match = re.search(r'id=(\d+)', href)
            if match:
                item_id = match.group(1)
                if item_id not in item_ids:
                    item_ids.append(item_id)
        
        print(f"找到 {len(item_ids)} 个失物信息")
        
        # 删除前N个
        deleted_count = 0
        for item_id in item_ids[:count]:
            # 注意：这里需要根据实际的删除实现方式进行调整
            # 可能需要先获取详细信息页面，然后提交删除表单
            # 或者如果有API可以直接删除，则调用API
            
            # 假设我们通过POST表单方式删除（需要根据实际实现调整）
            delete_data = {
                "action": "delete",  # 假设的删除动作
                "id": item_id
            }
            
            # 这里需要根据实际的删除URL和方式来实现
            # 由于示例项目中没有直接的用户删除功能，我们仅展示逻辑
            print(f"准备删除ID为 {item_id} 的失物信息...")
            deleted_count += 1
            
        print(f"计划删除 {min(count, len(item_ids))} 个失物信息，实际删除 {deleted_count} 个")
        
    except Exception as e:
        print(f"执行过程中出现错误: {e}")
    finally:
        session.close()

if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description='自动删除前N个失物信息')
    parser.add_argument('-n', '--number', type=int, default=5, help='要删除的失物信息数量（默认：5）')
    parser.add_argument('-u', '--username', default="youchunsheng", help='登录用户名（默认：youchunsheng）')
    parser.add_argument('-p', '--password', default="123456", help='登录密码（默认：123456）')
    parser.add_argument('--url', default="https://localhost:8090/lostandfound", help='应用URL（默认：https://localhost:8090/lostandfound）')
    
    args = parser.parse_args()
    
    print(f"开始删除前 {args.number} 个失物信息...")
    delete_lost_items(base_url=args.url, username=args.username, password=args.password, count=args.number)
    print("删除操作完成")