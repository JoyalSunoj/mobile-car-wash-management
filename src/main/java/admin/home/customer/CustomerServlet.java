package admin.home.customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i = 1;
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = con.prepareStatement("SELECT * FROM flat_details");
			ResultSet flatRs = pst.executeQuery();

			pst = con.prepareStatement("SELECT c.*, f.flat_name FROM customer_details c "
					+ "LEFT JOIN flat_details f ON c.flat_id = f.flat_id");
			ResultSet rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Customer Management - WashOnWheels</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<center>");

			out.println("<h1>WashOnWheels - Admin Portal</h1>");
			out.println("<h2>Welcome " + name + "</h2><br>");
			out.println("<hr>");
			out.println("<table border='2' width='100%' cellspacing='0'>");
			out.println("<tr>");
			out.println("<td align='center'><a href='admin'>Home</a></td>");
			out.println("<td align='center'><a href='washBooking'>Booking</a></td>");
			out.println("<td align='center'><a href='logout'>Logout</a></td>");
			out.println("</tr>");
			out.println("</table>");
			
			out.println("<h1>Customer Details</h1>");
			out.println("<form method='post' action='customerSave'>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td>Customer Name: <input type='text' name='customer-name' placeholder='Customer Name'></td>");
			out.println("<td>Flat Name: <select name='flat-id' >");
			while (flatRs.next()) {
				out.println("<option value='" + flatRs.getInt("flat_id") + "'>" + flatRs.getString("flat_name")
						+ "</option>");
			}
			out.println("</select></td>");
			out.println("<td>Flat No:<input type='text' name='flat-no' placeholder='Flat No'></td>");
			out.println("<td>Customer PhNo: <input type='text' name='customer-phone' placeholder='Phone Number'></td>");
			out.println("</tr>");
			out.println("<tr><td></td></tr>");
			out.println("<tr>");
			out.println("<td>Customer Address: <input type='text' name='customer-address' placeholder='Customer Address'></td>");
			out.println("<td>Vehicle Brand: <input type='text' name='vehicle-brand' placeholder='Vehicle Brand'></td>");
			out.println("<td>Vehicle No: <input type='text' name='vehicle-no' placeholder='Vehicle Number'></td>");
			out.println("</tr>");
			out.println("<tr><td></td></tr>");
			out.println("</table>");
			out.println("<input type='submit' value='Save'>");
			out.println("</form>");
			out.println("<hr><br>");
			out.println("<table border='2' cellpadding='10' cellspacing='0'>");
			out.println("<tr><th>SLNO</th>");
			out.println("<th>CUSTOMER NAME</th>");
			out.println("<th>CUSTOMER ADDRESS</th>");
			out.println("<th>CUSTOMER PHONE NO</th>");
			out.println("<th>FLAT NO</th>");
			out.println("<th>FLAT NAME</th>");
			out.println("<th>VEHICLE NO</th>");
			out.println("<th>VEHICLE BRAND</th>");
			out.println("<th colspan='2'>ACTION</th>");

			out.println("</tr>");
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString("customer_name") + "</td>");
				out.println("<td>" + rs.getString("customer_address") + "</td>");
				out.println("<td>" + rs.getString("customer_phone") + "</td>");
				out.println("<td>" + rs.getString("flat_no") + "</td>");
				out.println("<td>" + rs.getString("flat_name") + "</td>");
				out.println("<td>" + rs.getString("vehicle_no") + "</td>");
				out.println("<td>" + rs.getString("vehicle_brand") + "</td>");
				out.println("<td><a href='customerEdit?cid=" + rs.getInt("customer_id") + "'>Edit</a></td>");
				out.println("<td><a href='customerDelete?cid=" + rs.getInt("customer_id")
						+ "' onclick=\"return confirm('Are you sure you want to delete this flat?');\">Delete</a></td>");
				out.println("</tr>");
				i++;
			}
			out.println("</table>");
			out.println("<center>");
			out.println("</body></html>");

			rs.close();
			flatRs.close();
			pst.close();
			con.close();

		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
