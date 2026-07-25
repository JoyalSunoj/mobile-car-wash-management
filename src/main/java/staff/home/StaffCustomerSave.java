package staff.home;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staffCustomerSave")
public class StaffCustomerSave extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASS = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}
		String staffName = (String) session.getAttribute("user");

		String customerName = request.getParameter("customer_name");
		String customerPhone = request.getParameter("customer_phone");
		String customerAddress = request.getParameter("customer_address");
		String vehicleNo = request.getParameter("vehicle_no");
		String vehicleBrand = request.getParameter("vehicle_brand");

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO customer_details(customer_name, customer_phone, customer_address, vehicle_no, vehicle_brand, added_by) VALUES(?,?,?,?,?,?)");
			pst.setString(1, customerName);
			pst.setString(2, customerPhone);
			pst.setString(3, customerAddress);
			pst.setString(4, vehicleNo);
			pst.setString(5, vehicleBrand);
			pst.setString(6, staffName);

			int flag = pst.executeUpdate();
			pst.close();
			con.close();

			if (flag > 0) {
				// Redirect back to staff customer page to see updated table
				response.sendRedirect("staffCustomer");
			} else {
				response.getWriter().println("Error: Customer not added.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("Error: " + e.getMessage());
		}
	}
}
