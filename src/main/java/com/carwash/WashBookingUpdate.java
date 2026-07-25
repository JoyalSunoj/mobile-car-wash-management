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

@WebServlet("/washBookingUpdate")
public class WashBookingUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, User, Password);

			int bookingId = Integer.parseInt(request.getParameter("booking-id"));
			int customerId = Integer.parseInt(request.getParameter("customer-id"));
			int flatId = Integer.parseInt(request.getParameter("flat-id"));
			String vehicleNo = request.getParameter("vehicle-no");
			int serviceId = Integer.parseInt(request.getParameter("service-id"));
			String bookingDate = request.getParameter("booking-date");
			String status = request.getParameter("status");

			PreparedStatement pst = con.prepareStatement(
					"UPDATE wash_booking SET customer_id=?, flat_id=?, vehicle_no=?, service_id=?, booking_date=?, status=? WHERE booking_id=?");
			pst.setInt(1, customerId);
			pst.setInt(2, flatId);
			pst.setString(3, vehicleNo);
			pst.setInt(4, serviceId);
			pst.setString(5, bookingDate);
			pst.setString(6, status);
			pst.setInt(7, bookingId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("washBooking");
			else
				response.getWriter().print("Booking update failed!");

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("Error: " + e.getMessage());
		}
	}
}
