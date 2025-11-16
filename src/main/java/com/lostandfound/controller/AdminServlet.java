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

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Check if user is logged in and is admin
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            AdminService adminService = new AdminService(connection);

            if (pathInfo == null || pathInfo.equals("/")) {
                // Admin dashboard
                request.setAttribute("userCount", 0); // Would be implemented in real app
                request.setAttribute("lostItemCount", adminService.getAllLostItems().size());
                request.setAttribute("foundItemCount", adminService.getAllFoundItems().size());
                request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
            } else if (pathInfo.equals("/users")) {
                // Manage users
                // request.setAttribute("users", adminService.getAllUsers());
                request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
            } else if (pathInfo.equals("/lost-items")) {
                // Manage lost items
                request.setAttribute("lostItems", adminService.getAllLostItems());
                request.getRequestDispatcher("/admin/lost-items.jsp").forward(request, response);
            } else if (pathInfo.equals("/found-items")) {
                // Manage found items
                request.setAttribute("foundItems", adminService.getAllFoundItems());
                request.getRequestDispatcher("/admin/found-items.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String pathInfo = request.getPathInfo();

        // Check if user is logged in and is admin
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            AdminService adminService = new AdminService(connection);

            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                
                if (pathInfo != null && pathInfo.equals("/lost-items")) {
                    // Delete lost item
                    adminService.deleteLostItem(id);
                    response.sendRedirect(request.getContextPath() + "/admin/lost-items");
                } else if (pathInfo != null && pathInfo.equals("/found-items")) {
                    // Delete found item
                    adminService.deleteFoundItem(id);
                    response.sendRedirect(request.getContextPath() + "/admin/found-items");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
        }
    }
}