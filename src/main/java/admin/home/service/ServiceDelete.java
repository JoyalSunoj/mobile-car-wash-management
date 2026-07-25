// package: admin.home.service
// File: ServiceDelete.java
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

@WebServlet("/serviceDelete")
public class ServiceDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String sid = request.getParameter("sid");
			if (sid == null || sid.trim().isEmpty()) {
				response.getWriter().print("Missing service id");
				return;
			}
			int serviceId = Integer.parseInt(sid);

			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pst = con.prepareStatement("DELETE FROM service_details WHERE service_id=?");
			pst.setInt(1, serviceId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("service");
			else
				response.getWriter().print("Delete failed!");

			pst.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("Error: " + e.getMessage());
		}
	}
}
