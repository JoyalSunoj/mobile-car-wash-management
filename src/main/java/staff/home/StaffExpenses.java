package staff.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staffExpenses")
public class StaffExpenses extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}
		String staffName = (String) session.getAttribute("user");

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = con.prepareStatement("SELECT * FROM expenses WHERE added_by_staff=?");
			pst.setString(1, staffName);
			ResultSet rs = pst.executeQuery();

			out.println("<html><head><title>Expenses</title></head><body style='font-family:Arial,sans-serif;'>");
			out.println("<h1>Expenses Added by You</h1>");
			out.println("<a href='staff'>Back to Dashboard</a><br><br>");

			// ADD EXPENSE FORM
			out.println("<form method='post' action='staffExpensesSave'>");
			out.println("Expense Name: <input type='text' name='expense_name' required/><br>");
			out.println("Amount: <input type='number' step='0.01' name='amount' required/><br>");
			out.println("Date: <input type='date' name='expense_date' required/><br>");
			out.println("<input type='submit' value='Add Expense'/>");
			out.println("</form><br>");

			// TABLE OF EXPENSES
			out.println("<table border='1' style='border-collapse:collapse;width:80%'>");
			out.println(
					"<tr style='background-color:#007bff;color:white;'><th>SL</th><th>Name</th><th>Amount</th><th>Date</th></tr>");
			int i = 1;
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString("expense_name") + "</td>");
				out.println("<td>" + rs.getDouble("amount") + "</td>");
				out.println("<td>" + rs.getDate("expense_date") + "</td>");
				out.println("</tr>");
				i++;
			}

			out.println("</table></body></html>");
			rs.close();
			pst.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
