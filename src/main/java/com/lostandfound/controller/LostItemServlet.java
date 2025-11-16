package com.lostandfound.controller;

import com.lostandfound.model.LostItem;
import com.lostandfound.model.User;
import com.lostandfound.service.LostItemService;
import com.lostandfound.utils.DatabaseConnection;
import com.lostandfound.utils.FileUploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/lost-items/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class LostItemServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try (Connection connection = DatabaseConnection.getConnection()) {
            LostItemService lostItemService = new LostItemService(connection);

            if (pathInfo == null || pathInfo.equals("/")) {
                // Display all lost items with search/filter support
                String keyword = request.getParameter("keyword");
                String category = request.getParameter("category");
                String location = request.getParameter("location");
                
                List<LostItem> lostItems = lostItemService.searchLostItems(keyword, category, location);
                request.setAttribute("lostItems", lostItems);
                request.getRequestDispatcher("/lost-items.jsp").forward(request, response);
            } else if (pathInfo.equals("/new")) {
                // Show form to create new lost item
                request.getRequestDispatcher("/new-lost-item.jsp").forward(request, response);
            } else if (pathInfo.equals("/detail")) {
                // Show lost item detail
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        LostItem lostItem = lostItemService.getLostItemById(id);
                        if (lostItem != null) {
                            request.setAttribute("lostItem", lostItem);
                            request.getRequestDispatcher("/lost-item-detail.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Lost item not found");
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing item ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection connection = DatabaseConnection.getConnection()) {
            LostItemService lostItemService = new LostItemService(connection);
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            if ("create".equals(action)) {
                // Create a new lost item
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String category = request.getParameter("category");
                String lostLocation = request.getParameter("lostLocation");
                String lostTimeString = request.getParameter("lostTime");
                LocalDateTime lostTime = LocalDateTime.parse(lostTimeString);
                String contactInfo = request.getParameter("contactInfo");

                LostItem lostItem = new LostItem();
                lostItem.setUserId(currentUser.getId());
                lostItem.setTitle(title);
                lostItem.setDescription(description);
                lostItem.setCategory(category);
                lostItem.setLostLocation(lostLocation);
                lostItem.setLostTime(lostTime);
                lostItem.setContactInfo(contactInfo);
                
                // 处理图片上传
                Part imagePart = request.getPart("image");
                if (imagePart != null && imagePart.getSize() > 0) {
                    // 获取上传路径
                    String uploadPath = getServletContext().getRealPath("");
                    String imagePath = FileUploadUtil.saveImageFile(imagePart, uploadPath);
                    if (imagePath != null) {
                        lostItem.setImageUrl(imagePath);
                    }
                }

                boolean created = lostItemService.createLostItem(lostItem);
                if (created) {
                    response.sendRedirect("lost-items");
                } else {
                    response.sendRedirect("lost-items/new?error=creation_failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("lost-items/new?error=database_error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("lost-items/new?error=upload_failed");
        }
    }
}