

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class SoftwareServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String name = request.getParameter("name");
	        String description = request.getParameter("description");
	        String[] accessLevels = request.getParameterValues("accessLevels");
	        String accessLevelStr = String.join(",", accessLevels);

	        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jfs", "postgres", "Durga@593")) {
	            String sql = "INSERT INTO software (name, description, access_levels) VALUES (?, ?, ?)";
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setString(1, name);
	            stmt.setString(2, description);
	            stmt.setString(3, accessLevelStr);
	            stmt.executeUpdate();
	            response.sendRedirect("createSoftware.jsp?success=1");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
