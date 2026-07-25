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

@WebServlet("/customerDelete")
public class CustomerDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(); // get or create session
		String user = (String) session.getAttribute("user");

		if (user == null) {  
		    response.sendRedirect("index.html"); // go back to login
		}

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, User, Password);

			int customerId = Integer.parseInt(request.getParameter("cid"));

			PreparedStatement pst = con.prepareStatement("DELETE FROM customer_details WHERE customer_id = ?");

			pst.setInt(1, customerId);

			int flag = pst.executeUpdate();

			if (flag > 0)
				response.sendRedirect("customer");
			else
				response.getWriter().print("Delete failed.");

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("ERROR: " + e.getMessage());
		}
	}
}
