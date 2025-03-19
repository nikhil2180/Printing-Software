<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tcs.printing.Record" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manager Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            height: 100vh;
        }

        /* Sidebar Styles */
        .sidebar {
            width: 200px;
            background-color: #333;
            color: #fff;
            padding-top: 20px;
        }

        .sidebar button {
            width: 100%;
            padding: 15px;
            background: #333;
            border: none;
            color: #fff;
            text-align: left;
            cursor: pointer;
            font-size: 16px;
            display: block;
        }

        .sidebar button:hover, .sidebar button.active {
            background-color: #575757;
        }

        .sidebar h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        /* Main Content Area */
        .content {
            flex-grow: 1;
            padding: 20px;
        }

        .panel {
            display: none;
        }

        .panel.active {
            display: block;
        }

        /* Dashboard Styling */
        .dashboard {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 90%;
            margin: 20px auto;
        }

        .dashboard h2 {
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: center;
        }

        th {
            background-color: #333;
			    color: white;
			    text-align: center;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .action-btn {
            background-color: #28a745;
            border: none;
            color: white;
            padding: 8px 12px;
            border-radius: 5px;
            cursor: pointer;
        }

        .action-btn:hover {
            background-color: #218838;
        }
    </style>
    <script>
    function showPanel(panelId) {
        var panels = document.querySelectorAll('.panel');
        panels.forEach(panel => panel.classList.remove('active'));

        document.getElementById(panelId).classList.add('active');

        var buttons = document.querySelectorAll('.sidebar button');
        buttons.forEach(button => button.classList.remove('active'));
        document.querySelector(`[data-panel="${panelId}"]`).classList.add('active');
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelector('[data-panel="viewStatusPanel"]').addEventListener('click', function (event) {
            event.preventDefault(); // Prevents default navigation

            fetch('status')
                .then(response => response.text())
                .then(data => {
                    // Update only the table body to avoid sidebar duplication
                    document.getElementById('viewStatusTableBody').innerHTML = data; 
                    showPanel('viewStatusPanel');
                })
                .catch(error => console.error('Error loading status:', error));
        });
    });

</script>
</head>
<body>

    <!-- Sidebar -->
    <div class="sidebar">
        <h2>Machine Panel</h2>
        <button data-panel="dashboardPanel" onclick="showPanel('dashboardPanel')">Dashboard</button>
        <button data-panel="pendingTasksPanel" onclick="showPanel('pendingTasksPanel')">Pending Tasks</button>
        <button data-panel="viewStatusPanel">View Status</button>
    </div>

    <!-- Main Content -->
    <div class="content">

        <!-- Dashboard Panel -->
        <div id="dashboardPanel" class="panel active">
            <h2>Dashboard</h2>
            <p>Dashboard content will be added soon...</p>
        </div>

        <!-- Pending Tasks Panel -->
        <div id="pendingTasksPanel" class="panel">
            <div class="dashboard">
                <h2>Pending Tasks</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Job ID</th>
                            <th>Job Name</th>
                            <th>Description</th>
                            <th>Priority</th>
                            <th>Due Date</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 	
                        int job=1;
                            List<Record> records = (List<Record>) request.getAttribute("machineOne");
                            if (records != null && !records.isEmpty()) {
                                for (Record record : records) {
                        %>
                        <tr>
                            <td><%= job %></td>
                            <td><%= record.getJobName() %></td>
                            <td><%= record.getJobDescription() %></td>
                            <td><%= record.getPriority() %></td>
                            <td><%= record.getDueDate() %></td>
                            <td>
                                <form action="machine" method="post" onsubmit="setTimeout(() => location.reload(), 500);">
                                    <input type="hidden" name="jobId" value="<%= record.getJobid()%>">
                                    <button type="submit" class="action-btn">Complete Task</button>
                                </form>
                            </td>
                        </tr>
                        <% 
                        	job++;
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="6" style="text-align: center;">No pending tasks.</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        
        <!-- View Status Panel -->
<!-- View Status Panel -->
 <div id="viewStatusPanel" class="panel">
    <h2>View Status</h2>
    <div id="viewStatusPanelContent">
        <table>
            <thead>
                <tr>
                    <th>Job ID</th>
                    <th>Job Name</th>
                    <th>Description</th>
                    <th>Priority</th>
                    <th>Due Date</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody id="viewStatusTableBody">
                <%-- This tbody will be updated dynamically --%>
                <%
                int job1=1;
                    List<Record> record2 = (List<Record>) request.getAttribute("status");
                    if (record2 != null && !record2.isEmpty()) {
                        for (Record record : record2) {
                %>
                <tr>
                    <td><%= job1 %></td>
                    <td><%= record.getJobName() %></td>
                    <td><%= record.getJobDescription() %></td>
                    <td><%= record.getPriority() %></td>
                    <td><%= record.getDueDate() %></td>
                   <td><%= record.getStatus() %></td>
                </tr>
                
                <%
                
                        }
                    } else {
                %>
                <tr>
                    <td colspan="6" style="text-align: center;">No status records available.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>
    </div>

</body>
</html>
