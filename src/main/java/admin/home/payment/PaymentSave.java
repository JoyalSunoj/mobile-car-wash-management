package admin.home.payment;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/paymentSave")
public class PaymentSave extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			int customerId = Integer.parseInt(request.getParameter("customer-id"));
			String date = request.getParameter("payment-date");
			double amount = Double.parseDouble(request.getParameter("payment-amount"));
			String mode = request.getParameter("payment-mode");
			String status = request.getParameter("payment-status");

			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO payment_details (customer_id, payment_date, payment_amount, payment_mode, payment_status) VALUES (?, ?, ?, ?, ?)");

			pst.setInt(1, customerId);
			pst.setString(2, date);
			pst.setDouble(3, amount);
			pst.setString(4, mode);
			pst.setString(5, status);

			int flag = pst.executeUpdate();

			if (flag > 0) {
				response.sendRedirect("payment");
				return;
			}

			out.println("<html><body style='font-family:Arial;background-color:#f4f4f4;'>");
			out.println("<h2 style='color:red;text-align:center;'>Payment Save Failed!</h2>");
			out.println("<a href='payment' style='text-decoration:none;display:block;text-align:center;'>Back</a>");
			out.println("</body></html>");

			con.close();

		} catch (Exception e) {
			out.print("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
