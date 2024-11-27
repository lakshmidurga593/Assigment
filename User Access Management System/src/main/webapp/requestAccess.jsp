<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="jakarta.servlet.http.HttpServlet" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html>
<head>
    <title>Request Software Access</title>
</head>
<body>
    <h2>Request Software Access</h2>

    <%-- Check if the user is logged in and is an Employee --%>
    <%
        HttpSession s = request.getSession();
        String username = (String) s.getAttribute("username");
        String role = (String) s.getAttribute("role");

        if (username == null || !"Employee".equals(role)) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>

    <%-- Display success message if the request was submitted successfully --%>
    <%
        String success = request.getParameter("success");
        if ("1".equals(success)) {
    %>
        <p style="color: green;">Your access request has been submitted successfully!</p>
    <% } %>

    <%-- Fetch software options from the database --%>
    <%
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to the PostgreSQL database
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jfs", "postgres", "Durga@593");

            // Fetch available software
            String sql = "SELECT id, name FROM software";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
    %>
    
    <%-- Request Access Form --%>
    <form action="RequestServlet" method="post">
        <label for="software">Software:</label>
        <select id="software" name="softwareId" required>
            <% while (rs.next()) { %>
                <option value="<%= rs.getInt("id") %>"><%= rs.getString("name") %></option>
            <% } %>
        </select><br>

        <label for="accessType">Access Type:</label>
        <select id="accessType" name="accessType" required>
            <option value="Read">Read</option>
            <option value="Write">Write</option>
            <option value="Admin">Admin</option>
        </select><br>

        <label for="reason">Reason:</label>
        <textarea id="reason" name="reason" required></textarea><br>

        <button type="submit">Submit Request</button>
    </form>
    
    <% 
        } catch (Exception e) { 
            e.printStackTrace(); 
    %>
        <p style="color: red;">Failed to load software options. Please try again later.</p>
    <% 
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    %>
</body>
</html>
    