package admin.home.customer;

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

@WebServlet("/customerEdit")
public class CustomerEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(); // get or create session
		String username = (String) session.getAttribute("user");

		if (username == null) {
			response.sendRedirect("index.html"); // go back to login
		}

		String cid = request.getParameter("cid");

		if (cid == null) {
			response.sendRedirect("customer");
			return;
		}

		int customerId = Integer.parseInt(cid);

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = con.prepareStatement("SELECT * FROM customer_details WHERE customer_id=?");
			pst.setInt(1, customerId);
			ResultSet rs = pst.executeQuery();

			if (!rs.next()) {
				response.sendRedirect("customer");
				return;
			}

			PreparedStatement flatStmt = con.prepareStatement("SELECT * FROM flat_details");
			ResultSet flatRs = flatStmt.executeQuery();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Customer Management - WashOnWheels</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<center>");

			out.println("<h1>WashOnWheels - Admin Portal</h1>");
			out.println("<h2>Welcome " + username + "</h2><br>");
			out.println("<hr>");
			out.println("<table border='2' width='100%' cellspacing='0'>");
			out.println("<tr>");
			out.println("<td align='center'><a href='admin'>Home</a></td>");
			out.println("<td align='center'><a href='washBooking'>Booking</a></td>");
			out.println("<td align='center'><a href='logout'>Logout</a></td>");
			out.println("</tr>");
			out.println("</table>");

			out.println("<h1>Edit Customer</h1>");
			out.println("<form method='post' action='customerUpdate'>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td>Customer Name: <input type='text' name='customer-name' placeholder='Customer Name'value='"
					+ rs.getString("customer_name") + "'/> ></td>");
			out.println("<td>Flat Name: <select name='flat-id' >");
			while (flatRs.next()) {
				out.println("<option value='" + flatRs.getInt("flat_id") + "' "+ (flatRs.getInt("flat_id") == rs.getInt("flat_id") ? "selected" : "")
						+ ">" + flatRs.getString("flat_name") + "</option>");
			}
			out.println("</select></td>");
			out.println("<td>Flat No:<input type='text' name='flat-no' placeholder='Flat No' value='" + rs.getString("flat_no")
					+ "'/> ></td>");
			out.println("<td>Customer PhNo: <input type='text' name='customer-phone' placeholder='Phone Number' value='"
					+ rs.getString("customer_phone") + "'/> ></td>");
			out.println("</tr>");
			out.println("<tr><td></td></tr>");
			out.println("<tr>");
			out.println(
					"<td>Customer Address: <input type='text' name='customer-address' placeholder='Customer Address' value='"
							+ rs.getString("customer_address") + "'/> ></td>");
			out.println("<td>Vehicle Brand: <input type='text' name='vehicle-brand' placeholder='Vehicle Brand' value='"
					+ rs.getString("vehicle_brand") + "'/> ></td>");
			out.println("<td>Vehicle No: <input type='text' name='vehicle-no' placeholder='Vehicle Number' value='"
					+ rs.getString("vehicle_no") + "'/> ></td>");
			out.println("</tr>");
			out.println("<tr><td></td></tr>");
			out.println("</table>");

			out.println("<input type='hidden' name='customer-id' value='" + customerId + "'/> ");

			out.println("<input type='submit' value='Update'/>");
			out.println("<a href='customer'><button type='button'>Cancel</button></a>");

			out.println("</form>");

			out.println("</body></html>");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
