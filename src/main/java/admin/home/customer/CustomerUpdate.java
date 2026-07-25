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
import javax.servlet.http.HttpSession;

@WebServlet("/customerUpdate")
public class CustomerUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, User, Password);

			int id = Integer.parseInt(request.getParameter("customer-id"));
			String name = request.getParameter("customer-name");
			String flatId = request.getParameter("flat-id");
			String flatNo = request.getParameter("flat-no");
			String phone = request.getParameter("customer-phone");
			String address = request.getParameter("customer-address");
			String vehicleNo = request.getParameter("vehicle-no");
			String vehicleBrand = request.getParameter("vehicle-brand");

			PreparedStatement pst = con.prepareStatement(
					"UPDATE customer_details SET customer_name=?, flat_id=?, flat_no=?, customer_phone=?, customer_address=?, vehicle_no=?, vehicle_brand=? WHERE customer_id=?");

			pst.setString(1, name);
			pst.setInt(2, Integer.parseInt(flatId));
			pst.setString(3, flatNo == null ? "" : flatNo);
			pst.setString(4, phone == null ? "" : phone);
			pst.setString(5, address == null ? "" : address);
			pst.setString(6, vehicleNo == null ? "" : vehicleNo);
			pst.setString(7, vehicleBrand == null ? "" : vehicleBrand);
			pst.setInt(8, id);

			int flag = pst.executeUpdate();

			if (flag > 0)
				response.sendRedirect("customer");
			else
				response.getWriter().print("Update failed.");

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("ERROR: " + e.getMessage());
		}
	}
}
