package com.tcs.printing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "status", urlPatterns = { "/status" })
public class status extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public status() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String machinename = (String) session.getAttribute("machineName");

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM jobs WHERE machine_name=? AND status=?");

            ps.setString(1, machinename);
            ps.setString(2, "Completed");

            ResultSet rs = ps.executeQuery();
            List<Record> records = new ArrayList<Record>();
            while (rs.next()) {
                Record record = new Record();
                record.setJobid(rs.getInt("job_id"));
                record.setJobName(rs.getString("job_details"));
                record.setJobDescription(rs.getString("job_description"));
                record.setPriority(rs.getString("priority"));
                record.setDueDate(rs.getDate("due_date"));
                record.setStatus(rs.getString("status"));
                
                records.add(record);
            }

            // Generate HTML table rows dynamically
            StringBuilder tableRows = new StringBuilder();
            if (records.isEmpty()) {
                tableRows.append("<tr><td colspan='6' style='text-align: center;'>No status records available.</td></tr>");
            } 
            else {
            		int count=1;
                for (Record record : records) {
                    tableRows.append("<tr>");
                    tableRows.append("<td>").append(count++).append("</td>");
                    tableRows.append("<td>").append(record.getJobName()).append("</td>");
                    tableRows.append("<td>").append(record.getJobDescription()).append("</td>");
                    tableRows.append("<td>").append(record.getPriority()).append("</td>");
                    tableRows.append("<td>").append(record.getDueDate()).append("</td>");
                    tableRows.append("<td>").append(record.getStatus()).append("</td>");
                    tableRows.append("</tr>");
                }
            } 
            response.setContentType("text/html");
            response.getWriter().write(tableRows.toString());
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle any POST requests here if needed.
    }
}
