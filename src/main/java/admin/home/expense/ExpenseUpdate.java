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

@WebServlet("/expenseUpdate")
public class ExpenseUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASS = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASS);

			int id = Integer.parseInt(request.getParameter("expense-id"));
			String name = request.getParameter("expense-name");
			double amount = Double.parseDouble(request.getParameter("expense-amount"));
			String date = request.getParameter("expense-date");

			PreparedStatement pst = con.prepareStatement(
					"UPDATE expenses SET expense_name=?, expense_amount=?, expense_date=? WHERE expense_id=?");
			pst.setString(1, name);
			pst.setDouble(2, amount);
			pst.setDate(3, java.sql.Date.valueOf(date));
			pst.setInt(4, id);
			pst.executeUpdate();

			con.close();
			response.sendRedirect("expenses");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
