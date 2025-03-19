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
import com.tcs.printing.DatabaseConnection;

@WebServlet(name = "machine", urlPatterns = { "/machine" })
public class MachineServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MachineServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MachineServlet Called");

        HttpSession session = request.getSession();
        String machineName = (String) session.getAttribute("machineName");

        if (machineName == null) {
            response.sendRedirect("login.jsp?error=NoSession");
            return;
        }

        System.out.println("Session Machine Name: " + machineName);

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM jobs WHERE machine_name = ? AND status = ?");
            
            ps.setString(1, machineName);
            ps.setString(2, "Pending");

            System.out.println("Executing Query: SELECT * FROM jobs WHERE machine_name = '" + machineName + "' AND status = 'Pending'");

            ResultSet rs = ps.executeQuery();
            List<Record> records = new ArrayList<>();

            while (rs.next()) {
                System.out.println("Job Found: " + rs.getInt("job_id") + " - " + rs.getString("job_details"));

                Record record = new Record();
                record.setJobid(rs.getInt("job_id"));
                record.setJobName(rs.getString("job_details"));
                record.setJobDescription(rs.getString("job_description"));
                record.setPriority(rs.getString("priority"));
                record.setDueDate(rs.getDate("due_date"));

                records.add(record);
            }

            System.out.println("Total Jobs Retrieved: " + records.size());
            request.setAttribute("machineOne", records);
            request.setAttribute("machineTwo", records);
            request.setAttribute("machineThree", records);
            
            // Redirect dynamically to the respective JSP based on machine name
            request.getRequestDispatcher(machineName + ".jsp").forward(request, response);

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String machineName = (String) session.getAttribute("machineName"); // Get machineName from session
        String jobId = request.getParameter("jobId"); // Get Job ID from form submission

        if (machineName == null) {
            response.sendRedirect("login.jsp?error=Unauthorized");
            return;
        }

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE jobs SET status=? WHERE job_id=? AND machine_name=?");

            ps.setString(1, "Completed");
            ps.setString(2, jobId);
            ps.setString(3, machineName);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Job status updated successfully for Job ID: " + jobId);
            } else {
                System.out.println("No matching job found or already updated.");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(machineName + ".jsp");
    }
    }