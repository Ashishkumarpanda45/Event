package in.pr.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/registerservlet")
public class Register extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String myname = req.getParameter("email");
        String mypass = req.getParameter("password");
        
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/event", "root", "Ashish@123");
            
            // Prepare SQL statement
            PreparedStatement pStatement = con.prepareStatement("INSERT INTO register(Email, Password) VALUES(?, ?)");
            pStatement.setString(1, myname);
            pStatement.setString(2, mypass);
            
            // Execute update
            int count = pStatement.executeUpdate();
            
            if (count > 0) {
                out.println("User registration successful");
                RequestDispatcher rDispatcher = req.getRequestDispatcher("register.jsp");
                rDispatcher.include(req, resp);
            } else {
                out.println("User registration failed");
            }
        } catch (Exception e) {
        	  RequestDispatcher rDispatcher = req.getRequestDispatcher("login.html");
              
              rDispatcher.forward(req, resp);
            e.printStackTrace();
        }
    }
}
