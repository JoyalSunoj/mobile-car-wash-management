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
import javax.servlet.http.HttpSession;

@WebServlet("/washBookingEdit")
public class WashBookingEdit extends HttpServlet {
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
			PreparedStatement pst = con.prepareStatement("SELECT * FROM wash_booking WHERE booking_id=?");
			pst.setInt(1, bookingId);
			ResultSet rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("<html><body>");
			out.println("<h1>Welcome " + name + "</h1><br><br>");
			out.println("<a href='washBooking'>Back</a><br><br>");

			while (rs.next()) {
				out.println("<form method='post' action='washBookingUpdate'>");
				out.println("<input type='hidden' name='booking-id' value='" + rs.getInt("booking_id") + "'/>");
				out.println("Customer ID<input type='text' name='customer-id' value='" + rs.getInt("customer_id")
						+ "'/><br>");
				out.println("Flat ID<input type='text' name='flat-id' value='" + rs.getInt("flat_id") + "'/><br>");
				out.println("Vehicle No<input type='text' name='vehicle-no' value='" + rs.getString("vehicle_no")
						+ "'/><br>");
				out.println(
						"Service ID<input type='text' name='service-id' value='" + rs.getInt("service_id") + "'/><br>");
				out.println("Booking Date<input type='date' name='booking-date' value='" + rs.getDate("booking_date")
						+ "'/><br>");
				out.println("Status<input type='text' name='status' value='" + rs.getString("status") + "'/><br>");
				out.println("<input type='submit' value='Update Booking'/>");
				out.println("</form>");
			}
			out.println("</body></html>");
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
