package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/attendance")
public class AttendanceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i = 1;
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);

			// Fetch all attendance records
			pst = con.prepareStatement(
					"SELECT a.attendance_id, s.staff_name, a.date, a.status FROM attendance_details a JOIN staff_details s ON a.staff_id = s.staff_id");
			rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			out.println("<html><body>");
			out.println("<h1>Welcome " + name + "</h1><br><br>");

			out.println("<a href='staffServlet'>Staff</a><br>");
			out.println("<a href='attendance'>Attendance</a><br>");
			out.println("<a href='logout'>Logout</a><br>");

			// Form to add attendance
			out.println("<h2>Add Attendance</h2>");
			out.println("<form method='post' action='attendanceSave'>");
			out.println("Staff ID: <input type='text' name='staff-id' required/><br>");
			out.println("Date: <input type='date' name='date' required/><br>");
			out.println(
					"Status: <select name='status'><option value='Present'>Present</option><option value='Absent'>Absent</option></select><br>");
			out.println("<input type='submit' value='Save'/>");
			out.println("</form><br>");

			// Display table
			out.println("<table border='1'>");
			out.println("<tr><th>SLNO</th><th>Staff Name</th><th>Date</th><th>Status</th><th>Action</th></tr>");
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString("staff_name") + "</td>");
				out.println("<td>" + rs.getDate("date") + "</td>");
				out.println("<td>" + rs.getString("status") + "</td>");
				out.println("<td><a href='attendanceDelete?attendance_id=" + rs.getInt("attendance_id")
						+ "' onclick='return confirm(\"Are you sure?\");'>Delete</a></td>");
				out.println("</tr>");
				i++;
			}
			out.println("</table>");
			out.println("</body></html>");

		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
