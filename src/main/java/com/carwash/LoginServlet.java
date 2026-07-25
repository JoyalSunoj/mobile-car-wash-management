package com.carwash;

import java.io.IOException;
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	String userName = "";
	String passWord = "";
	String role = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);

			userName = request.getParameter("username");
			passWord = request.getParameter("password");

			pst = con.prepareStatement("select user_name,user_role from login where user_name=? and user_password=?");
			pst.setString(1, userName);
			pst.setString(2, passWord);

			rs = pst.executeQuery();

			if (rs.next()) {
				role = rs.getString("user_role");
				
			HttpSession session = request.getSession();
			session.setAttribute("user", userName);

			if (role.equals("admin")) {
				response.sendRedirect("admin");
			} else if (role.equals("staff")) {
				response.sendRedirect("staff");
			  }
			}
			else {
				response.sendRedirect("index.html?error=invalid Username or Password");
			}
			 rs.close();
	         pst.close();
	         con.close();

		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
