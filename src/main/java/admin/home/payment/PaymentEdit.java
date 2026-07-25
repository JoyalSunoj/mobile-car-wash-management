package admin.home.payment;

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

@WebServlet("/paymentEdit")
public class PaymentEdit extends HttpServlet {
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

		String name = (String) session.getAttribute("user");

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASS);

			int paymentId = Integer.parseInt(request.getParameter("payment-id"));

			PreparedStatement pst = con.prepareStatement("SELECT * FROM payment_details WHERE payment_id=?");
			pst.setInt(1, paymentId);
			ResultSet rs = pst.executeQuery();

			out.println("<!DOCTYPE html>");
			out.println("<html lang='en'>");
			out.println("<head>");
			out.println("<meta charset='UTF-8'>");
			out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
			out.println("<title>Edit Payment - WashOnWheels</title>");
			out.println("</head>");
			out.println("<body style='font-family: Arial, sans-serif; background:#f4f4f4; margin:0;'>");

			// Header
			out.println("<header style='background-color:#007bff;color:white;padding:15px;text-align:center;'>");
			out.println("<h1>WashOnWheels - Admin Portal</h1>");
			out.println("<p>Welcome, " + name + "</p>");
			out.println("</header>");

			// Navigation
			out.println(
					"<nav style='background-color:#333;padding:10px;display:flex;flex-wrap:wrap;justify-content:center;'>");
			out.println("<a href='admin' style='color:white;margin-right:15px;text-decoration:none;'>Home</a>");
			out.println("<a href='customer' style='color:white;margin-right:15px;text-decoration:none;'>Customer</a>");
			out.println("<a href='flat' style='color:white;margin-right:15px;text-decoration:none;'>Flat</a>");
			out.println("<a href='service' style='color:white;margin-right:15px;text-decoration:none;'>Services</a>");
			out.println("<a href='staffServlet' style='color:white;margin-right:15px;text-decoration:none;'>Staff</a>");
			out.println("<a href='payment' style='color:white;margin-right:15px;text-decoration:none;'>Payment</a>");
			out.println("<a href='logout' style='color:white;text-decoration:none;'>Logout</a>");
			out.println("</nav>");

			// Main Container
			out.println("<main style='padding:20px; display:flex; justify-content:center;'>");
			out.println(
					"<div style='background:white;padding:30px; border-radius:10px; width:45%; box-shadow:0 0 10px rgba(0,0,0,0.2);'>");
			out.println("<h2 style='text-align:center; margin-bottom:20px;'>Edit Payment</h2>");
			out.println(
					"<a href='payment' style='text-decoration:none;color:#007bff;'>&larr; Back to Payment List</a><br><br>");

			while (rs.next()) {
				out.println("<form method='post' action='paymentUpdate'>");
				out.println("<input type='hidden' name='payment-id' value='" + rs.getInt("payment_id") + "'/>");

				out.println("<label>Customer ID</label><br>");
				out.println("<input type='text' name='customer-id' value='" + rs.getInt("customer_id")
						+ "' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;'/>");

				out.println("<label>Payment Date</label><br>");
				out.println("<input type='date' name='payment-date' value='" + rs.getDate("payment_date")
						+ "' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;'/>");

				out.println("<label>Amount</label><br>");
				out.println("<input type='text' name='payment-amount' value='" + rs.getDouble("payment_amount")
						+ "' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;'/>");

				out.println("<label>Mode</label><br>");
				out.println("<input type='text' name='payment-mode' value='" + rs.getString("payment_mode")
						+ "' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;'/>");

				String currentStatus = rs.getString("payment_status");
				out.println("<label>Status</label><br>");
				out.println(
						"<select name='payment-status' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;'>");
				out.println(
						"<option value='Paid'" + ("Paid".equals(currentStatus) ? " selected" : "") + ">Paid</option>");
				out.println("<option value='Pending'" + ("Pending".equals(currentStatus) ? " selected" : "")
						+ ">Pending</option>");
				out.println("</select><br><br>");

				out.println(
						"<input type='submit' value='Update Payment' style='background:#007bff;color:white;padding:10px 20px;border:none;border-radius:5px; cursor:pointer;'/>");

				out.println("</form>");
			}

			out.println("</div>");
			out.println("</main>");

			// Footer
			out.println(
					"<footer style='background-color:#333;color:white;text-align:center;padding:10px;margin-top:20px;'>");
			out.println("<p>&copy; 2025 WashOnWheels. All Rights Reserved.</p>");
			out.println("</footer>");

			out.println("</body></html>");
			rs.close();
			pst.close();
			con.close();

		} catch (Exception e) {
			out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
			e.printStackTrace();
		}
	}
}
