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

@WebServlet("/washBooking")
public class WashBookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i = 1;
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, User, Password);
			PreparedStatement pst = con.prepareStatement("SELECT * FROM wash_booking");
			ResultSet rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			out.println("<html><body>");
			out.println("<h1>Welcome " + name + "</h1><br><br>");
			out.println("<a href='admin'>Home</a><br>");
			out.println("<a href='logout'>Logout</a><br><br>");

			// Form to add booking
			out.println("<form method='post' action='washBookingSave'>");
			out.println("Customer ID<input type='text' name='customer-id'/><br>");
			out.println("Flat ID<input type='text' name='flat-id'/><br>");
			out.println("Vehicle No<input type='text' name='vehicle-no'/><br>");
			out.println("Service ID<input type='text' name='service-id'/><br>");
			out.println("Booking Date<input type='date' name='booking-date'/><br>");
			out.println("<input type='submit' value='Save Booking'/>");
			out.println("</form><br>");

			// Booking table
			out.println("<table border='1'>");
			out.println(
					"<tr><th>SLNO</th><th>Customer ID</th><th>Flat ID</th><th>Vehicle No</th><th>Service ID</th><th>Booking Date</th><th>Status</th><th>Edit</th><th>Delete</th></tr>");
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getInt("customer_id") + "</td>");
				out.println("<td>" + rs.getInt("flat_id") + "</td>");
				out.println("<td>" + rs.getString("vehicle_no") + "</td>");
				out.println("<td>" + rs.getInt("service_id") + "</td>");
				out.println("<td>" + rs.getDate("booking_date") + "</td>");
				out.println("<td>" + rs.getString("status") + "</td>");
				out.println("<td><a href='washBookingEdit?booking-id=" + rs.getInt("booking_id") + "'>Edit</a></td>");
				out.println("<td><a href='washBookingDelete?booking-id=" + rs.getInt("booking_id")
						+ "' onclick=\"return confirm('Are you sure to delete?');\">Delete</a></td>");
				out.println("</tr>");
				i++;
			}
			out.println("</table>");
			out.println("</body></html>");
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
