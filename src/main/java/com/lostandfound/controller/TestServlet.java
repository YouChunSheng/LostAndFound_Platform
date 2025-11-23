package com.lostandfound.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h2>Test Servlet</h2>");
        out.println("<p>Test servlet is working correctly.</p>");
        
        // Check session
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        out.println("<p>Session user: " + (user != null ? user.toString() : "null") + "</p>");
        
        out.println("</body></html>");
    }
}