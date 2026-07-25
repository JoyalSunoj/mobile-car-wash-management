// package: admin.home.service
// File: ServiceServlet.java
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

@WebServlet("/service")
public class ServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i = 1;
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

			PreparedStatement pst = con.prepareStatement("SELECT * FROM service_details");
			ResultSet rs = pst.executeQuery();

			out.println("<!DOCTYPE html>");
			out.println("<html lang='en'>");
			out.println("<head>");
			out.println("<meta charset='UTF-8'>");
			out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
			out.println("<title>Service Management - WashOnWheels</title>");

			// print JS (creates printable table without action columns)
			out.println("<script>");
			out.println("function printServices(){");
			out.println(" var table = document.getElementById('serviceTable');");
			out.println(" var newWin = window.open('','', 'height=600,width=800');");
			out.println(" newWin.document.write('<html><head><title>Services</title></head><body>');");
			out.println(" newWin.document.write('<h2 style=\"text-align:center;\">WashOnWheels - Service List</h2>');");
			out.println(
					" newWin.document.write('<table border=\"1\" style=\"width:70%; margin:auto; border-collapse:collapse;\">');");
			out.println(" newWin.document.write('<tr><th>SLNO</th><th>Service Name</th><th>Rate</th></tr>');");
			out.println(" for(var r=1;r<table.rows.length;r++){");
			out.println(
					"  newWin.document.write('<tr><td>'+table.rows[r].cells[0].innerText+'</td><td>'+table.rows[r].cells[1].innerText+'</td><td>'+table.rows[r].cells[2].innerText+'</td></tr>');");
			out.println(" }");
			out.println(" newWin.document.write('</table></body></html>'); newWin.document.close(); newWin.print();");
			out.println("}");
			out.println("</script>");

			out.println("</head>");
			out.println("<body style='font-family:Arial,sans-serif;background-color:#f4f4f4;margin:0;'>");

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
			out.println("<a href='service' style='color:white;margin-right:15px;text-decoration:none;'>Service</a>");
			out.println("<a href='staffServlet' style='color:white;margin-right:15px;text-decoration:none;'>Staff</a>");
			out.println("<a href='logout' style='color:white;text-decoration:none;'>Logout</a>");
			out.println("</nav>");

			// Main content
			out.println("<main style='padding:20px;text-align:center;'>");
			out.println("<h2>Service Management</h2>");

			// Add service form (horizontal)
			out.println("<form method='post' action='serviceSave' style='margin-bottom:20px;'>");
			out.println(
					"<input type='text' name='service-name' placeholder='Service Name' required style='padding:5px;'/> &nbsp;");
			out.println(
					"<input type='text' name='service-rate' placeholder='Service Rate' required style='padding:5px;'/> &nbsp;");
			out.println("<input type='submit' value='Add Service' style='padding:5px;'/> &nbsp;");
			out.println("<a href='admin'><button type='button' style='padding:5px;'>Cancel</button></a>");
			out.println("</form>");

			// Print button
			out.println(
					"<button onclick='printServices()' style='margin-bottom:10px;padding:5px;'>Print Services</button>");

			// Service table
			out.println(
					"<table border='1' id='serviceTable' style='width:70%; margin:auto; border-collapse:collapse;'>");
			out.println("<tr style='background-color:#007bff;color:white;'>");
			out.println("<th>SLNO</th>");
			out.println("<th>SERVICE NAME</th>");
			out.println("<th>SERVICE RATE</th>");
			out.println("<th>Edit</th>");
			out.println("<th>Delete</th>");
			out.println("</tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString("service_name") + "</td>");
				out.println("<td>" + rs.getString("service_rate") + "</td>");
				out.println("<td><a href='serviceEdit?sid=" + rs.getInt("service_id") + "'>Edit</a></td>");
				out.println("<td><a href='serviceDelete?sid=" + rs.getInt("service_id")
						+ "' onclick=\"return confirm('Are you sure to delete this service?');\">Delete</a></td>");
				out.println("</tr>");
				i++;
			}

			out.println("</table>");
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
}
