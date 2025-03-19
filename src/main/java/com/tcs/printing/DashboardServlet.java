package com.tcs.printing;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dashboard", urlPatterns = { "/dashboard" })
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public DashboardServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String machineName = request.getParameter("machines");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM jobs WHERE machine_name=?");
            ps.setString(1, machineName);
            ResultSet rs = ps.executeQuery();
            
            StringBuilder htmlResponse = new StringBuilder();    //basically strings are immutable thats why we use string builder because it is mutable 
            htmlResponse.append("<html><head><title>Machine Details</title></head><body>");
            htmlResponse.append("<h2>Machine Details</h2>");
            htmlResponse.append("<table border='1'><tr><th>Job ID</th><th>Company</th><th>Client Name</th><th>Mobile</th><th>Job Details</th><th>Job Description</th><th>Sub Machines</th><th>Priority</th></tr>");
            
            while (rs.next()) {
                htmlResponse.append("<tr>");
                htmlResponse.append("<td>" + rs.getInt("job_id") + "</td>");
                //htmlResponse.append("<td>" + rs.getInt("machine_id") + "</td>");
                htmlResponse.append("<td>" + rs.getString("company") + "</td>");
                htmlResponse.append("<td>" + rs.getString("client_name") + "</td>");
                htmlResponse.append("<td>" + rs.getString("mobile") + "</td>");
                htmlResponse.append("<td>" + rs.getString("job_details") + "</td>");
                htmlResponse.append("<td>" + rs.getString("job_description") + "</td>");
                htmlResponse.append("<td>" + rs.getString("sub_machines") + "</td>");
                htmlResponse.append("<td>" + rs.getString("priority") + "</td>");
                htmlResponse.append("</tr>");
            }
            
            htmlResponse.append("</table>");
            htmlResponse.append("</body></html>");
            
            response.getWriter().write(htmlResponse.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("<html><body><h3>Error fetching data</h3></body></html>");
        }
    }
}