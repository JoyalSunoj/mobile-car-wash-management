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

@WebServlet("/staffSalaryDelete")
public class StaffSalaryDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);

			int salaryId = Integer.parseInt(request.getParameter("salary_id"));
			pst = con.prepareStatement("DELETE FROM staff_salary WHERE salary_id=?");
			pst.setInt(1, salaryId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("staffSalary");
			else
				response.getWriter().print("Delete failed!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
