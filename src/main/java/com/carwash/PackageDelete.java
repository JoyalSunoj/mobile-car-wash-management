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

@WebServlet("/packageDelete")
public class PackageDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			int packageId = Integer.parseInt(request.getParameter("pid"));

			pst = con.prepareStatement("DELETE FROM packages WHERE package_id=?");
			pst.setInt(1, packageId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("packages");
			else
				response.getWriter().print("Delete failed!");

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("Error: " + e.getMessage());
		}
	}
}
