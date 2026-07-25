package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/staffSalaryEdit")
public class StaffSalaryEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);

			String salaryIdStr = request.getParameter("salary_id");
			int salaryId = Integer.parseInt(salaryIdStr);

			pst = con.prepareStatement("SELECT * FROM staff_salary WHERE salary_id=?");
			pst.setInt(1, salaryId);
			rs = pst.executeQuery();

			out.println("<html><body>");
			out.println("<h2>Edit Staff Salary</h2>");

			if (rs.next()) {
				out.println("<form method='post' action='staffSalaryUpdate'>");
				out.println("<input type='hidden' name='salary_id' value='" + rs.getInt("salary_id") + "'/>");
				out.println("Staff ID: <input type='number' name='staff_id' value='" + rs.getInt("staff_id")
						+ "' required/><br>");
				out.println(
						"Month: <input type='text' name='month' value='" + rs.getString("month") + "' required/><br>");
				out.println("Base Salary: <input type='text' name='base_salary' value='" + rs.getDouble("base_salary")
						+ "' required/><br>");
				out.println("Days Worked: <input type='number' name='days_worked' value='" + rs.getInt("days_worked")
						+ "' required/><br>");
				out.println("Bonuses: <input type='text' name='bonuses' value='" + rs.getDouble("bonuses") + "'/><br>");
				out.println("Deductions: <input type='text' name='deductions' value='" + rs.getDouble("deductions")
						+ "'/><br>");
				out.println("<input type='submit' value='Update'/>");
				out.println("</form>");
			} else {
				out.println("Record not found!");
			}

			out.println("</body></html>");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
