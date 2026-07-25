package admin.home.customer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/customerSave")
public class CustomerSave extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, User, Password);

			String name = request.getParameter("customer-name");
			String address = request.getParameter("customer-address");
			String flatId = request.getParameter("flat-id");
			String flatNo = request.getParameter("flat-no");
			String phone = request.getParameter("customer-phone");
			String vehicleNo = request.getParameter("vehicle-no");
			String vehicleBrand = request.getParameter("vehicle-brand");

			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO customer_details (customer_name,customer_address, flat_id, flat_no, customer_phone, vehicle_no, vehicle_brand) VALUES (?,?,?,?,?,?,?)");

			pst.setString(1, name);
			pst.setString(2, address == null ? "" : address);
			pst.setInt(3, Integer.parseInt(flatId));
			pst.setString(4, flatNo == null ? "" : flatNo);
			pst.setString(5, phone == null ? "" : phone);
			pst.setString(6, vehicleNo == null ? "" : vehicleNo);
			pst.setString(7, vehicleBrand == null ? "" : vehicleBrand);

			int flag = pst.executeUpdate();

			if (flag > 0)
				response.sendRedirect("customer");
			else
				response.getWriter().print("Save failed.");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("ERROR: " + e.getMessage());
		}
	}
}
