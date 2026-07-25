package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/attendanceSave")
public class AttendanceSave extends HttpServlet {
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

			int staffId = Integer.parseInt(request.getParameter("staff-id"));
			String date = request.getParameter("date");
			String status = request.getParameter("status");

			pst = con.prepareStatement("INSERT INTO attendance_details(staff_id, date, status) VALUES (?, ?, ?)");
			pst.setInt(1, staffId);
			pst.setDate(2, Date.valueOf(date));
			pst.setString(3, status);

			int flag = pst.executeUpdate();
			if (flag > 0) {
				response.sendRedirect("attendance");
			} else {
				response.getWriter().print("Save failed!");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.getWriter().print("Error: " + e.getMessage());
		}
	}
}
