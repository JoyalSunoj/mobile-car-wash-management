package admin.home;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin")

public class AdminHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Forward the request to doPost
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");
			out.println("<html>");
			out.println("<head><title>Admin Home</title></head>");
			out.println("<body>");
			out.println("<center>");
			out.println("<h1>WashOnWheels - Admin Portal</h1>");
			out.println("<h1>Welcome " + name + "</h1>");
			out.println("<hr>");//horizontal line
			out.println("<table border='2' width='100%' cellspacing='0'>");
			out.println("<tr>");
			out.println("<td align='center'><a href='customer'>Customer</a></td>");
			out.println("<td align='center'><a href='flat'>Flats</a></td>");
			out.println("<td align='center'><a href='washBooking'>Booking</a></td>");
			out.println("<td align='center'><a href='assignWork'>Assign Work</a></td>");
			out.println("<td align='center'><a href='service'>Services</a></td>");
			out.println("<td align='center'><a href='packages'>Packages</a></td>");
			out.println("<td align='center'><a href='staffServlet'>Staff</a></td>");
			out.println("<td align='center'><a href='attendance'>Attendance</a></td>");
			out.println("<td align='center'><a href='staffSalary'>Salary</a></td>");
			out.println("<td align='center'><a href='payment'>Payment</a></td>");
			out.println("<td align='center'><a href='expenses'>Expenses</a></td>");
			out.println("<td align='center'><a href='reports'>Reports</a></td>");
			out.println("<td align='center'><a href='logout'>Logout</a></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<br><br><br><br>");
			out.println("<img src='images/carwash.webp' width='800' height='449'/>");
			out.println("</center>");
			out.println("</body></html>");
			out.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
}

