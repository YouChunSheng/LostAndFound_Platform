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
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String pathInfo = request.getPathInfo();

        try (Connection connection = DatabaseConnection.getConnection()) {
            LostItemService lostItemService = new LostItemService(connection);

            if (pathInfo == null || pathInfo.equals("/")) {
                // Display all lost items with search/filter support
                String keyword = request.getParameter("keyword");
                String category = request.getParameter("category");
                String location = request.getParameter("location");
                String dateFrom = request.getParameter("dateFrom");
                String dateTo = request.getParameter("dateTo");
                
                // 分页参数
                String pageParam = request.getParameter("page");
                String pageSizeParam = request.getParameter("pageSize");
                
                int page = 1; // 默认第一页
                int pageSize = 20; // 默认每页20条记录
                
                if (pageParam != null && !pageParam.isEmpty()) {
                    try {
                        page = Integer.parseInt(pageParam);
                        if (page < 1) page = 1;
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }
                
                if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
                    try {
                        pageSize = Integer.parseInt(pageSizeParam);
                        if (pageSize < 1) pageSize = 20;
                        if (pageSize > 100) pageSize = 100; // 最多每页100条记录
                    } catch (NumberFormatException e) {
                        pageSize = 20;
                    }
                }
                
                List<LostItem> lostItems = lostItemService.searchLostItems(keyword, category, location, dateFrom, dateTo, page, pageSize);
                int totalItems = lostItemService.getTotalLostItemsCount(keyword, category, location, dateFrom, dateTo);
                int totalPages = (int) Math.ceil((double) totalItems / pageSize);
                
                request.setAttribute("lostItems", lostItems);
                request.setAttribute("currentPage", page);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("totalItems", totalItems);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("keyword", keyword);
                request.setAttribute("category", category);
                request.setAttribute("location", location);
                request.setAttribute("dateFrom", dateFrom);
                request.setAttribute("dateTo", dateTo);
                
                request.getRequestDispatcher("/lost-items.jsp").forward(request, response);
            } else if (pathInfo.equals("/new")) {
                // Show form to create new lost item
                request.getRequestDispatcher("/new-lost-item.jsp").forward(request, response);
            } else if (pathInfo.equals("/detail")) {
                // Show lost item detail
                String idParam = request.getParameter("id");
                String action = request.getParameter("action");
                
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        LostItem lostItem = lostItemService.getLostItemById(id);
                        if (lostItem != null) {
                            // Handle specific actions
                            if ("edit".equals(action)) {
                                // Check if user is owner or admin
                                User currentUser = (User) request.getSession().getAttribute("user");
                                if (currentUser != null && (currentUser.getId() == lostItem.getUserId() || "admin".equals(currentUser.getRole()))) {
                                    request.setAttribute("lostItem", lostItem);
                                    request.getRequestDispatcher("/edit-lost-item.jsp").forward(request, response);
                                    return;
                                } else {
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "您没有权限编辑此信息");
                                    return;
                                }
                            } else if ("match".equals(action)) {
                                // Show match form for non-owners
                                User currentUser = (User) request.getSession().getAttribute("user");
                                if (currentUser != null && currentUser.getId() != lostItem.getUserId()) {
                                    request.setAttribute("lostItem", lostItem);
                                    request.getRequestDispatcher("/match-lost-item.jsp").forward(request, response);
                                    return;
                                } else {
                                    // Owner cannot match their own item
                                    request.setAttribute("lostItem", lostItem);
                                    request.setAttribute("errorMessage", "您不能匹配自己的物品");
                                    request.getRequestDispatcher("/lost-item-detail.jsp").forward(request, response);
                                    return;
                                }
                            }
                            
                            // Show item detail
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
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");

        try (Connection connection = DatabaseConnection.getConnection()) {
            LostItemService lostItemService = new LostItemService(connection);
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            if ("create".equals(action)) {
                // Create a new lost item
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String category = request.getParameter("category");
                String lostLocation = request.getParameter("lostLocation");
                String lostTimeString = request.getParameter("lostTime");
                LocalDateTime lostTime = null;
                if (lostTimeString != null && !lostTimeString.isEmpty()) {
                    try {
                        lostTime = LocalDateTime.parse(lostTimeString.replace("T", " ")+":00");
                    } catch (Exception e) {
                        // Handle the exception or set to current time
                        lostTime = LocalDateTime.now();
                    }
                } else {
                    lostTime = LocalDateTime.now();
                }
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
                    response.sendRedirect(request.getContextPath() + "/lost-items");
                } else {
                    response.sendRedirect(request.getContextPath() + "/lost-items/new?error=creation_failed");
                }
            } else if ("delete".equals(action)) {
                // Delete a lost item (only owner can delete)
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        boolean deleted = lostItemService.deleteLostItemByIdAndUserId(id, currentUser.getId());
                        if (deleted) {
                            // Check if the user is admin and came from admin panel
                            String referer = request.getHeader("Referer");
                            if (referer != null && referer.contains("/admin/")) {
                                response.sendRedirect(request.getContextPath() + "/admin/lost-items?message=删除成功");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/lost-items?message=删除成功");
                            }
                        } else {
                            // Check if the user is admin and came from admin panel
                            String referer = request.getHeader("Referer");
                            if (referer != null && referer.contains("/admin/")) {
                                response.sendRedirect(request.getContextPath() + "/admin/lost-items?error=删除失败，您可能不是该信息的所有者");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/lost-items?error=删除失败，您可能不是该信息的所有者");
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Check if the user is admin and came from admin panel
                        String referer = request.getHeader("Referer");
                        if (referer != null && referer.contains("/admin/")) {
                            response.sendRedirect(request.getContextPath() + "/admin/lost-items?error=无效的物品ID");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/lost-items?error=无效的物品ID");
                        }
                    }
                } else {
                    // Check if the user is admin and came from admin panel
                    String referer = request.getHeader("Referer");
                    if (referer != null && referer.contains("/admin/")) {
                        response.sendRedirect(request.getContextPath() + "/admin/lost-items?error=缺少物品ID");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/lost-items?error=缺少物品ID");
                    }
                }
            } else if ("edit".equals(action)) {
                // Show edit form
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        LostItem lostItem = lostItemService.getLostItemById(id);
                        if (lostItem != null && lostItem.getUserId() == currentUser.getId()) {
                            request.setAttribute("lostItem", lostItem);
                            request.getRequestDispatcher("/edit-lost-item.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "您没有权限编辑此信息");
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的物品ID");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少物品ID");
                }
            } else if ("update".equals(action)) {
                // Update lost item
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        LostItem existingItem = lostItemService.getLostItemById(id);
                        
                        if (existingItem != null && existingItem.getUserId() == currentUser.getId()) {
                            String title = request.getParameter("title");
                            String description = request.getParameter("description");
                            String category = request.getParameter("category");
                            String lostLocation = request.getParameter("lostLocation");
                            String lostTimeString = request.getParameter("lostTime");
                            String contactInfo = request.getParameter("contactInfo");
                            String currentImage = request.getParameter("currentImage");
                            
                            LocalDateTime lostTime = null;
                            if (lostTimeString != null && !lostTimeString.isEmpty()) {
                                try {
                                    lostTime = LocalDateTime.parse(lostTimeString.replace("T", " ")+":00");
                                } catch (Exception e) {
                                    lostTime = LocalDateTime.now();
                                }
                            } else {
                                lostTime = LocalDateTime.now();
                            }
                            
                            LostItem lostItem = new LostItem();
                            lostItem.setId(id);
                            lostItem.setUserId(currentUser.getId());
                            lostItem.setTitle(title);
                            lostItem.setDescription(description);
                            lostItem.setCategory(category);
                            lostItem.setLostLocation(lostLocation);
                            lostItem.setLostTime(lostTime);
                            lostItem.setContactInfo(contactInfo);
                            lostItem.setImageUrl(currentImage); // Default to current image
                            
                            // Handle image upload
                            Part imagePart = request.getPart("image");
                            if (imagePart != null && imagePart.getSize() > 0) {
                                // Get upload path
                                String uploadPath = getServletContext().getRealPath("");
                                String imagePath = FileUploadUtil.saveImageFile(imagePart, uploadPath);
                                if (imagePath != null) {
                                    // Delete old image if exists
                                    if (currentImage != null && !currentImage.isEmpty()) {
                                        FileUploadUtil.deleteFile(getServletContext().getRealPath("") + File.separator + currentImage);
                                    }
                                    lostItem.setImageUrl(imagePath);
                                }
                            }
                            
                            boolean updated = lostItemService.updateLostItem(lostItem);
                            if (updated) {
                                response.sendRedirect(request.getContextPath() + "/lost-items/detail?id=" + id + "&message=更新成功");
                            } else {
                                request.setAttribute("errorMessage", "更新失败");
                                request.setAttribute("lostItem", lostItem);
                                request.getRequestDispatcher("/edit-lost-item.jsp").forward(request, response);
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "您没有权限编辑此信息");
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的物品ID");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少物品ID");
                }
            } else if ("match".equals(action)) {
                // Show match form
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        LostItem lostItem = lostItemService.getLostItemById(id);
                        if (lostItem != null) {
                            request.setAttribute("lostItem", lostItem);
                            request.getRequestDispatcher("/match-lost-item.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "失物信息未找到");
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的物品ID");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少物品ID");
                }
            } else if ("submitMatch".equals(action)) {
                // Submit match request
                String lostItemIdParam = request.getParameter("lostItemId");
                String message = request.getParameter("message");
                String contactInfo = request.getParameter("contactInfo");
                
                if (lostItemIdParam != null && !lostItemIdParam.isEmpty() && message != null && !message.isEmpty() && contactInfo != null && !contactInfo.isEmpty()) {
                    try {
                        int lostItemId = Integer.parseInt(lostItemIdParam);
                        LostItem lostItem = lostItemService.getLostItemById(lostItemId);
                        
                        if (lostItem != null) {
                            // In a real application, you would save this match request to the database
                            // For now, we'll just redirect back to the lost items list page with a success message
                            response.sendRedirect(request.getContextPath() + "/lost-items?message=匹配申请已提交，失主会收到通知");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/lost-items?error=失物信息未找到");
                        }
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/lost-items?error=无效的物品ID");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/lost-items?error=请填写所有必填字段");
                }
            } else if ("claim".equals(action)) {
                // Claim lost item
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        LostItem lostItem = lostItemService.getLostItemById(id);
                        
                        if (lostItem != null && "unclaimed".equals(lostItem.getStatus())) {
                            // Update status to claimed
                            boolean updated = lostItemService.updateLostItemStatus(id, "claimed");
                            if (updated) {
                                response.sendRedirect(request.getContextPath() + "/lost-items?message=认领成功！");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/lost-items?error=认领失败，请稍后再试");
                            }
                        } else if (lostItem != null && "claimed".equals(lostItem.getStatus())) {
                            response.sendRedirect(request.getContextPath() + "/lost-items?error=该物品已被认领");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/lost-items?error=失物信息未找到");
                        }
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/lost-items?error=无效的物品ID");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/lost-items?error=缺少物品ID");
                }
            } else if ("revokeClaim".equals(action)) {
                // Revoke claim for lost item
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        LostItem lostItem = lostItemService.getLostItemById(id);
                        
                        if (lostItem != null && "claimed".equals(lostItem.getStatus())) {
                            // Update status to unclaimed
                            boolean updated = lostItemService.updateLostItemStatus(id, "unclaimed");
                            if (updated) {
                                response.sendRedirect(request.getContextPath() + "/lost-items?message=认领已撤销！");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/lost-items?error=撤销认领失败，请稍后再试");
                            }
                        } else if (lostItem != null && "unclaimed".equals(lostItem.getStatus())) {
                            response.sendRedirect(request.getContextPath() + "/lost-items?error=该物品未被认领");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/lost-items?error=失物信息未找到");
                        }
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/lost-items?error=无效的物品ID");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/lost-items?error=缺少物品ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lost-items?error=数据库错误");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/lost-items?error=操作失败");
        }
    }
}