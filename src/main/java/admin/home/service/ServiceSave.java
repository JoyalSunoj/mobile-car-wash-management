// package: admin.home.service
// File: ServiceSave.java
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

@WebServlet("/serviceSave")
public class ServiceSave extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			String serviceName = request.getParameter("service-name");
			String serviceRate = request.getParameter("service-rate");

			if (serviceName == null || serviceName.trim().isEmpty()) {
				response.getWriter().print("Service name required");
				return;
			}
			if (serviceRate == null || serviceRate.trim().isEmpty()) {
				response.getWriter().print("Service rate required");
				return;
			}

			PreparedStatement pst = con
					.prepareStatement("INSERT INTO service_details(service_name, service_rate) VALUES(?,?)");
			pst.setString(1, serviceName.trim());
			pst.setString(2, serviceRate.trim());

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("service");
			else
				response.getWriter().print("Save failed");

			pst.close();
			con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			response.getWriter().print("Error: " + ex.getMessage());
		}
	}
}
