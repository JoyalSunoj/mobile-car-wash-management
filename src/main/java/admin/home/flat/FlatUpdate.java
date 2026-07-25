package admin.home.flat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/flatUpdate")
public class FlatUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, User, Password);

			int flatId = Integer.parseInt(request.getParameter("flat-id"));
			String flatName = request.getParameter("flat-name");
			String flatAddress = request.getParameter("flat-address");

			PreparedStatement pst = con.prepareStatement("UPDATE flat_details SET flat_name=?, flat_address=? WHERE flat_id=?");
			pst.setString(1, flatName);
			pst.setString(2, flatAddress);
			pst.setInt(3, flatId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("flat");
			else
				response.sendRedirect("admin");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
