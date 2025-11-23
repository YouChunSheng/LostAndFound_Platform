package com.lostandfound.controller;

import com.lostandfound.model.User;
import com.lostandfound.service.AdminService;
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
import java.util.Arrays;

@WebServlet("/admin/batch-delete-found-items")
public class BatchDeleteFoundItemsServlet extends HttpServlet {
    
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
        String[] itemIds = request.getParameterValues("itemIds");
        if (itemIds == null || itemIds.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"未选择任何项目\"}");
            return;
        }
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            AdminService adminService = new AdminService(connection);
            
            // 转换ID列表为整数数组
            int[] ids = Arrays.stream(itemIds)
                    .mapToInt(Integer::parseInt)
                    .toArray();
            
            // 执行批量删除
            boolean success = adminService.batchDeleteFoundItems(ids);
            
            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"删除成功\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"删除失败\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"数据库错误: " + e.getMessage() + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"无效的ID格式\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"未知错误: " + e.getMessage() + "\"}");
        }
    }
}