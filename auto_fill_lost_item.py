import os
import random
import time
import datetime
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options

# 预设数据
ITEM_NAMES = [
    "手机", "钱包", "钥匙", "身份证", "学生证", "银行卡", "耳机", "U盘", 
    "笔记本电脑", "平板电脑", "书本", "眼镜", "手表", "外套", "雨伞"
]

LOCATIONS = [
    "教学楼", "实验楼", "图书馆", "宿舍", "食堂", "体育馆", "校医院", 
    "行政楼", "超市", "校门", "一二期区域", "中部区域", "一期区域", "其他"
]

CATEGORIES = ["证件", "电子设备", "衣物", "书籍", "其他"]

CONTACT_INFOS = [
    "13812345678", "zhangsan@example.com", "lisi@example.com", 
    "13987654321", "wangwu@example.com"
]

def get_next_photo_path(photo_dir="photo"):
    """
    获取photo目录下按阿拉伯数字升序排列的下一个图片路径
    """
    if not os.path.exists(photo_dir):
        print(f"目录 {photo_dir} 不存在")
        return None
    
    # 获取所有图片文件
    photo_files = [f for f in os.listdir(photo_dir) if f.lower().endswith(('.png', '.jpg', '.jpeg', '.gif'))]
    
    if not photo_files:
        print(f"目录 {photo_dir} 中没有图片文件")
        return None
    
    # 按数字排序
    photo_files.sort(key=lambda x: int(os.path.splitext(x)[0]) if x.split('.')[0].isdigit() else 0)
    
    # 返回第一个图片的路径
    return os.path.join(photo_dir, photo_files[0])

def generate_random_datetime():
    """
    生成随机的日期时间字符串，符合HTML datetime-local格式 (YYYY-MM-DDTHH:MM)
    """
    # 生成最近30天内的随机日期
    now = datetime.datetime.now()
    random_days = random.randint(0, 30)
    random_date = now - datetime.timedelta(days=random_days)
    
    # 生成随机时间
    random_hour = random.randint(8, 22)
    random_minute = random.randint(0, 59)
    
    # 格式化为HTML datetime-local格式 (YYYY-MM-DDTHH:MM)
    time_str = random_date.strftime(f"%Y-%m-%dT{random_hour:02d}:{random_minute:02d}")
    print(f"生成的时间字符串: {time_str}")  # 调试信息
    return time_str

def auto_fill_lost_item_form(username="youchunsheng", password="123456", base_url="https://localhost:8090/lostandfound"):
    """
    自动填写失物信息表单
    """
    # 配置Chrome浏览器选项
    chrome_options = Options()
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--ignore-certificate-errors")  # 忽略SSL证书错误
    chrome_options.add_argument("--ignore-ssl-errors")          # 忽略SSL错误
    
    # 添加用户代理以避免被识别为自动化脚本
    chrome_options.add_argument("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
    
    # 如果需要无头模式（不显示浏览器窗口），取消下面这行注释
    # chrome_options.add_argument("--headless")
    
    try:
        # 初始化WebDriver（请根据您的Chrome版本调整chromedriver路径）
        driver = webdriver.Chrome(options=chrome_options)
        wait = WebDriverWait(driver, 10)
        
        print("=== 开始登录 ===")
        # 访问登录页面
        driver.get(f"{base_url}/login.jsp")
        time.sleep(2)
        print(f"当前页面URL: {driver.current_url}")
        
        # 检查页面标题和基本元素，确认页面加载
        try:
            title = driver.title
            print(f"页面标题: {title}")
        except:
            print("无法获取页面标题")
        
        # 填写登录表单（使用指定账户）
        driver.find_element(By.ID, "username").send_keys(username)
        driver.find_element(By.ID, "password").send_keys(password)
        
        # 提交登录表单
        driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
        time.sleep(3)
        
        # 检查是否登录成功
        print(f"登录后页面URL: {driver.current_url}")
        if "login=success" in driver.current_url or "index.jsp" in driver.current_url:
            print("登录成功")
        else:
            print("登录可能失败，继续尝试访问发布页面...")
        
        # 循环添加20件物品
        for i in range(20):
            print(f"\n=== 开始添加第 {i+1} 件物品 ===")
            
            print("=== 访问发布失物信息页面 ===")
            # 访问发布失物信息页面
            driver.get(f"{base_url}/lost-items/new")
            time.sleep(3)
            print(f"发布页面URL: {driver.current_url}")
            
            # 检查页面元素，确认页面加载
            try:
                page_title_element = driver.find_element(By.CSS_SELECTOR, "h3.text-center")
                print(f"页面标题元素文本: {page_title_element.text}")
            except:
                print("未找到预期的页面标题元素")
            
            # 填写表单
            # 标题（随机选择）
            title = random.choice(ITEM_NAMES)
            driver.find_element(By.ID, "title").send_keys(title)
            print(f"填写标题: {title}")
            
            # 描述（基于标题生成）
            description = f"丢失了一个{title}，特征是{random.choice(['黑色的', '白色的', '红色的', '蓝色的', '崭新的', '旧的'])}"
            driver.find_element(By.ID, "description").send_keys(description)
            print(f"填写描述: {description}")
            
            # 分类（随机选择）
            category_select = Select(driver.find_element(By.ID, "category"))
            category = random.choice(CATEGORIES)
            category_select.select_by_visible_text(category)
            print(f"选择分类: {category}")
            
            # 丢失地点（随机选择）
            location = random.choice(LOCATIONS)
            driver.find_element(By.ID, "lostLocation").send_keys(location)
            print(f"填写地点: {location}")
            
            # 丢失时间（随机生成）
            lost_time = generate_random_datetime()
            time_input = driver.find_element(By.ID, "lostTime")
            time_input.clear()
            time_input.send_keys(lost_time)
            print(f"填写时间: {lost_time}")
            
            # 添加延迟以便观察时间输入
            time.sleep(2)
            
            # 检查时间是否正确输入
            entered_time = time_input.get_attribute('value')
            print(f"输入框中的时间值: {entered_time}")
            
            # 验证时间格式是否正确（包含分钟）
            if entered_time and len(entered_time) >= 16:  # 至少包含 YYYY-MM-DDTHH:MM
                # 检查分钟部分是否存在
                time_parts = entered_time.split('T')
                if len(time_parts) == 2:
                    time_part = time_parts[1]  # HH:MM 格式
                    if ':' in time_part:
                        hour_minute = time_part.split(':')
                        if len(hour_minute) >= 2 and hour_minute[1].isdigit() and len(hour_minute[1]) == 2:
                            print(f"时间格式正确，包含小时和分钟: {hour_minute[0]}:{hour_minute[1]}")
                        else:
                            print("警告：时间格式可能不正确，分钟部分有问题")
                    else:
                        print("警告：时间格式可能不正确，缺少冒号分隔符")
                else:
                    print("警告：时间格式可能不正确，缺少T分隔符")
            else:
                print("警告：输入的时间值太短，可能格式不正确")
            
            # 联系方式（随机选择）
            contact = random.choice(CONTACT_INFOS)
            driver.find_element(By.ID, "contactInfo").send_keys(contact)
            print(f"填写联系方式: {contact}")
            
            # 上传图片（如果有）
            photo_path = get_next_photo_path()
            if photo_path and os.path.exists(photo_path):
                driver.find_element(By.ID, "image").send_keys(os.path.abspath(photo_path))
                print(f"已选择图片: {photo_path}")
            else:
                print("未找到图片，跳过图片上传")
            
            print("=== 提交表单 ===")
            # 提交表单
            driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
            print("已点击提交按钮")
            time.sleep(3)
            
            # 检查是否提交成功（根据后端代码，成功后会跳转到lost-items页面）
            print(f"提交后页面URL: {driver.current_url}")
            current_url = driver.current_url
            if "lost-items" in current_url and "new" not in current_url:
                print(f"第 {i+1} 件失物信息发布成功！页面已跳转到失物信息列表页。")
            elif "error" in current_url:
                print(f"第 {i+1} 件物品发布失败，页面包含错误信息。")
            else:
                print(f"第 {i+1} 件物品发布可能失败或者页面跳转异常，请检查页面")
                
            # 等待并检查页面元素
            time.sleep(2)
        
        print("\n=== 所有物品添加完成 ===")
        
    except Exception as e:
        print(f"发生错误: {e}")
        import traceback
        traceback.print_exc()
    finally:
        # 关闭浏览器
        try:
            # 在关闭前等待用户查看结果
            time.sleep(5)
            driver.quit()
        except:
            pass

if __name__ == "__main__":
    print("开始自动填写失物信息表单，将添加20件物品...")
    auto_fill_lost_item_form()
    print("程序执行完毕")