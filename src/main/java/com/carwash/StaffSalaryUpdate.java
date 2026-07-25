package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/staffSalaryUpdate")
public class StaffSalaryUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);

			int salaryId = Integer.parseInt(request.getParameter("salary_id"));
			int staffId = Integer.parseInt(request.getParameter("staff_id"));
			String month = request.getParameter("month");
			double baseSalary = Double.parseDouble(request.getParameter("base_salary"));
			int daysWorked = Integer.parseInt(request.getParameter("days_worked"));
			double bonuses = request.getParameter("bonuses").isEmpty() ? 0
					: Double.parseDouble(request.getParameter("bonuses"));
			double deductions = request.getParameter("deductions").isEmpty() ? 0
					: Double.parseDouble(request.getParameter("deductions"));

			double totalSalary = baseSalary * (daysWorked / 30.0) + bonuses - deductions;

			pst = con.prepareStatement(
					"UPDATE staff_salary SET staff_id=?, month=?, base_salary=?, days_worked=?, bonuses=?, deductions=?, total_salary=? WHERE salary_id=?");
			pst.setInt(1, staffId);
			pst.setString(2, month);
			pst.setDouble(3, baseSalary);
			pst.setInt(4, daysWorked);
			pst.setDouble(5, bonuses);
			pst.setDouble(6, deductions);
			pst.setDouble(7, totalSalary);
			pst.setInt(8, salaryId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("staffSalary");
			else
				response.getWriter().print("Update failed!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
