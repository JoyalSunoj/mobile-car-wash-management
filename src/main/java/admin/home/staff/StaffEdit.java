package admin.home.staff;

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

@WebServlet("/staffEdit")
public class StaffEdit extends HttpServlet {
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
		if (sid == null) {
			response.sendRedirect("staffServlet");
			return;
		}

		try {
			int staffId = Integer.parseInt(sid);

			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = con.prepareStatement("SELECT * FROM staff_details WHERE staff_id=?");
			pst.setInt(1, staffId);
			ResultSet rs = pst.executeQuery();

			if (!rs.next()) {
				rs.close();
				pst.close();
				con.close();
				response.sendRedirect("staffServlet");
				return;
			}

			String staffName = rs.getString("staff_name");
			String staffPhone = rs.getString("staff_phone");
			String staffRole = rs.getString("staff_role");
			String staffEmail = rs.getString("staff_email");
			String staffAddress = rs.getString("staff_address");

			out.println("<!DOCTYPE html>");
			out.println("<html lang='en'>");
			out.println("<head>");
			out.println("<meta charset='UTF-8'>");
			out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
			out.println("<title>Edit Staff - WashOnWheels</title>");
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
			out.println("<a href='logout' style='color:white;text-decoration:none;'>Logout</a>");
			out.println("</nav>");

			// Main content
			out.println("<main style='padding:20px;text-align:center;'>");
			out.println("<h2>Edit Staff</h2>");

			out.println("<form method='post' action='staffUpdate' style='margin-bottom:20px;'>");
			out.println("<input type='hidden' name='staff-id' value='" + staffId + "' />");
			out.println("<input type='text' name='staff-name' value='" + escape(staffName)
					+ "' placeholder='Staff Name' style='padding:6px;'/> &nbsp;");
			out.println("<input type='text' name='staff-phone' value='" + escape(staffPhone)
					+ "' placeholder='Phone' style='padding:6px;'/> &nbsp;");
			out.println("<input type='text' name='staff-role' value='" + escape(staffRole)
					+ "' placeholder='Role' style='padding:6px;'/> &nbsp;");
			out.println("<input type='email' name='staff-email' value='" + escape(staffEmail)
					+ "' placeholder='Email' style='padding:6px;'/> &nbsp;");
			out.println("<input type='text' name='staff-address' value='" + escape(staffAddress)
					+ "' placeholder='Address' style='padding:6px; width:220px;'/> &nbsp;");
			out.println("<input type='submit' value='Update Staff' style='padding:6px;'/> &nbsp;");
			out.println("<a href='staffServlet'><button type='button' style='padding:6px;'>Cancel</button></a>");
			out.println("</form>");

			out.println("</main>");

			// Footer
			out.println(
					"<footer style='background-color:#333;color:white;text-align:center;padding:10px;margin-top:20px;'>");
			out.println("<p>&copy; 2025 WashOnWheels. All Rights Reserved.</p>");
			out.println("</footer>");

			out.println("</body>");
			out.println("</html>");

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
