package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/packageUpdate")
public class PackageUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			int packageId = Integer.parseInt(request.getParameter("package_id"));
			String name = request.getParameter("package_name");
			String serviceIds = request.getParameter("service_ids");
			String price = request.getParameter("package_price");

			pst = con.prepareStatement(
					"UPDATE packages SET package_name=?, service_ids=?, package_price=? WHERE package_id=?");
			pst.setString(1, name);
			pst.setString(2, serviceIds);
			pst.setBigDecimal(3, new java.math.BigDecimal(price));
			pst.setInt(4, packageId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("packages");
			else
				response.getWriter().print("Update failed!");

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("Error: " + e.getMessage());
		}
	}
}
