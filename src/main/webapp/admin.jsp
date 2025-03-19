<%@page import="com.tcs.printing.DatabaseConnection"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.ResultSet" %>
	
<!DOCTYPE html>
<html lang="en">
<head>
	 <meta charset="UTF-8">
	 <meta name="viewport" content="width=device-width, initial-scale=1.0">
	 <title>Manager Panel</title>
	 <style>
	        body {
	            font-family: Arial, sans-serif;
	            background-color: #f4f4f4;
	            margin: 0;
	            padding: 0;
	            display: flex;
	            height: 100vh;
	        }
	
			.sidebar {
			    width: 200px;
			    background-color: #673AB7;
			    color: #fff;
			    padding-top: 20px;
			    height: 100vh;
			    overflow-y: auto;
			    position: fixed; /* Keep sidebar fixed */
			    top: 0;
			    left: 0;
			}

	        .sidebar button {
	            width: 100%;
	            padding: 15px;
	            background: #673AB7;
	            border: none;
	            color: #fff;
	            text-align: left;
	            cursor: pointer;
	            font-size: 16px;
	        }
	
	        .sidebar button:hover, .sidebar button.active {
	            background-color: #575757;
	        }
	
	        .sidebar h2 {
	            text-align: center;
	        }
	
	        .content {
			    margin-left: 200px; /* Adjust content width to prevent overlay */
			    flex-grow: 1;
			    padding: 20px;
			    background: #f4f4f4;
			    overflow-y: auto;
			}
	
	        .panel {
	            display: none;
	        }
	
	        .panel.active {
	            display: block;
	        }
	
	        .form-group {
	            margin-bottom: 20px;
	        }
	
	        label {
	            font-weight: bold;
	            margin-bottom: 5px;
	            display: block;
	        }
	
	        input[type="text"],
	        input[type="date"],
	        textarea,
	        select,
	        input[type="file"] {
	            width: 100%;
	            padding: 10px;
	            font-size: 16px;
	            border: 1px solid #ccc;
	            border-radius: 5px;
	            box-sizing: border-box;
	        }
	
	        textarea {
	            resize: vertical;
	        }
	
	        button[type="submit"] {
	            padding: 10px 20px;
	            font-size: 16px;
	            background-color: #333;
	            color: #fff;
	            border: none;
	            border-radius: 5px;
	            cursor: pointer;
	        }
	
	        button[type="submit"]:hover {
	            background-color: #575757;
	        }
			table {
			    width: 100%;
			    border-collapse: collapse;
			    margin-top: 20px;
			    background: #fff;
			    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
			    border-radius: 8px;
			    overflow: hidden;
			}
			
			/* Header Styling */
			thead {
			    background-color: #333;
			    color: white;
			    text-align: center;
			}
			
			th, td {
			    padding: 12px;
			    border: 1px solid #ddd;
			    text-align: center;
			}
			
			/* Alternating Row Colors */
			tbody tr:nth-child(even) {
			    background-color: #f9f9f9;
			}
			
			/* Hover Effect */
			tbody tr:hover {
			    background-color: #f1f1f1;
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
	        
	        function updateSubMachines() {
	            var machineDropdown = document.getElementById("machines");
	            var subMachineDropdown = document.getElementById("subMachines");
	            var subMachineContainer = document.getElementById("subMachineContainer");
	            var selectedMachine = machineDropdown.value;

	            subMachineDropdown.innerHTML = '<option value="" disabled selected>Select a Sub Machine</option>';

	            var subMachines = {
	                "machineOne": ["POLO HQ", "HP Latex", "Rolland", "Plotter Cutting", "Laser", "CNC Router"],
	                "machineTwo": ["Lamination-2", "Sunboard Pasting-2", "Letter Raising", "Framing-3", "Chaddar Making-2", "Standee Making", "others"],
	                "machineThree": ["UV Printing-3", "Fabric Box", "Back to Back", "Clip-on", "Stiching Only", "others"]
	            };

	            if (selectedMachine in subMachines) {
	                subMachines[selectedMachine].forEach(function(subMachine) {
	                    var option = document.createElement("option");
	                    option.value = subMachine;
	                    option.textContent = subMachine;
	                    subMachineDropdown.appendChild(option);
	                });
	                subMachineContainer.style.display = "block";
	            } else {
	                subMachineContainer.style.display = "none";
	            }
	        }
	    </script>
	</head>
	<body>
	    <div class="sidebar">
	        <h2>Admin Panel</h2>
	        <button data-panel="dashboard" onclick="showPanel('dashboard')">Dashboard</button>
	        <button data-panel="assignTaskPanel" onclick="showPanel('assignTaskPanel')">Create Job Card</button>
	        <button data-panel="viewTasksPanel" onclick="showPanel('viewTasksPanel')">View Jobs</button>
	        <button data-panel="manageEmployeesPanel" onclick="showPanel('manageEmployeesPanel')">Manage Machines</button>
	        <button data-panel="reports" onclick="showPanel('reports')">Report</button>
	 	</div>
	
	    <div class="content">
	    
	        <!-- Dashboard Panel -->
	        <div id="dashboard" class="panel">
	            <h2>Dashboard</h2>
	            <p>Dashboard will be showing soon...</p>
	        </div>
	    
	        <!-- Assign Task Panel -->
	        <div id="assignTaskPanel" class="panel active">
	            <h2>Create Job Card</h2>
	            <form action="manager" method="post">
	                
	                <div class="form-group">
	                    <label for="client">Client Name:</label>
	                    <input type="text" id="client" name="client" placeholder="Enter client name" required>
	                </div>
	                
	                <div class="form-group">
	                    <label for="client">Company:</label>
	                    <input type="text" id="company" name="company" placeholder="Enter client name" required>
	                </div>
	                
	                <div class="form-group">
	                    <label for="client">Mobile:</label>
	                    <input type="text" id="mobile" name="mobile" placeholder="Enter client name" required>
	                </div>
	                
	                <div class="form-group">
	                    <label for="jobName">Job Card Details:</label>
	                    <input type="text" id="jobName" name="jobName" placeholder="Enter task title" required>
	                </div>
	
	                <div class="form-group">
	                    <label for="jobDescription">Job Description:</label>
	                    <textarea id="jobDescription" name="jobDescription" placeholder="Enter detailed task description" required></textarea>
	                </div>
	
	                <div class="form-group">
	                    <label for="machines">Machines:</label>
	                    <select id="machines" name="machine" onchange="updateSubMachines()">
	                        <option value="" disabled selected>Select a Process</option>
	                        <option value="machineOne">Process One</option>
	                        <option value="machineTwo">Process Two</option>
	                        <option value="machineThree">Process Three</option>
	                    </select>
	                </div>
	
	                <div class="form-group" id="subMachineContainer" style="display: none;">
	                    <label for="subMachines">Select Sub Machine:</label>
	                    <select id="subMachines" name="subMachine" required>
	                        <option value="" disabled selected>Select a Sub Machine</option>
	                    </select>
	                </div>
	                
	                <div class="form-group">
	                    <label for="priority">Priority Level:</label>
	                    <select id="priority" name="priority" required>
	                        <option value="" disabled selected>Priority Level</option>
	                        <option value="Low">Low</option>
	                        <option value="Medium">Medium</option>
	                        <option value="High">High</option>
	                        <option value="Critical">Critical</option>
	                    </select>
	                </div>
	                
	                <div class="form-group">
	                    <label for="dueDate">Due Date:</label>
	                    <input type="date" id="dueDate" name="dueDate" required>
	                </div>
	
	                <div class="form-group">
	                    <label for="attachment">Attachments:</label>
	                    <input type="file" id="attachment" name="attachment" accept=".pdf,.docx,.xlsx,.png,.jpg">
	                </div>
	
	                <div class="form-group">
	                    <button type="submit">Create</button>
	                </div>
	            </form>
	        </div>
	
	        <!-- View Tasks Panel -->
			<div id="viewTasksPanel" class="panel">
			    <h2>View Jobs</h2>
			
			    <table>
			        <thead>
			            <tr>
			                <th>Job ID</th>
			                <th>Job Name</th>
			                <th>Description</th>
			                <th>Machine</th>
			                <th>Sub Machine</th>
			                <th>Priority</th>
			                <th>Due Date</th>
			                <th>Status</th>
			                <th>Bill Generated</th>
			            </tr>
			        </thead>
			        <tbody>
			    <%
			        try {
			            Connection con = DatabaseConnection.getConnection();
			            String query = "SELECT * FROM jobs";
			            PreparedStatement ps = con.prepareStatement(query);
			            ResultSet rs = ps.executeQuery();
			
			            while (rs.next()) {
			    %>
			                <tr>
			                    <td><%= rs.getInt("job_id") %></td>
			                    <td><%= rs.getString("job_details") %></td>
			                    <td><%= rs.getString("job_description") %></td>
			                    <td><%= rs.getString("machine_name") %></td>
			                    <td><%= rs.getString("sub_machines") %></td>
			                    <td><%= rs.getString("priority") %></td>
			                    <td><%= rs.getDate("due_date") %></td>
			                    <td><%= rs.getString("status") %></td>
			                    <td>
			                        <form action="bill" method="post">
									    <input type="hidden" name="job_id" value="<%= rs.getInt("job_id") %>">
									    <button type="submit">Generate</button>
									</form>
			                    </td>
			                </tr>
			    <%
			            }
			            rs.close();
			            ps.close();
			            con.close();
			        } 
			        catch (Exception e) {
			            e.printStackTrace();
			    %>
			        <tr><td colspan="9">Error fetching data</td></tr>
			    <%
			        }
			    %>
			</tbody>
			
			    </table>
			</div>

	
	       <!-- Manage Employees Panel -->
	       <div id="manageEmployeesPanel" class="panel">
		    <h2>Machines</h2>
		    <table>
		        <thead>
		            <tr>
		                <th>Sr. No.</th>
		                <th>Machine Name</th>
		                <th>Sub Machines</th>
		            </tr>
		        </thead>
		        <tbody>
		            <tr>
		                <td>1</td>
		                <td>Process One</td>
		                <td>
		                    <ul>
		                        <li>POLO HQ</li>
		                        <li>HP Latex</li>
		                        <li>Rolland</li>
		                        <li>Plotter Cutting</li>
		                        <li>Laser</li>
		                        <li>CNC Router</li>
		                    </ul>
		                </td>
		            </tr>
		            <tr>
		            	<td>2</td>
		            	<td>Process Two</td>
		            	<td>
		            		<ul>
		            			<li>Lamination-2</li>
		            			<li>Sunboard Pasting-2</li>
		            			<li>Letter Raising</li>
		            			<li>Framing</li>
		            			<li>Chaddar Making-2</li>
		            			<li>Standee Making</li>
		            			<li>Others</li>
		            		</ul>
		            	</td>
		            </tr>
		            <tr>
		            	<td>3</td>
		            	<td>Process Three</td>
		            	<td>
		            		<ul>
		            			<li>UV Printing-3</li>
		            			<li>Fabric Box</li>
		            			<li>Back to Back</li>
		            			<li>Clip-on</li>
		            			<li>Stiching Only</li>
		            			<li>Others</li>
		            		</ul>
		            	</td>
		            </tr>
		        </tbody>
    </table>
</div>
	    </div>
	</body>
	</html>