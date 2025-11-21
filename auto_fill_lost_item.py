import os
import random
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
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
    生成随机的日期时间字符串
    """
    # 生成最近30天内的随机日期
    import datetime
    now = datetime.datetime.now()
    random_days = random.randint(0, 30)
    random_date = now - datetime.timedelta(days=random_days)
    
    # 生成随机时间
    random_hour = random.randint(8, 22)
    random_minute = random.randint(0, 59)
    
    # 格式化为HTML datetime-local格式
    return random_date.strftime(f"%Y-%m-%dT{random_hour:02d}:{random_minute:02d}")

def auto_fill_lost_item_form(base_url="http://localhost:8080/LostAndFound_Platform"):
    """
    自动填写失物信息表单
    """
    # 配置Chrome浏览器选项
    chrome_options = Options()
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    
    # 如果需要无头模式（不显示浏览器窗口），取消下面这行注释
    # chrome_options.add_argument("--headless")
    
    try:
        # 初始化WebDriver（请根据您的Chrome版本调整chromedriver路径）
        driver = webdriver.Chrome(options=chrome_options)
        wait = WebDriverWait(driver, 10)
        
        # 访问登录页面
        driver.get(f"{base_url}/login.jsp")
        time.sleep(2)
        
        # 填写登录表单（使用默认测试账户）
        driver.find_element(By.ID, "username").send_keys("user1")
        driver.find_element(By.ID, "password").send_keys("user123")
        
        # 等待并获取验证码图片
        captcha_img = wait.until(EC.presence_of_element_located((By.ID, "captchaImage")))
        print("请手动输入验证码，程序将等待30秒...")
        
        # 点击验证码图片刷新验证码（如果需要）
        captcha_img.click()
        time.sleep(1)
        
        # 等待用户手动输入验证码
        time.sleep(30)
        
        # 提交登录表单
        driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
        time.sleep(3)
        
        # 检查是否登录成功
        if "login=success" in driver.current_url or "index.jsp" in driver.current_url:
            print("登录成功")
        else:
            print("登录失败，继续尝试访问发布页面...")
        
        # 访问发布失物信息页面
        driver.get(f"{base_url}/lost-items/new")
        time.sleep(3)
        
        # 填写表单
        # 标题（随机选择）
        title = random.choice(ITEM_NAMES)
        driver.find_element(By.ID, "title").send_keys(title)
        
        # 描述（基于标题生成）
        description = f"丢失了一个{title}，特征是{random.choice(['黑色的', '白色的', '红色的', '蓝色的', '崭新的', '旧的'])}"
        driver.find_element(By.ID, "description").send_keys(description)
        
        # 分类（随机选择）
        category_select = Select(driver.find_element(By.ID, "category"))
        category_select.select_by_visible_text(random.choice(CATEGORIES))
        
        # 丢失地点（随机选择）
        driver.find_element(By.ID, "lostLocation").send_keys(random.choice(LOCATIONS))
        
        # 丢失时间（随机生成）
        lost_time = generate_random_datetime()
        driver.find_element(By.ID, "lostTime").send_keys(lost_time)
        
        # 联系方式（随机选择）
        driver.find_element(By.ID, "contactInfo").send_keys(random.choice(CONTACT_INFOS))
        
        # 上传图片（如果有）
        photo_path = get_next_photo_path()
        if photo_path:
            driver.find_element(By.ID, "image").send_keys(os.path.abspath(photo_path))
            print(f"已选择图片: {photo_path}")
        else:
            print("未找到图片，跳过图片上传")
        
        # 提交表单
        driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
        time.sleep(3)
        
        # 检查是否提交成功
        if "lost-items" in driver.current_url:
            print("失物信息发布成功！")
        else:
            print("发布可能失败，请检查页面")
        
        # 等待几秒查看结果
        time.sleep(5)
        
    except Exception as e:
        print(f"发生错误: {e}")
    finally:
        # 关闭浏览器
        try:
            driver.quit()
        except:
            pass

if __name__ == "__main__":
    print("开始自动填写失物信息表单...")
    auto_fill_lost_item_form()
    print("程序执行完毕")