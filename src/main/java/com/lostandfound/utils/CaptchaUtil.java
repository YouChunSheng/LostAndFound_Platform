package com.lostandfound.utils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class CaptchaUtil {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    /**
     * 生成验证码图片并写入响应
     */
    public static void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // 创建图片缓冲区
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        
        // 设置背景色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 生成随机验证码
        Random random = new Random();
        StringBuilder captchaCode = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            char ch = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            captchaCode.append(ch);
        }
        
        // 将验证码存储到会话中
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captchaCode.toString());
        
        // 绘制验证码文本
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 24));
        
        // 计算文本位置使其居中
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = (WIDTH - fontMetrics.stringWidth(captchaCode.toString())) / 2;
        int y = (HEIGHT - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        
        graphics.drawString(captchaCode.toString(), x, y);
        
        // 添加干扰线
        graphics.setColor(Color.GRAY);
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            graphics.drawLine(x1, y1, x2, y2);
        }
        
        // 添加干扰点
        graphics.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 20; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            graphics.drawOval(x1, y1, 1, 1);
        }
        
        graphics.dispose();
        
        // 输出图片到响应
        ImageIO.write(bufferedImage, "png", response.getOutputStream());
    }
    
    /**
     * 验证用户输入的验证码是否正确
     */
    public static boolean validateCaptcha(HttpServletRequest request, String userInput) {
        HttpSession session = request.getSession();
        String captcha = (String) session.getAttribute("captcha");
        
        // 移除已使用的验证码
        session.removeAttribute("captcha");
        
        // 检查验证码是否正确（忽略大小写）
        return captcha != null && captcha.equalsIgnoreCase(userInput);
    }
}