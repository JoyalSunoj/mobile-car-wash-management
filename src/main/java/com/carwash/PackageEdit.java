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

@WebServlet("/packageEdit")
public class PackageEdit extends HttpServlet {
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

			String pidStr = request.getParameter("pid");
			int pid = Integer.parseInt(pidStr);

			pst = con.prepareStatement("SELECT * FROM packages WHERE package_id = ?");
			pst.setInt(1, pid);
			rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			out.println("<html><body>");
			out.println("<h1>Welcome " + name + "</h1><br><br>");
			out.println("<a href='packages'>Packages</a><br>");
			out.println("<a href='logout'>Logout</a><br>");
			out.println("<h2>Edit Package</h2>");

			if (rs.next()) {
				out.println("<form method='post' action='packageUpdate'>");
				out.println("<input type='hidden' name='package_id' value='" + rs.getInt("package_id") + "'/><br>");
				out.println("Package Name: <input type='text' name='package_name' value='"
						+ rs.getString("package_name") + "' required/><br>");
				out.println("Service IDs: <input type='text' name='service_ids' value='" + rs.getString("service_ids")
						+ "' required/><br>");
				out.println("Package Price: <input type='number' step='0.01' name='package_price' value='"
						+ rs.getBigDecimal("package_price") + "' required/><br>");
				out.println("<input type='submit' value='Update'/>");
				out.println("</form>");
			}

			out.println("</body></html>");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
