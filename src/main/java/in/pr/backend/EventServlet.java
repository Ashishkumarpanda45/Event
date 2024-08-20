package in.pr.backend;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;

@WebServlet("/eventservlet")
public class EventServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if ("create".equals(action)) {
			
			createEvent(request, response);
			
		}
	}

	private void createEvent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String date = request.getParameter("date");
		String time = request.getParameter("time");
		String location = request.getParameter("location");
		String category = request.getParameter("category");

		// Check if userId exists in session
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userId") == null) {
			response.sendRedirect("login.html");
			return;
		}

		Integer hostId = (Integer) session.getAttribute("userId");

		try {
			// Load JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish database connection
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/event", "root", "Ashish@123");

			// Prepare SQL statement
			String sql = "INSERT INTO events (title, description, date, time, location, category, host_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);

			// Set parameters
			ps.setString(1, title);
			ps.setString(2, description);
			ps.setString(3, date);
			ps.setString(4, time);
			ps.setString(5, location);
			ps.setString(6, category);
			ps.setInt(7, hostId);

			// Execute the statement
			int result = ps.executeUpdate();
			if (result > 0) {
				response.sendRedirect("dashboard.html");
			} else {
				response.getWriter().println("Failed to create event. Please try again.");
			}

			// Close the connection
			ps.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().println("SQL Error occurred while inserting data: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("Unexpected error occurred: " + e.getMessage());
		}
	}
}
