package com.lostandfound.controller;

import com.lostandfound.model.FoundItem;
import com.lostandfound.model.User;
import com.lostandfound.service.FoundItemService;
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
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/found-items/*")
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
                LocalDateTime foundTime = LocalDateTime.parse(foundTimeString);
                String contactInfo = request.getParameter("contactInfo");

                FoundItem foundItem = new FoundItem();
                foundItem.setUserId(currentUser.getId());
                foundItem.setTitle(title);
                foundItem.setDescription(description);
                foundItem.setCategory(category);
                foundItem.setFoundLocation(foundLocation);
                foundItem.setFoundTime(foundTime);
                foundItem.setContactInfo(contactInfo);
                // Image upload would be handled here in a real application

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
        }
    }
}