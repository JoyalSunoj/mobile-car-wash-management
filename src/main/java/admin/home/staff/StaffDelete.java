package admin.home.staff;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/staffDelete")
public class StaffDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			String sid = request.getParameter("sid");
			int staffId = Integer.parseInt(sid);

			PreparedStatement pst = con.prepareStatement("DELETE FROM staff_details WHERE staff_id=?");
			pst.setInt(1, staffId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("staffServlet");
			else
				response.getWriter().print("Delete failed!");

			pst.close();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			response.getWriter().print("Error: " + ex.getMessage());
		}
	}
}
