package in.pr.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/loginform")
public class Login extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String myname = req.getParameter("email");
        String mypass = req.getParameter("password");
        String userid=req.getParameter("userid");
        resp.setContentType("text/html");
        HttpSession session=req.getSession();
        session.setAttribute("myname", myname);
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/event", "root", "Ashish@123");
            
            // Prepare SQL statement
            PreparedStatement pStatement = connection.prepareStatement("SELECT * FROM register WHERE Email=? AND Password=?");
            pStatement.setString(1, myname);
            pStatement.setString(2, mypass);
            
            // Execute query and check if user exists
            ResultSet resultSet = pStatement.executeQuery();
            if (resultSet.next()) {
                // User exists, login successful
                
                out.println("Login successful!");
                // Redirect to a welcome or dashboard page
                RequestDispatcher rDispatcher = req.getRequestDispatcher("/dashboard.html");
                rDispatcher.forward(req, resp);
            } else {
                // User doesn't exist, login failed
                out.println("Invalid email or password.");
                // Redirect back to login page or an error page
                RequestDispatcher rDispatcher = req.getRequestDispatcher("/login.html");
                rDispatcher.include(req, resp);
            }
        } catch (ClassNotFoundException | SQLException e) {
        	  RequestDispatcher rDispatcher = req.getRequestDispatcher("/dashboard.html");
              rDispatcher.forward(req, resp);

            e.printStackTrace();
            out.println("An error occurred during login. Please try again later.");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);  // Forward GET requests to POST handler
    }
}