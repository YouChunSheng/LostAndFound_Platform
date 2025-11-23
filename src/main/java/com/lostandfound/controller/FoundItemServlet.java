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
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String pathInfo = request.getPathInfo();

        try (Connection connection = DatabaseConnection.getConnection()) {
            FoundItemService foundItemService = new FoundItemService(connection);

            if (pathInfo == null || pathInfo.equals("/")) {
                // Display all found items with search/filter support
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
                
                List<FoundItem> foundItems = foundItemService.searchFoundItems(keyword, category, location, dateFrom, dateTo, page, pageSize);
                int totalItems = foundItemService.getTotalFoundItemsCount(keyword, category, location, dateFrom, dateTo);
                int totalPages = (int) Math.ceil((double) totalItems / pageSize);
                
                request.setAttribute("foundItems", foundItems);
                request.setAttribute("currentPage", page);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("totalItems", totalItems);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("keyword", keyword);
                request.setAttribute("category", category);
                request.setAttribute("location", location);
                request.setAttribute("dateFrom", dateFrom);
                request.setAttribute("dateTo", dateTo);
                
                request.getRequestDispatcher("/found-items.jsp").forward(request, response);
            } else if (pathInfo.equals("/new")) {
                // Show form to create new found item
                request.getRequestDispatcher("/new-found-item.jsp").forward(request, response);
            } else if (pathInfo.equals("/detail")) {
                // Show found item detail
                String idParam = request.getParameter("id");
                String action = request.getParameter("action");
                
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        FoundItem foundItem = foundItemService.getFoundItemById(id);
                        if (foundItem != null) {
                            // Handle specific actions
                            if ("edit".equals(action)) {
                                // Check if user is owner or admin
                                User currentUser = (User) request.getSession().getAttribute("user");
                                if (currentUser != null && (currentUser.getId() == foundItem.getUserId() || "admin".equals(currentUser.getRole()))) {
                                    request.setAttribute("foundItem", foundItem);
                                    request.getRequestDispatcher("/edit-found-item.jsp").forward(request, response);
                                    return;
                                } else {
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "您没有权限编辑此信息");
                                    return;
                                }
                            }
                            
                            // Show item detail
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
        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");

        try (Connection connection = DatabaseConnection.getConnection()) {
            FoundItemService foundItemService = new FoundItemService(connection);
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            if ("create".equals(action)) {
                // Create a new found item
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String category = request.getParameter("category");
                String foundLocation = request.getParameter("foundLocation");
                String foundTimeString = request.getParameter("foundTime");
                String contactInfo = request.getParameter("contactInfo");
                
                // 验证必填字段
                if (title == null || title.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/found-items/new?error=标题不能为空");
                    return;
                }
                
                if (description == null || description.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/found-items/new?error=描述不能为空");
                    return;
                }
                
                if (category == null || category.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/found-items/new?error=请选择分类");
                    return;
                }
                
                if (foundLocation == null || foundLocation.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/found-items/new?error=拾取地点不能为空");
                    return;
                }
                
                if (foundTimeString == null || foundTimeString.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/found-items/new?error=拾取时间不能为空");
                    return;
                }
                
                if (contactInfo == null || contactInfo.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/found-items/new?error=联系方式不能为空");
                    return;
                }

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

                FoundItem foundItem = new FoundItem();
                foundItem.setUserId(currentUser.getId());
                foundItem.setTitle(title.trim());
                foundItem.setDescription(description.trim());
                foundItem.setCategory(category.trim());
                foundItem.setFoundLocation(foundLocation.trim());
                foundItem.setFoundTime(foundTime);
                foundItem.setContactInfo(contactInfo.trim());
                
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
                    response.sendRedirect(request.getContextPath() + "/found-items?message=发布成功！");
                } else {
                    response.sendRedirect(request.getContextPath() + "/found-items/new?error=发布失败");
                }
            } else if ("delete".equals(action)) {
                // Delete a found item (only owner can delete)
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        boolean deleted = foundItemService.deleteFoundItemByIdAndUserId(id, currentUser.getId());
                        if (deleted) {
                            // Check if the user is admin and came from admin panel
                            String referer = request.getHeader("Referer");
                            if (referer != null && referer.contains("/admin/")) {
                                response.sendRedirect(request.getContextPath() + "/admin/found-items?message=删除成功");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/found-items?message=删除成功");
                            }
                        } else {
                            // Check if the user is admin and came from admin panel
                            String referer = request.getHeader("Referer");
                            if (referer != null && referer.contains("/admin/")) {
                                response.sendRedirect(request.getContextPath() + "/admin/found-items?error=删除失败，您可能不是该信息的所有者");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/found-items?error=删除失败，您可能不是该信息的所有者");
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Check if the user is admin and came from admin panel
                        String referer = request.getHeader("Referer");
                        if (referer != null && referer.contains("/admin/")) {
                            response.sendRedirect(request.getContextPath() + "/admin/found-items?error=无效的物品ID");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/found-items?error=无效的物品ID");
                        }
                    }
                } else {
                    // Check if the user is admin and came from admin panel
                    String referer = request.getHeader("Referer");
                    if (referer != null && referer.contains("/admin/")) {
                        response.sendRedirect(request.getContextPath() + "/admin/found-items?error=缺少物品ID");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/found-items?error=缺少物品ID");
                    }
                }
            } else if ("edit".equals(action)) {
                // Show edit form
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        FoundItem foundItem = foundItemService.getFoundItemById(id);
                        if (foundItem != null && foundItem.getUserId() == currentUser.getId()) {
                            request.setAttribute("foundItem", foundItem);
                            request.getRequestDispatcher("/edit-found-item.jsp").forward(request, response);
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
                // Update found item
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        FoundItem existingItem = foundItemService.getFoundItemById(id);
                        
                        if (existingItem != null && existingItem.getUserId() == currentUser.getId()) {
                            String title = request.getParameter("title");
                            String description = request.getParameter("description");
                            String category = request.getParameter("category");
                            String foundLocation = request.getParameter("foundLocation");
                            String foundTimeString = request.getParameter("foundTime");
                            String contactInfo = request.getParameter("contactInfo");
                            String currentImage = request.getParameter("currentImage");
                            
                            // 验证必填字段
                            if (title == null || title.trim().isEmpty()) {
                                response.sendRedirect(request.getContextPath() + "/found-items/detail?action=edit&id=" + id + "&error=标题不能为空");
                                return;
                            }
                            
                            if (description == null || description.trim().isEmpty()) {
                                response.sendRedirect(request.getContextPath() + "/found-items/detail?action=edit&id=" + id + "&error=描述不能为空");
                                return;
                            }
                            
                            if (category == null || category.trim().isEmpty()) {
                                response.sendRedirect(request.getContextPath() + "/found-items/detail?action=edit&id=" + id + "&error=请选择分类");
                                return;
                            }
                            
                            if (foundLocation == null || foundLocation.trim().isEmpty()) {
                                response.sendRedirect(request.getContextPath() + "/found-items/detail?action=edit&id=" + id + "&error=拾取地点不能为空");
                                return;
                            }
                            
                            if (foundTimeString == null || foundTimeString.trim().isEmpty()) {
                                response.sendRedirect(request.getContextPath() + "/found-items/detail?action=edit&id=" + id + "&error=拾取时间不能为空");
                                return;
                            }
                            
                            if (contactInfo == null || contactInfo.trim().isEmpty()) {
                                response.sendRedirect(request.getContextPath() + "/found-items/detail?action=edit&id=" + id + "&error=联系方式不能为空");
                                return;
                            }
                            
                            LocalDateTime foundTime = null;
                            if (foundTimeString != null && !foundTimeString.isEmpty()) {
                                try {
                                    foundTime = LocalDateTime.parse(foundTimeString.replace("T", " ")+":00");
                                } catch (Exception e) {
                                    foundTime = LocalDateTime.now();
                                }
                            } else {
                                foundTime = LocalDateTime.now();
                            }
                            
                            FoundItem foundItem = new FoundItem();
                            foundItem.setId(id);
                            foundItem.setUserId(currentUser.getId());
                            foundItem.setTitle(title.trim());
                            foundItem.setDescription(description.trim());
                            foundItem.setCategory(category.trim());
                            foundItem.setFoundLocation(foundLocation.trim());
                            foundItem.setFoundTime(foundTime);
                            foundItem.setContactInfo(contactInfo.trim());
                            foundItem.setImageUrl(currentImage); // Default to current image
                            
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
                                    foundItem.setImageUrl(imagePath);
                                }
                            }
                            
                            boolean updated = foundItemService.updateFoundItem(foundItem);
                            if (updated) {
                                response.sendRedirect(request.getContextPath() + "/found-items/detail?id=" + id + "&message=更新成功");
                            } else {
                                request.setAttribute("errorMessage", "更新失败");
                                request.setAttribute("foundItem", foundItem);
                                request.getRequestDispatcher("/edit-found-item.jsp").forward(request, response);
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
            } else if ("claim".equals(action)) {
                // Claim found item
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        FoundItem foundItem = foundItemService.getFoundItemById(id);
                        
                        if (foundItem != null && "unclaimed".equals(foundItem.getStatus())) {
                            // Update status to claimed
                            boolean updated = foundItemService.updateFoundItemStatus(id, "claimed");
                            if (updated) {
                                response.sendRedirect(request.getContextPath() + "/found-items?message=认领成功！");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/found-items?error=认领失败，请稍后再试");
                            }
                        } else if (foundItem != null && "claimed".equals(foundItem.getStatus())) {
                            response.sendRedirect(request.getContextPath() + "/found-items?error=该物品已被认领");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/found-items?error=招领信息未找到");
                        }
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/found-items?error=无效的物品ID");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/found-items?error=缺少物品ID");
                }
            } else if ("match".equals(action)) {
                // Match found item
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        FoundItem foundItem = foundItemService.getFoundItemById(id);
                        
                        if (foundItem != null && "unclaimed".equals(foundItem.getStatus())) {
                            // In a real application, you would save this match request to the database
                            // For now, we'll just redirect back to the found items list page with a success message
                            response.sendRedirect(request.getContextPath() + "/found-items?message=匹配申请已提交，拾主会收到通知");
                        } else if (foundItem != null && "claimed".equals(foundItem.getStatus())) {
                            response.sendRedirect(request.getContextPath() + "/found-items?error=该物品已被认领");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/found-items?error=招领信息未找到");
                        }
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/found-items?error=无效的物品ID");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/found-items?error=缺少物品ID");
                }
            } else if ("revokeClaim".equals(action)) {
                // Revoke claim for found item
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int id = Integer.parseInt(idParam);
                        FoundItem foundItem = foundItemService.getFoundItemById(id);
                        
                        if (foundItem != null && "claimed".equals(foundItem.getStatus())) {
                            // Update status to unclaimed
                            boolean updated = foundItemService.updateFoundItemStatus(id, "unclaimed");
                            if (updated) {
                                response.sendRedirect(request.getContextPath() + "/found-items?message=认领已撤销！");
                            } else {
                                response.sendRedirect(request.getContextPath() + "/found-items?error=撤销认领失败，请稍后再试");
                            }
                        } else if (foundItem != null && "unclaimed".equals(foundItem.getStatus())) {
                            response.sendRedirect(request.getContextPath() + "/found-items?error=该物品未被认领");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/found-items?error=招领信息未找到");
                        }
                    } catch (NumberFormatException e) {
                        response.sendRedirect(request.getContextPath() + "/found-items?error=无效的物品ID");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/found-items?error=缺少物品ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/found-items/new?error=database_error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/found-items/new?error=upload_failed");
        }
    }
}