<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="java.sql.Connection" %>
 <%@ page import="java.sql.DriverManager" %>
 <%@ page import="java.sql.PreparedStatement" %>
  <%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pending Access Requests</title>
</head>
<body>
    <h2>Pending Access Requests</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Request ID</th>
                <th>Employee Name</th>
                <th>Software Name</th>
                <th>Access Type</th>
                <th>Reason</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% 
                String username = (String) session.getAttribute("username");
                if (username == null || !"Manager".equals(session.getAttribute("role"))) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jfs", "postgres", "Durga@593");
                String sql = "SELECT r.id, u.username AS employee_name, s.name AS software_name, r.access_type, r.reason " +
                             "FROM requests r " +
                             "JOIN users u ON r.user_id = u.id " +
                             "JOIN software s ON r.software_id = s.id " +
                             "WHERE r.status = 'Pending'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getInt("id") %></td>
                <td><%= rs.getString("employee_name") %></td>
                <td><%= rs.getString("software_name") %></td>
                <td><%= rs.getString("access_type") %></td>
                <td><%= rs.getString("reason") %></td>
                <td>
                    <form action="ApprovalServlet" method="post" style="display:inline;">
                        <input type="hidden" name="requestId" value="<%= rs.getInt("id") %>">
                        <button type="submit" name="action" value="Approve">Approve</button>
                        <button type="submit" name="action" value="Reject">Reject</button>
                    </form>
                </td>
            </tr>
            <% } conn.close(); %>
        </tbody>
    </table>
</body>
</html>
    