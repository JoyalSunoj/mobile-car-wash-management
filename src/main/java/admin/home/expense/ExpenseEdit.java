package admin.home.expense;

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

@WebServlet("/expenseEdit")
public class ExpenseEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASS = "YOUR_MYSQL_PASSWORD_HERE";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}
		String name = (String) session.getAttribute("user");

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASS);

			int id = Integer.parseInt(request.getParameter("id"));
			PreparedStatement pst = con.prepareStatement("SELECT * FROM expenses WHERE expense_id=?");
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			out.println("<!DOCTYPE html>");
			out.println(
					"<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Edit Expense</title></head>");
			out.println("<body style='font-family:Arial,sans-serif;background:#f4f4f4;margin:0;'>");

			// Header
			out.println("<header style='background:#007bff;color:white;padding:15px;text-align:center;'>");
			out.println("<h1>WashOnWheels - Admin Portal</h1>");
			out.println("<p>Welcome, " + name + "</p>");
			out.println("</header>");

			// Navigation
			out.println(
					"<nav style='background-color:#333;padding:10px;display:flex;flex-wrap:wrap;justify-content:center;'>");
			out.println("<a href='admin' style='color:white;margin-right:15px;text-decoration:none;'>Home</a>");
			out.println("<a href='expenses' style='color:white;margin-right:15px;text-decoration:none;'>Expenses</a>");
			out.println("<a href='logout' style='color:white;text-decoration:none;'>Logout</a>");
			out.println("</nav>");

			out.println("<main style='padding:20px; display:flex; justify-content:center;'>");
			out.println(
					"<div style='background:white;padding:30px;border-radius:10px;box-shadow:0 0 10px rgba(0,0,0,0.2);width:50%;'>");
			out.println("<h2 style='text-align:center;'>Edit Expense</h2>");
			out.println("<a href='expenses' style='color:#007bff;'>&larr; Back to Expense List</a><br><br>");

			if (rs.next()) {
				out.println("<form method='post' action='expenseUpdate'>");
				out.println("<input type='hidden' name='expense-id' value='" + rs.getInt("expense_id") + "'/>");
				out.println("<label>Expense Name</label><br>");
				out.println("<input type='text' name='expense-name' value='" + rs.getString("expense_name")
						+ "' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;' required/><br>");
				out.println("<label>Amount</label><br>");
				out.println("<input type='number' step='0.01' name='expense-amount' value='"
						+ rs.getDouble("expense_amount")
						+ "' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;' required/><br>");
				out.println("<label>Date</label><br>");
				out.println("<input type='date' name='expense-date' value='" + rs.getDate("expense_date")
						+ "' style='width:95%;padding:8px;margin:5px 0;border:1px solid #888;border-radius:5px;' required/><br>");
				out.println(
						"<input type='submit' value='Update Expense' style='background:#007bff;color:white;padding:10px 20px;border:none;border-radius:5px;cursor:pointer;margin-top:10px;'/>");
				out.println("</form>");
			}

			out.println("</div></main>");
			out.println("</body></html>");

			rs.close();
			pst.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
