package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/attendanceDelete")
public class AttendanceDelete extends HttpServlet {
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

			int attendanceId = Integer.parseInt(request.getParameter("attendance_id"));
			pst = con.prepareStatement("DELETE FROM attendance_details WHERE attendance_id=?");
			pst.setInt(1, attendanceId);

			int flag = pst.executeUpdate();
			if (flag > 0) {
				response.sendRedirect("attendance");
			} else {
				response.getWriter().print("Delete failed!");
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.getWriter().print("Error: " + e.getMessage());
		}
	}
}
