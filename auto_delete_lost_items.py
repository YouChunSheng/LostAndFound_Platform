import requests
from bs4 import BeautifulSoup
import argparse
import urllib3
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

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
        item_links = soup.find_all('a', href=lambda x: x and 'lost-items/detail?id=' in x)
        
        item_ids = []
        for link in item_links:
            href = link.get('href')
            # 提取ID参数
            if 'id=' in href:
                id_str = href.split('id=')[1].split('&')[0] if '&' in href else href.split('id=')[1]
                try:
                    item_id = int(id_str)
                    if item_id not in item_ids:
                        item_ids.append(item_id)
                except ValueError:
                    continue
        
        print(f"找到 {len(item_ids)} 个失物信息")
        
        # 删除前count个物品
        deleted_count = 0
        for item_id in item_ids[:count]:
            # 访问详情页获取CSRF信息（如果有需要）
            detail_page = session.get(f"{base_url}/lost-items/detail?id={item_id}", timeout=10)
            if detail_page.status_code != 200:
                print(f"无法访问物品详情页，ID: {item_id}")
                continue
                
            # 执行删除操作
            delete_data = {
                "action": "delete",
                "id": str(item_id)
            }
            
            delete_response = session.post(f"{base_url}/lost-items/detail", data=delete_data, timeout=10)
            
            if delete_response.status_code in [200, 302]:  # 200表示成功，302表示重定向
                print(f"成功删除失物信息，ID: {item_id}")
                deleted_count += 1
            else:
                print(f"删除失败，ID: {item_id}，状态码: {delete_response.status_code}")
        
        print(f"总共尝试删除 {min(count, len(item_ids))} 个物品，成功删除 {deleted_count} 个物品")
        
    except Exception as e:
        print(f"发生异常: {str(e)}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='自动删除失物招领平台的前N个失物信息')
    parser.add_argument('-n', '--number', type=int, default=5, help='要删除的失物信息数量 (默认: 5)')
    parser.add_argument('-u', '--username', default="youchunsheng", help='登录用户名 (默认: youchunsheng)')
    parser.add_argument('-p', '--password', default="123456", help='登录密码 (默认: 123456)')
    parser.add_argument('--url', default="https://localhost:8090/lostandfound", help='应用基础URL (默认: https://localhost:8090/lostandfound)')
    
    args = parser.parse_args()
    
    delete_lost_items(
        base_url=args.url,
        username=args.username,
        password=args.password,
        count=args.number
    )