

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");

	        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jfs", "Dell", "Durga@593")) {
	            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setString(1, username);
	            stmt.setString(2, password);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                String role = rs.getString("role");
	                HttpSession session = request.getSession();
	                session.setAttribute("username", username);
	                session.setAttribute("role", role);

	                switch (role) {
	                    case "Employee":
	                        response.sendRedirect("requestAccess.jsp");
	                        break;
	                    case "Manager":
	                        response.sendRedirect("pendingRequests.jsp");
	                        break;
	                    case "Admin":
	                        response.sendRedirect("createSoftware.jsp");
	                        break;
	                }
	            } else {
	                response.sendRedirect("login.jsp?error=Invalid Credentials");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
