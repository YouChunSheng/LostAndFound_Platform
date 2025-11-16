package com.lostandfound.controller;

import com.lostandfound.model.User;
import com.lostandfound.service.UserService;
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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        User user = new User(username, password, email, phone);

        try (Connection connection = DatabaseConnection.getConnection()) {
            UserService userService = new UserService(connection);
            boolean registered = userService.registerUser(user);

            if (registered) {
                // Registration successful
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("index.jsp?registration=success");
            } else {
                // Username already exists
                response.sendRedirect("register.jsp?error=username_exists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=database_error");
        }
    }
}