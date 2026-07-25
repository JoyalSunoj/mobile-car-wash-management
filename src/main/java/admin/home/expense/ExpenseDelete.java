package admin.home.expense;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/expenseDelete")
public class ExpenseDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASS = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASS);

			int id = Integer.parseInt(request.getParameter("id"));
			PreparedStatement pst = con.prepareStatement("DELETE FROM expenses WHERE expense_id=?");
			pst.setInt(1, id);
			pst.executeUpdate();

			con.close();
			response.sendRedirect("expenses");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
