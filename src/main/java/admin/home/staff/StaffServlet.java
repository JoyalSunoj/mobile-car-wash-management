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

@WebServlet("/staffServlet")
public class StaffServlet extends HttpServlet {
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

			PreparedStatement pst = con.prepareStatement("SELECT * FROM staff_details");
			ResultSet rs = pst.executeQuery();

			out.println("<!DOCTYPE html>");
			out.println("<html lang='en'>");
			out.println("<head>");
			out.println("<meta charset='UTF-8'>");
			out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
			out.println("<title>Staff Management - WashOnWheels</title>");

			// JS print function
			out.println("<script>");
			out.println("function printStaff() {");
			out.println("  var table = document.getElementById('staffTable');");
			out.println("  var newWin = window.open('', '', 'height=600,width=900');");
			out.println("  newWin.document.write('<html><head><title>Staff List</title></head><body>');");
			out.println("  newWin.document.write('<h2 style=\"text-align:center;\">WashOnWheels - Staff List</h2>');");
			out.println(
					"  newWin.document.write('<table border=\"1\" style=\"width:90%; margin:auto; border-collapse:collapse;\">');");
			out.println(
					"  newWin.document.write('<tr><th>SLNO</th><th>Name</th><th>Phone</th><th>Role</th><th>Email</th><th>Address</th></tr>');");
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
			out.println("<a href='logout' style='color:white;text-decoration:none;'>Logout</a>");
			out.println("</nav>");

			// Main content
			out.println("<main style='padding:20px;text-align:center;'>");
			out.println("<h2>Staff Management</h2>");

			// Horizontal add form
			out.println("<form method='post' action='staffSave' style='margin-bottom:18px;'>");
			out.println(
					"<input type='text' name='staff-name' placeholder='Staff Name' required style='padding:6px;'/> &nbsp;");
			out.println("<input type='text' name='staff-phone' placeholder='Phone' style='padding:6px;'/> &nbsp;");
			out.println("<input type='text' name='staff-role' placeholder='Role' style='padding:6px;'/> &nbsp;");
			out.println("<input type='email' name='staff-email' placeholder='Email' style='padding:6px;'/> &nbsp;");
			out.println(
					"<input type='text' name='staff-address' placeholder='Address' style='padding:6px; width:220px;'/> &nbsp;");
			out.println("<input type='submit' value='Add Staff' style='padding:6px;'/> &nbsp;");
			out.println("</form>");

			// Print button
			out.println(
					"<button onclick='printStaff()' style='margin-bottom:10px;padding:6px;'>Print Staff List</button>");

			// Staff table
			out.println("<table border='1' id='staffTable' style='width:90%; margin:auto; border-collapse:collapse;'>");
			out.println("<tr style='background-color:#007bff;color:white;'>");
			out.println(
					"<th>SLNO</th><th>NAME</th><th>PHONE</th><th>ROLE</th><th>EMAIL</th><th>ADDRESS</th><th>Edit</th><th>Delete</th>");
			out.println("</tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString("staff_name") + "</td>");
				out.println("<td>" + rs.getString("staff_phone") + "</td>");
				out.println("<td>" + rs.getString("staff_role") + "</td>");
				out.println("<td>" + rs.getString("staff_email") + "</td>");
				out.println("<td>" + rs.getString("staff_address") + "</td>");
				out.println("<td><a href='staffEdit?sid=" + rs.getInt("staff_id") + "'>Edit</a></td>");
				out.println("<td><a href='staffDelete?sid=" + rs.getInt("staff_id")
						+ "' onclick=\"return confirm('Are you sure to delete this staff?');\">Delete</a></td>");
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
