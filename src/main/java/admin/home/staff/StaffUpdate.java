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

@WebServlet("/staffUpdate")
public class StaffUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			int staffId = Integer.parseInt(request.getParameter("staff-id"));
			String name = request.getParameter("staff-name");
			String phone = request.getParameter("staff-phone");
			String role = request.getParameter("staff-role");
			String email = request.getParameter("staff-email");
			String address = request.getParameter("staff-address");

			PreparedStatement pst = con.prepareStatement(
					"UPDATE staff_details SET staff_name=?, staff_phone=?, staff_role=?, staff_email=?, staff_address=? WHERE staff_id=?");
			pst.setString(1, name != null ? name.trim() : "");
			pst.setString(2, phone != null ? phone.trim() : "");
			pst.setString(3, role != null ? role.trim() : "");
			pst.setString(4, email != null ? email.trim() : "");
			pst.setString(5, address != null ? address.trim() : "");
			pst.setInt(6, staffId);

			int flag = pst.executeUpdate();
			if (flag > 0)
				response.sendRedirect("staffServlet");
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
