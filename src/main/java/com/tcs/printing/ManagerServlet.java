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

@WebServlet(name = "manager", urlPatterns = { "/manager" })
public class ManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ManagerServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MachineServlet Called");

        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userid");

        if (userId == 0) {
            response.sendRedirect("login.jsp?error=NoSession");
            return;
        }

        System.out.println("Session User ID: " + userId);

        try {
            // Get the machine_id for the given user_id
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement psMachine = con.prepareStatement("SELECT machine_id FROM machines WHERE user_id = ?");
            psMachine.setInt(1, userId);
            ResultSet rsMachine = psMachine.executeQuery();

            if (rsMachine.next()) {
                int machineId = rsMachine.getInt("machine_id");

                // Fetch jobs for this machine_id
                PreparedStatement psJobs = con.prepareStatement("SELECT * FROM jobs WHERE machine_id = ? AND status = ?");
                psJobs.setInt(1, machineId);
                psJobs.setString(2, "Pending");

                ResultSet rsJobs = psJobs.executeQuery();
                List<Record> records = new ArrayList<>();

                while (rsJobs.next()) {
                    Record record = new Record();
                    record.setJobid(rsJobs.getInt("job_id"));
                    record.setJobName(rsJobs.getString("job_details"));
                    record.setJobDescription(rsJobs.getString("job_description"));
                    record.setPriority(rsJobs.getString("priority"));
                    record.setDueDate(rsJobs.getDate("due_date"));

                    records.add(record);
                }

                System.out.println("Total Jobs Retrieved: " + records.size());
                request.setAttribute("jobs", records);

                // Forward to a JSP file (based on user_id/machine_id)
                request.getRequestDispatcher("jobs.jsp").forward(request, response);
            } else {
                response.sendRedirect("login.jsp?error=NoMachineFound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String jobName = request.getParameter("jobName");
        String jobDescription = request.getParameter("jobDescription");
        String machine = request.getParameter("machine");
        String priority = request.getParameter("priority");
        String dueDate = request.getParameter("dueDate");
        String subMachine=request.getParameter("subMachine");
        String mobile=request.getParameter("mobile");
        String client=request.getParameter("client");
        String company=request.getParameter("company");

        HttpSession session = request.getSession();
        int machineId = (int) session.getAttribute("machineId");

        // Check if the machine selected is "Machine One"
        if ("machineOne".equalsIgnoreCase(machine)) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO jobs (machine_id, job_details, job_description, machine_name, priority, due_date, sub_machines, client_name, company, mobile) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                ps.setInt(1, machineId);
                ps.setString(2, jobName);
                ps.setString(3, jobDescription);
                ps.setString(4, machine);
                ps.setString(5, priority);
                ps.setString(6, dueDate);
                ps.setString(7, subMachine);
                ps.setString(8, client);
                ps.setString(9, company);
                ps.setString(10, mobile);

                int rowsUpdated = ps.executeUpdate();  

                if (rowsUpdated > 0) {
                    response.getWriter().write("Job assigned successfully to Machine One.");
                } else {
                    response.getWriter().write("Failed to assign job.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("Error while assigning job.");
            }
        } 
        else if("machineTwo".equalsIgnoreCase(machine))
        {
        	try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO jobs (machine_id, job_details, job_description, machine_name, priority, due_date, sub_machines, client_name, company, mobile) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                ps.setInt(1, machineId);
                ps.setString(2, jobName);
                ps.setString(3, jobDescription);
                ps.setString(4, machine);
                ps.setString(5, priority);
                ps.setString(6, dueDate);
                ps.setString(7, subMachine);
                ps.setString(8, client);
                ps.setString(9, company);
                ps.setString(10, mobile);

                int rowsUpdated = ps.executeUpdate();  

                if (rowsUpdated > 0) {
                    response.getWriter().write("Job assigned successfully to Machine Two.");
                } else {
                    response.getWriter().write("Failed to assign job.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("Error while assigning job.");
            }
        }
        else {
        	try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO jobs (machine_id, job_details, job_description, machine_name, priority, due_date, sub_machines, client_name, company, mobile) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                ps.setInt(1, machineId);
                ps.setString(2, jobName);
                ps.setString(3, jobDescription);
                ps.setString(4, machine);
                ps.setString(5, priority);
                ps.setString(6, dueDate);
                ps.setString(7, subMachine);
                ps.setString(8, client);
                ps.setString(9, company);
                ps.setString(10, mobile);

                int rowsUpdated = ps.executeUpdate();  

                if (rowsUpdated > 0) {
                    response.getWriter().write("Job assigned successfully to Machine Three.");
                } else {
                    response.getWriter().write("Failed to assign job.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("Error while assigning job.");
            }
        }
    }

}
