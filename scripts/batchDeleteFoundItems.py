#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import argparse
import sys

def batch_delete_found_items(base_url, session_id, item_ids):
    """
    批量删除招领信息
    
    Args:
        base_url (str): 应用的基础URL
        session_id (str): 管理员会话ID
        item_ids (list): 要删除的招领信息ID列表
    """
    # 构造请求URL
    url = f"{base_url}/admin/batch-delete-found-items"
    
    # 设置请求头，包含会话信息
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Cookie': f'JSESSIONID={session_id}'
    }
    
    # 构造表单数据
    data = '&'.join([f'ids={item_id}' for item_id in item_ids])
    
    try:
        # 发送POST请求
        response = requests.post(url, headers=headers, data=data)
        
        # 检查响应状态
        if response.status_code == 200:
            result = response.json()
            if result.get('success'):
                print(f"成功: {result.get('message')}")
                return True
            else:
                print(f"失败: {result.get('message')}")
                return False
        else:
            print(f"HTTP错误: {response.status_code}")
            return False
            
    except requests.exceptions.RequestException as e:
        print(f"请求错误: {e}")
        return False

def main():
    parser = argparse.ArgumentParser(description='批量删除招领信息')
    parser.add_argument('-u', '--url', required=True, help='应用的基础URL (例如: http://localhost:8080/lostandfound)')
    parser.add_argument('-s', '--session', required=True, help='管理员会话ID')
    parser.add_argument('-n', '--number', type=int, default=5, help='要删除的招领信息数量 (默认: 5)')
    parser.add_argument('--ids', nargs='+', type=int, help='要删除的招领信息ID列表')
    
    args = parser.parse_args()
    
    # 如果提供了ID列表，则直接使用
    if args.ids:
        item_ids = args.ids
    else:
        # 否则生成ID列表 (这里我们假设要删除ID为1到n的招领信息)
        item_ids = list(range(1, args.number + 1))
    
    print(f"准备删除 {len(item_ids)} 项招领信息，ID为: {item_ids}")
    
    if batch_delete_found_items(args.url, args.session, item_ids):
        print("批量删除操作完成")
        sys.exit(0)
    else:
        print("批量删除操作失败")
        sys.exit(1)

if __name__ == '__main__':
    main()