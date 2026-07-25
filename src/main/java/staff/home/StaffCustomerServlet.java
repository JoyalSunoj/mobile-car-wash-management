package staff.home;

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

@WebServlet("/staffCustomer")
public class StaffCustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASS = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}
		String staffName = (String) session.getAttribute("user");

		out.println(
				"<html><head><title>Customers</title></head><body style='font-family:Arial,sans-serif;background:#f4f4f4;'>");
		out.println("<header style='background:#007bff;color:white;padding:15px;text-align:center;'>");
		out.println("<h1>WashOnWheels - Staff Portal</h1>");
		out.println("<p>Welcome, " + staffName + "</p>");
		out.println("<a href='staff' style='color:white;text-decoration:none;'>Home</a> | ");
		out.println("<a href='staffLogout' style='color:white;text-decoration:none;'>Logout</a>");
		out.println("</header>");

		// Add Customer Form
		out.println("<div style='padding:20px;'>");
		out.println("<h2>Add Customer</h2>");
		out.println("<form method='post' action='staffCustomerSave'>");
		out.println("Name: <input type='text' name='customer_name' required/><br><br>");
		out.println("Phone: <input type='text' name='customer_phone' required/><br><br>");
		out.println("Address: <input type='text' name='customer_address' required/><br><br>");
		out.println("Vehicle Number: <input type='text' name='vehicle_no' required/><br><br>");
		out.println("Vehicle Brand: <input type='text' name='vehicle_brand' required/><br><br>");
		out.println(
				"<input type='submit' value='Add Customer' style='background:#007bff;color:white;padding:5px 15px;border:none;border-radius:5px;'/>");
		out.println("</form></div>");

		// Show added customers by this staff
		out.println("<div style='padding:20px;'>");
		out.println("<h2>Your Customers</h2>");
		out.println(
				"<table border='1' cellpadding='10' style='border-collapse:collapse;width:90%;margin:auto;background:white;'>");
		out.println(
				"<tr style='background:#007bff;color:white;'><th>SL No</th><th>Name</th><th>Phone</th><th>Address</th><th>Vehicle No</th><th>Brand</th></tr>");

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement pst = con.prepareStatement("SELECT * FROM customer_details WHERE added_by=?");
			pst.setString(1, staffName);
			ResultSet rs = pst.executeQuery();
			int i = 1;
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString("customer_name") + "</td>");
				out.println("<td>" + rs.getString("customer_phone") + "</td>");
				out.println("<td>" + rs.getString("customer_address") + "</td>");
				out.println("<td>" + rs.getString("vehicle_no") + "</td>");
				out.println("<td>" + rs.getString("vehicle_brand") + "</td>");
				out.println("</tr>");
				i++;
			}
			rs.close();
			pst.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.println("</table></div>");
		out.println("</body></html>");
		out.close();
	}
}
