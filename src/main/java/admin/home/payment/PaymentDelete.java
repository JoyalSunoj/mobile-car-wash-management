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

@WebServlet("/paymentDelete")
public class PaymentDelete extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String USER = "root";
	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

			int paymentId = Integer.parseInt(request.getParameter("payment-id"));

			PreparedStatement pst = con.prepareStatement("DELETE FROM payment_details WHERE payment_id=?");

			pst.setInt(1, paymentId);

			int flag = pst.executeUpdate();

			if (flag > 0) {
				response.sendRedirect("payment");
				return;
			}

			out.println("<html><body style='font-family:Arial;background-color:#f4f4f4;'>");
			out.println("<h2 style='color:red;text-align:center;'>Unable to Delete Payment!</h2>");
			out.println("<a href='payment' style='text-decoration:none;display:block;text-align:center;'>Back</a>");
			out.println("</body></html>");

			con.close();

		} catch (Exception e) {
			out.print("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
