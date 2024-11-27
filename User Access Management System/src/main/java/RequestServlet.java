
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class RequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Get the current user session
	        HttpSession session = request.getSession();
	        String username = (String) session.getAttribute("username");
	        
	        // If the user is not logged in, redirect to login page
	        if (username == null) {
	            response.sendRedirect("login.jsp");
	            return;
	        }
	        
	        // Retrieve form parameters
	        String softwareId = request.getParameter("softwareId"); // ID of the requested software
	        String accessType = request.getParameter("accessType"); // Access type (Read, Write, Admin)
	        String reason = request.getParameter("reason");         // Reason for the request

	        try {
	            // Establish database connection
	            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jfs", "postgres", "Durga@593");

	            // Get the user ID from the username
	            String userQuery = "SELECT id FROM users WHERE username = ?";
	            PreparedStatement userStmt = conn.prepareStatement(userQuery);
	            userStmt.setString(1, username);
	            var userResult = userStmt.executeQuery();
	            int userId = 0;
	            if (userResult.next()) {
	                userId = userResult.getInt("id");
	            } else {
	                // User not found (shouldn't happen if the session is valid)
	                response.sendRedirect("error.jsp");
	                return;
	            }

	            // Insert the request into the requests table
	            String requestQuery = "INSERT INTO requests (user_id, software_id, access_type, reason, status) " +
	                                  "VALUES (?, ?, ?, ?, 'Pending')";
	            PreparedStatement requestStmt = conn.prepareStatement(requestQuery);
	            requestStmt.setInt(1, userId);
	            requestStmt.setInt(2, Integer.parseInt(softwareId));
	            requestStmt.setString(3, accessType);
	            requestStmt.setString(4, reason);
	            requestStmt.executeUpdate();

	            // Redirect to a confirmation page or back to the request form
	            response.sendRedirect("requestAccess.jsp?success=1");
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Redirect to an error page in case of failure
	            response.sendRedirect("error.jsp");
	        }
	    }
	}



