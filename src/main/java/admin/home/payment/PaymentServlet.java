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

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = con.prepareStatement("SELECT * FROM payment_details");
			ResultSet rs = pst.executeQuery();

			PreparedStatement pstCus = con.prepareStatement("SELECT customer_id, customer_name FROM customer_details");
			ResultSet rsCus = pstCus.executeQuery();

			out.println("<!DOCTYPE html>");
			out.println("<html lang='en'>");
			out.println("<head>");
			out.println("<meta charset='UTF-8'>");
			out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
			out.println("<title>Payment Management - WashOnWheels</title>");

			// JS Print function
			out.println("<script>");
			out.println("function printPayment() {");
			out.println("  var table = document.getElementById('paymentTable');");
			out.println("  var newWin = window.open('', '', 'height=600,width=900');");
			out.println("  newWin.document.write('<html><head><title>Payment List</title></head><body>');");
			out.println(
					"  newWin.document.write('<h2 style=\"text-align:center;\">WashOnWheels - Payment List</h2>');");
			out.println(
					"  newWin.document.write('<table border=\"1\" style=\"width:90%; margin:auto; border-collapse:collapse;\">');");
			out.println(
					"  newWin.document.write('<tr><th>SLNO</th><th>Customer ID</th><th>Date</th><th>Amount</th><th>Mode</th><th>Status</th></tr>');");

			out.println("  for(var r=1;r<table.rows.length;r++){");
			out.println("    newWin.document.write('<tr>' +");
			out.println("      '<td>'+table.rows[r].cells[0].innerText+'</td>' +");
			out.println("      '<td>'+table.rows[r].cells[1].innerText+'</td>' +");
			out.println("      '<td>'+table.rows[r].cells[2].innerText+'</td>' +");
			out.println("      '<td>'+table.rows[r].cells[3].innerText+'</td>' +");
			out.println("      '<td>'+table.rows[r].cells[4].innerText+'</td>' +");
			out.println("      '<td>'+table.rows[r].cells[5].innerText+'</td>' +");
			out.println("    '</tr>');");
			out.println("  }");

			out.println("  newWin.document.write('</table></body></html>');");
			out.println("  newWin.document.close(); newWin.print();");
			out.println("}");
			out.println("</script>");

			out.println("</head>");
			out.println("<body style='font-family: Arial,sans-serif;background-color:#f4f4f4;margin:0;'>");

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

			// Main content
			out.println("<main style='padding:20px;text-align:center;'>");
			out.println("<h2>Payment Management</h2>");

			// Add Payment Form (horizontal)
			out.println("<form method='post' action='paymentSave' style='margin-bottom:18px;'>");

			// Customer Dropdown
			out.println("<select name='customer-id' style='padding:6px;'>");
			while (rsCus.next()) {
				out.println("<option value='" + rsCus.getInt("customer_id") + "'>" + rsCus.getString("customer_name")
						+ "</option>");
			}
			out.println("</select> &nbsp;");

			out.println("<input type='date' name='payment-date' style='padding:6px;'/> &nbsp;");
			out.println("<input type='text' name='payment-amount' placeholder='Amount' style='padding:6px;'/> &nbsp;");
			out.println("<input type='text' name='payment-mode' placeholder='Mode' style='padding:6px;'/> &nbsp;");

			out.println("<select name='payment-status' style='padding:6px;'>");
			out.println("<option value='Paid'>Paid</option>");
			out.println("<option value='Pending'>Pending</option>");
			out.println("</select> &nbsp;");

			out.println("<input type='submit' value='Add Payment' style='padding:6px;'/>");
			out.println("</form>");

			// Print Button
			out.println(
					"<button onclick='printPayment()' style='margin-bottom:10px;padding:6px;'>Print Payment List</button>");

			// Payment Table
			out.println(
					"<table border='1' id='paymentTable' style='width:90%; margin:auto; border-collapse:collapse;'>");
			out.println("<tr style='background-color:#007bff;color:white;'>");
			out.println(
					"<th>SLNO</th><th>Customer ID</th><th>Date</th><th>Amount</th><th>Mode</th><th>Status</th><th>Edit</th><th>Delete</th>");
			out.println("</tr>");

			int i = 1;
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getInt("customer_id") + "</td>");
				out.println("<td>" + rs.getString("payment_date") + "</td>");
				out.println("<td>" + rs.getDouble("payment_amount") + "</td>");
				out.println("<td>" + rs.getString("payment_mode") + "</td>");
				out.println("<td>" + rs.getString("payment_status") + "</td>");

				out.println("<td><a href='paymentEdit?payment-id=" + rs.getInt("payment_id") + "'>Edit</a></td>");
				out.println("<td><a href='paymentDelete?payment-id=" + rs.getInt("payment_id")
						+ "' onclick=\"return confirm('Delete this payment?');\">Delete</a></td>");

				out.println("</tr>");
				i++;
			}

			out.println("</table>");
			out.println("</main>");

			out.println(
					"<footer style='background-color:#333;color:white;text-align:center;padding:10px;margin-top:20px;'>");
			out.println("<p>&copy; 2025 WashOnWheels. All Rights Reserved.</p>");
			out.println("</footer>");

			out.println("</body></html>");

			rs.close();
			pst.close();
			con.close();

		} catch (Exception e) {
			out.print("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
