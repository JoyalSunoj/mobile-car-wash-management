package admin.home.expense;

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

@WebServlet("/expenses")
public class ExpensesServlet extends HttpServlet {
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

			PreparedStatement pst = con.prepareStatement("SELECT * FROM expenses");
			ResultSet rs = pst.executeQuery();

			out.println("<!DOCTYPE html>");
			out.println("<html lang='en'>");
			out.println("<head>");
			out.println("<meta charset='UTF-8'>");
			out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
			out.println("<title>Expenses - WashOnWheels</title>");
			out.println("</head>");
			out.println("<body style='font-family:Arial,sans-serif;background:#f4f4f4;margin:0;'>");

			// Header
			out.println("<header style='background:#007bff;color:white;padding:15px;text-align:center;'>");
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
			out.println("<a href='expenses' style='color:white;margin-right:15px;text-decoration:none;'>Expenses</a>");
			out.println("<a href='logout' style='color:white;text-decoration:none;'>Logout</a>");
			out.println("</nav>");

			// Main content
			out.println("<main style='padding:20px;'>");

			// Add Expense Form
			out.println(
					"<div style='background:white;padding:20px;border-radius:10px;box-shadow:0 0 10px rgba(0,0,0,0.2);width:50%;margin:auto;'>");
			out.println("<h2 style='text-align:center;'>Add Expense</h2>");
			out.println("<form method='post' action='expenseSave'>");
			out.println("<label>Expense Name</label><br>");
			out.println(
					"<input type='text' name='expense-name' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;' required/><br>");
			out.println("<label>Amount</label><br>");
			out.println(
					"<input type='number' step='0.01' name='expense-amount' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;' required/><br>");
			out.println("<label>Date</label><br>");
			out.println(
					"<input type='date' name='expense-date' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;' required/><br>");
			out.println(
					"<input type='submit' value='Add Expense' style='background:#007bff;color:white;padding:10px 20px;border:none;border-radius:5px;cursor:pointer;margin-top:10px;'/>");
			out.println("</form>");
			out.println("</div><br>");

			// Expense Table
			out.println(
					"<div style='background:white;padding:20px;border-radius:10px;box-shadow:0 0 10px rgba(0,0,0,0.2);width:90%;margin:auto;'>");
			out.println("<h2 style='text-align:center;'>Expenses List</h2>");
			out.println("<table border='1' cellpadding='10' style='width:100%;border-collapse:collapse;'>");
			out.println("<tr style='background:#007bff;color:white;'>");
			out.println("<th>ID</th><th>Name</th><th>Amount</th><th>Date</th><th>Edit</th><th>Delete</th></tr>");
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getInt("expense_id") + "</td>");
				out.println("<td>" + rs.getString("expense_name") + "</td>");
				out.println("<td>" + rs.getDouble("expense_amount") + "</td>");
				out.println("<td>" + rs.getDate("expense_date") + "</td>");
				out.println("<td><a href='expenseEdit?id=" + rs.getInt("expense_id")
						+ "' style='color:#007bff;'>Edit</a></td>");
				out.println("<td><a href='expenseDelete?id=" + rs.getInt("expense_id")
						+ "' onclick=\"return confirm('Are you sure?');\" style='color:red;'>Delete</a></td>");
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("</div>");

			out.println("</main>");

			// Footer
			out.println("<footer style='background:#333;color:white;text-align:center;padding:10px;margin-top:20px;'>");
			out.println("<p>&copy; 2025 WashOnWheels. All Rights Reserved.</p>");
			out.println("</footer>");

			out.println("</body></html>");
			rs.close();
			pst.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
