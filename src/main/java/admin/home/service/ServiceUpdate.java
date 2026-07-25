// package: admin.home.service
// File: ServiceUpdate.java
package admin.home.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/serviceUpdate")
public class ServiceUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String sid = request.getParameter("service-id");
			if (sid == null || sid.trim().isEmpty()) {
				response.getWriter().print("Missing service id");
				return;
			}
			int serviceId = Integer.parseInt(sid);

			String serviceName = request.getParameter("service-name");
			String serviceRate = request.getParameter("service-rate");

			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = con
					.prepareStatement("UPDATE service_details SET service_name=?, service_rate=? WHERE service_id=?");
			pst.setString(1, serviceName != null ? serviceName.trim() : "");
			pst.setString(2, serviceRate != null ? serviceRate.trim() : "");
			pst.setInt(3, serviceId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("service");
			else
				response.getWriter().print("Update failed");

			pst.close();
			con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			response.getWriter().print("Error: " + ex.getMessage());
		}
	}
}
