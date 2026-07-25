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

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);

			HttpSession session = request.getSession(false);
			String username = (String) session.getAttribute("user");

			out.println("<html><body>");
			out.println("<h1>Reports Dashboard</h1>");
			out.println("<h3>Welcome " + username + "</h3>");
			out.println("<a href='admin'>Dashboard</a> | <a href='logout'>Logout</a><br><br>");

			// Example Reports: Total Income, Total Expenses, Total Staff, Total Customers
			// Total Income (sum of payments)
			pst = con.prepareStatement("SELECT SUM(amount) AS total_income FROM payments");
			rs = pst.executeQuery();
			if (rs.next()) {
				out.println("<p>Total Income: ₹" + rs.getDouble("total_income") + "</p>");
			}

			// Total Expenses
			pst = con.prepareStatement("SELECT SUM(expense_amount) AS total_expenses FROM expenses");
			rs = pst.executeQuery();
			if (rs.next()) {
				out.println("<p>Total Expenses: ₹" + rs.getDouble("total_expenses") + "</p>");
			}

			// Total Staff
			pst = con.prepareStatement("SELECT COUNT(*) AS total_staff FROM staff");
			rs = pst.executeQuery();
			if (rs.next()) {
				out.println("<p>Total Staff: " + rs.getInt("total_staff") + "</p>");
			}

			// Total Customers
			pst = con.prepareStatement("SELECT COUNT(*) AS total_customers FROM customer_details");
			rs = pst.executeQuery();
			if (rs.next()) {
				out.println("<p>Total Customers: " + rs.getInt("total_customers") + "</p>");
			}

			out.println("<br><h3>Recent Payments</h3>");
			pst = con.prepareStatement("SELECT * FROM payments ORDER BY payment_id DESC LIMIT 5");
			rs = pst.executeQuery();
			out.println("<table border='1'><tr><th>ID</th><th>Customer</th><th>Amount</th><th>Date</th></tr>");
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getInt("payment_id") + "</td>");
				out.println("<td>" + rs.getString("customer_name") + "</td>");
				out.println("<td>" + rs.getDouble("amount") + "</td>");
				out.println("<td>" + rs.getDate("payment_date") + "</td>");
				out.println("</tr>");
			}
			out.println("</table>");

			out.println("</body></html>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
