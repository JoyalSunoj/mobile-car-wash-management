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

@WebServlet("/washBookingDelete")
public class WashBookingDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, User, Password);

			int bookingId = Integer.parseInt(request.getParameter("booking-id"));
			PreparedStatement pst = con.prepareStatement("DELETE FROM wash_booking WHERE booking_id=?");
			pst.setInt(1, bookingId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("washBooking");
			else
				response.getWriter().print("Booking delete failed!");

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("Error: " + e.getMessage());
		}
	}
}
