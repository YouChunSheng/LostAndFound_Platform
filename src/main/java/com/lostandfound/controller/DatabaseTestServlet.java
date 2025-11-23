package com.lostandfound.controller;

import com.lostandfound.utils.DatabaseConnection;
import com.lostandfound.dao.LostItemDAO;
import com.lostandfound.dao.FoundItemDAO;
import com.lostandfound.model.LostItem;
import com.lostandfound.model.FoundItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

@WebServlet("/test-database")
public class DatabaseTestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h2>Database Connection Test</h2>");
        
        try {
            // Test database connection
            out.println("<h3>Testing database connection...</h3>");
            Connection connection = DatabaseConnection.getConnection();
            out.println("<p>Database connection: <span style='color:green'>SUCCESS</span></p>");
            
            // Test LostItemDAO
            out.println("<h3>Testing LostItemDAO...</h3>");
            LostItemDAO lostItemDAO = new LostItemDAO(connection);
            List<LostItem> lostItems = lostItemDAO.getAllLostItems();
            out.println("<p>Lost items count: " + lostItems.size() + "</p>");
            out.println("<p>LostItemDAO: <span style='color:green'>SUCCESS</span></p>");
            
            // Test FoundItemDAO
            out.println("<h3>Testing FoundItemDAO...</h3>");
            FoundItemDAO foundItemDAO = new FoundItemDAO(connection);
            List<FoundItem> foundItems = foundItemDAO.getAllFoundItems();
            out.println("<p>Found items count: " + foundItems.size() + "</p>");
            out.println("<p>FoundItemDAO: <span style='color:green'>SUCCESS</span></p>");
            
            connection.close();
        } catch (Exception e) {
            out.println("<p style='color:red'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        out.println("</body></html>");
    }
}