package staff.home;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staff")
public class StaffHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}

		String name = (String) session.getAttribute("user");

		out.println("<!DOCTYPE html>");
		out.println("<html lang='en'><head>");
		out.println("<meta charset='UTF-8'>");
		out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
		out.println("<title>Staff Home - WashOnWheels</title>");
		out.println("</head><body style='font-family:Arial,sans-serif;background:#f4f4f4;margin:0;'>");

		// Header
		out.println("<header style='background:#007bff;color:white;padding:15px;text-align:center;'>");
		out.println("<h1>WashOnWheels Staff Portal</h1>");
		out.println("<p>Welcome, " + name + "</p>");
		out.println("</header>");

		// Menu
		out.println("<nav style='background:#333;padding:10px;text-align:center;'>");
		out.println("<a href='staff' style='color:white;margin-right:15px;text-decoration:none;'>Home</a>");
		out.println(
				"<a href='staffCustomer' style='color:white;margin-right:15px;text-decoration:none;'>Customers</a>");
		out.println(
				"<a href='staffBooking' style='color:white;margin-right:15px;text-decoration:none;'>Service Booking</a>");
		out.println("<a href='staffPayment' style='color:white;margin-right:15px;text-decoration:none;'>Payments</a>");
		out.println("<a href='staffExpenses' style='color:white;margin-right:15px;text-decoration:none;'>Expenses</a>");
		out.println(
				"<a href='staffAttendance' style='color:white;margin-right:15px;text-decoration:none;'>Attendance</a>");
		out.println("<a href='staffSalary' style='color:white;margin-right:15px;text-decoration:none;'>Salary</a>");
		out.println(
				"<a href='staffAssignedWork' style='color:white;margin-right:15px;text-decoration:none;'>Assigned Works</a>");
		out.println("<a href='logout' style='color:white;text-decoration:none;'>Logout</a>");
		out.println("</nav>");

		// Main
		out.println("<main style='text-align:center;padding:20px;'>");
		out.println("<p>Select a module from the menu above to manage your tasks.</p>");
		
		out.println("</main>");

		// Footer
		out.println("<footer style='background:#333;color:white;text-align:center;padding:10px;margin-top:20px;'>");
		out.println("<p>&copy; 2025 WashOnWheels. All Rights Reserved.</p>");
		out.println("</footer>");

		out.println("</body></html>");
		out.close();
	}
}
