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

@WebServlet("/packages")
public class PackageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			// Fetch all packages
			pst = con.prepareStatement("SELECT * FROM packages");
			rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			out.println("<html><body>");
			out.println("<h1>Welcome " + name + "</h1><br><br>");

			// Add navigation
			out.println("<a href='customer'>Customer</a><br>");
			out.println("<a href='packages'>Packages</a><br>");
			out.println("<a href='logout'>Logout</a><br>");

			// Form to add new package
			out.println("<h2>Add Package</h2>");
			out.println("<form method='post' action='packageSave'>");
			out.println("Package Name: <input type='text' name='package_name' required/><br>");
			out.println("Service IDs (comma-separated): <input type='text' name='service_ids' required/><br>");
			out.println("Package Price: <input type='number' step='0.01' name='package_price' required/><br>");
			out.println("<input type='submit' value='Save'/>");
			out.println("</form>");

			// Display packages
			out.println("<h2>All Packages</h2>");
			out.println("<table border='1'>");
			out.println(
					"<tr><th>ID</th><th>Name</th><th>Service IDs</th><th>Price</th><th>Edit</th><th>Delete</th></tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getInt("package_id") + "</td>");
				out.println("<td>" + rs.getString("package_name") + "</td>");
				out.println("<td>" + rs.getString("service_ids") + "</td>");
				out.println("<td>" + rs.getString("package_price") + "</td>");
				out.println("<td><a href='packageEdit?pid=" + rs.getInt("package_id") + "'>Edit</a></td>");
				out.println("<td><a href='packageDelete?pid=" + rs.getInt("package_id") + "'>Delete</a></td>");
				out.println("</tr>");
			}

			out.println("</table>");
			out.println("</body></html>");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
