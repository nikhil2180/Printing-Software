package com.tcs.printing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.xdevapi.Result;

@WebServlet(name = "login", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super(); 
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
		String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Database Connection
        	Connection con=DatabaseConnection.getConnection();
            // Query to check credentials
            PreparedStatement pst = con.prepareStatement("SELECT machine_id, machine_name FROM machines WHERE machine_name=? AND machine_password=?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int machineId = rs.getInt("machine_id");
                String machineName=rs.getString("machine_name");

                HttpSession session=request.getSession();
                session.setAttribute("machineId", machineId);
                session.setAttribute("machineName", machineName);
                
                // Redirect to respective JSP based on machine ID
                switch (machineId) {
                case 1:
                case 2:
                case 3:
                    response.sendRedirect("machine"); 
                    break;
                case 4:
                    response.sendRedirect("manager.jsp");
                    break;
                case 5:
                	response.sendRedirect("admin.jsp");
                	break;
                default:
                    response.sendRedirect("login.jsp?error=InvalidMachine");
            }
            } else {
                response.sendRedirect("login.jsp?error=InvalidCredentials");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=ServerError");
        }
    }
}