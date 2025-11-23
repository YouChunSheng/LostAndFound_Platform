package com.lostandfound.controller;

import com.lostandfound.model.User;
import com.lostandfound.service.LostItemService;
import com.lostandfound.utils.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/admin/batch-delete-lost-items")
public class BatchDeleteLostItemsServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // 检查用户是否已登录且为管理员
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"success\": false, \"message\": \"您没有权限执行此操作\"}");
            return;
        }
        
        // 获取要删除的ID列表
        String[] ids = request.getParameterValues("ids");
        
        if (ids == null || ids.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"未选择任何要删除的项目\"}");
            return;
        }
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            LostItemService lostItemService = new LostItemService(connection);
            
            // 将字符串数组转换为整数数组
            int[] idArray = new int[ids.length];
            for (int i = 0; i < ids.length; i++) {
                try {
                    idArray[i] = Integer.parseInt(ids[i]);
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"success\": false, \"message\": \"无效的ID格式\"}");
                    return;
                }
            }
            
            // 执行批量删除
            int deletedCount = lostItemService.batchDeleteLostItemsByIds(idArray);
            
            response.getWriter().write(String.format(
                "{\"success\": true, \"message\": \"批量删除完成，成功删除%d项\"}", 
                deletedCount));
                
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"数据库错误，请稍后再试\"}");
        }
    }
}