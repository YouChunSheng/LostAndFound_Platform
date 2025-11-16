package com.lostandfound.controller;

import com.lostandfound.model.FoundItem;
import com.lostandfound.model.User;
import com.lostandfound.service.FoundItemService;
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

@WebServlet("/found-items/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class FoundItemServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try (Connection connection = DatabaseConnection.getConnection()) {
            FoundItemService foundItemService = new FoundItemService(connection);

            if (pathInfo == null || pathInfo.equals("/")) {
                // Display all found items with search/filter support
                String keyword = request.getParameter("keyword");
                String category = request.getParameter("category");
                String location = request.getParameter("location");
                
                List<FoundItem> foundItems = foundItemService.searchFoundItems(keyword, category, location);
                request.setAttribute("foundItems", foundItems);
                request.getRequestDispatcher("/found-items.jsp").forward(request, response);
            } else if (pathInfo.equals("/new")) {
                // Show form to create new found item
                request.getRequestDispatcher("/new-found-item.jsp").forward(request, response);
            } else if (pathInfo.equals("/detail")) {
                // Show found item detail
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        FoundItem foundItem = foundItemService.getFoundItemById(id);
                        if (foundItem != null) {
                            request.setAttribute("foundItem", foundItem);
                            request.getRequestDispatcher("/found-item-detail.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Found item not found");
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
            FoundItemService foundItemService = new FoundItemService(connection);
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            if ("create".equals(action)) {
                // Create a new found item
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String category = request.getParameter("category");
                String foundLocation = request.getParameter("foundLocation");
                String foundTimeString = request.getParameter("foundTime");
                LocalDateTime foundTime = null;
                if (foundTimeString != null && !foundTimeString.isEmpty()) {
                    try {
                        foundTime = LocalDateTime.parse(foundTimeString.replace("T", " ")+":00");
                    } catch (Exception e) {
                        // Handle the exception or set to current time
                        foundTime = LocalDateTime.now();
                    }
                } else {
                    foundTime = LocalDateTime.now();
                }
                String contactInfo = request.getParameter("contactInfo");

                FoundItem foundItem = new FoundItem();
                foundItem.setUserId(currentUser.getId());
                foundItem.setTitle(title);
                foundItem.setDescription(description);
                foundItem.setCategory(category);
                foundItem.setFoundLocation(foundLocation);
                foundItem.setFoundTime(foundTime);
                foundItem.setContactInfo(contactInfo);
                
                // 处理图片上传
                Part imagePart = request.getPart("image");
                if (imagePart != null && imagePart.getSize() > 0) {
                    // 获取上传路径
                    String uploadPath = getServletContext().getRealPath("");
                    String imagePath = FileUploadUtil.saveImageFile(imagePart, uploadPath);
                    if (imagePath != null) {
                        foundItem.setImageUrl(imagePath);
                    }
                }

                boolean created = foundItemService.createFoundItem(foundItem);
                if (created) {
                    response.sendRedirect("found-items");
                } else {
                    response.sendRedirect("found-items/new?error=creation_failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("found-items/new?error=database_error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("found-items/new?error=upload_failed");
        }
    }
}