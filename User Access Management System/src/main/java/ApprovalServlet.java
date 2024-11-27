

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class ApprovalServlet
 */
public class ApprovalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String requestId = request.getParameter("requestId");
	        String action = request.getParameter("action");

	        String status = "Pending";
	        if ("Approve".equalsIgnoreCase(action)) {
	            status = "Approved";
	        } else if ("Reject".equalsIgnoreCase(action)) {
	            status = "Rejected";
	        }

	        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jfs", "postgres", "Durga@593")) {
	            String sql = "UPDATE requests SET status = ? WHERE id = ?";
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setString(1, status);
	            stmt.setInt(2, Integer.parseInt(requestId));
	            stmt.executeUpdate();

	            response.sendRedirect("pendingRequests.jsp");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
