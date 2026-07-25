// package: admin.home.service
// File: ServiceEdit.java
package admin.home.service;

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

@WebServlet("/serviceEdit")
public class ServiceEdit extends HttpServlet {
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

		String sid = request.getParameter("sid");
		if (sid == null || sid.trim().isEmpty()) {
			response.sendRedirect("service");
			return;
		}
		int serviceId = Integer.parseInt(sid);

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = con.prepareStatement("SELECT * FROM service_details WHERE service_id=?");
			pst.setInt(1, serviceId);
			ResultSet rs = pst.executeQuery();

			if (!rs.next()) {
				response.sendRedirect("service");
				return;
			}

			String svcName = rs.getString("service_name");
			String svcRate = rs.getString("service_rate");

			out.println("<!DOCTYPE html>");
			out.println("<html lang='en'><head>");
			out.println("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
			out.println("<title>Edit Service - WashOnWheels</title>");
			out.println("</head><body style='font-family:Arial,sans-serif;background-color:#f4f4f4;margin:0;'>");

			// Header
			out.println("<header style='background-color:#007bff;color:white;padding:15px;text-align:center;'>");
			out.println("<h1>WashOnWheels - Admin Portal</h1>");
			out.println("<p>Welcome, " + name + "</p>");
			out.println("</header>");

			// Nav
			out.println(
					"<nav style='background-color:#333;padding:10px;display:flex;flex-wrap:wrap;justify-content:center;'>");
			out.println("<a href='admin' style='color:white;margin-right:15px;text-decoration:none;'>Home</a>");
			out.println("<a href='customer' style='color:white;margin-right:15px;text-decoration:none;'>Customer</a>");
			out.println("<a href='flat' style='color:white;margin-right:15px;text-decoration:none;'>Flat</a>");
			out.println("<a href='service' style='color:white;margin-right:15px;text-decoration:none;'>Service</a>");
			out.println("<a href='logout' style='color:white;text-decoration:none;'>Logout</a>");
			out.println("</nav>");

			// Main
			out.println("<main style='padding:20px;text-align:center;'>");
			out.println("<h2>Edit Service</h2>");
			out.println("<form method='post' action='serviceUpdate' style='margin-bottom:20px;'>");
			out.println("<input type='hidden' name='service-id' value='" + serviceId + "'/>");
			out.println("<input type='text' name='service-name' value='" + escape(svcName)
					+ "' required style='padding:5px;'/> &nbsp;");
			out.println("<input type='text' name='service-rate' value='" + escape(svcRate)
					+ "' required style='padding:5px;'/> &nbsp;");
			out.println("<input type='submit' value='Update Service' style='padding:5px;'/> &nbsp;");
			out.println("<a href='service'><button type='button' style='padding:5px;'>Cancel</button></a>");
			out.println("</form>");
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

		} catch (Exception ex) {
			ex.printStackTrace();
			out.print("Error: " + ex.getMessage());
		}
	}

	private String escape(String s) {
		if (s == null)
			return "";
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
				"&#39;");
	}
}
