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

@WebServlet("/staffSalary")
public class StaffSalaryServlet extends HttpServlet {
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

			pst = con.prepareStatement(
					"SELECT ss.salary_id, s.staff_name, ss.month, ss.base_salary, ss.days_worked, ss.bonuses, ss.deductions, ss.total_salary FROM staff_salary ss JOIN staff_details s ON ss.staff_id = s.staff_id");
			rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			out.println("<html><body>");
			out.println("<h1>Welcome " + name + "</h1><br><br>");

			out.println("<a href='staffServlet'>Staff</a><br>");
			out.println("<a href='staffSalary'>Staff Salary</a><br>");
			out.println("<a href='logout'>Logout</a><br><br>");

			// Form to add salary
			out.println("<h2>Add Staff Salary</h2>");
			out.println("<form method='post' action='staffSalary'>");
			out.println("Staff ID: <input type='number' name='staff_id' required><br>");
			out.println("Month: <input type='text' name='month' required><br>");
			out.println("Base Salary: <input type='text' name='base_salary' required><br>");
			out.println("Days Worked: <input type='number' name='days_worked' required><br>");
			out.println("Bonuses: <input type='text' name='bonuses'><br>");
			out.println("Deductions: <input type='text' name='deductions'><br>");
			out.println("<input type='submit' value='Save'>");
			out.println("</form><br>");

			// Table for existing salaries
			out.println("<table border='1'>");
			out.println(
					"<tr><th>SLNO</th><th>Staff Name</th><th>Month</th><th>Base Salary</th><th>Days Worked</th><th>Bonuses</th><th>Deductions</th><th>Total Salary</th><th>Edit</th><th>Delete</th></tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString("staff_name") + "</td>");
				out.println("<td>" + rs.getString("month") + "</td>");
				out.println("<td>" + rs.getDouble("base_salary") + "</td>");
				out.println("<td>" + rs.getInt("days_worked") + "</td>");
				out.println("<td>" + rs.getDouble("bonuses") + "</td>");
				out.println("<td>" + rs.getDouble("deductions") + "</td>");
				out.println("<td>" + rs.getDouble("total_salary") + "</td>");
				out.println("<td><a href='staffSalaryEdit?salary_id=" + rs.getInt("salary_id") + "'>Edit</a></td>");
				out.println("<td><a href='staffSalaryDelete?salary_id=" + rs.getInt("salary_id")
						+ "' onclick=\"return confirm('Are you sure to delete?');\">Delete</a></td>");
				out.println("</tr>");
				i++;
			}

			out.println("</table>");
			out.println("</body></html>");

		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);

			int staffId = Integer.parseInt(request.getParameter("staff_id"));
			String month = request.getParameter("month");
			double baseSalary = Double.parseDouble(request.getParameter("base_salary"));
			int daysWorked = Integer.parseInt(request.getParameter("days_worked"));
			double bonuses = request.getParameter("bonuses").isEmpty() ? 0
					: Double.parseDouble(request.getParameter("bonuses"));
			double deductions = request.getParameter("deductions").isEmpty() ? 0
					: Double.parseDouble(request.getParameter("deductions"));

			// Calculate total salary
			double totalSalary = baseSalary * (daysWorked / 30.0) + bonuses - deductions;

			pst = con.prepareStatement(
					"INSERT INTO staff_salary(staff_id, month, base_salary, days_worked, bonuses, deductions, total_salary) VALUES(?,?,?,?,?,?,?)");
			pst.setInt(1, staffId);
			pst.setString(2, month);
			pst.setDouble(3, baseSalary);
			pst.setInt(4, daysWorked);
			pst.setDouble(5, bonuses);
			pst.setDouble(6, deductions);
			pst.setDouble(7, totalSalary);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("staffSalary");
			else
				response.getWriter().print("Save failed!");

		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			response.getWriter().print("Error: " + ex.getMessage());
		}
	}
}
