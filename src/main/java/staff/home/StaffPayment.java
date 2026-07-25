//package staff.home;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//@WebServlet("/staffPayment")
//public class StaffPayment extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	final String DRIVER = "com.mysql.cj.jdbc.Driver";
//	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
//	final String USER = "root";
//	final String PASSWORD = "YOUR_MYSQL_PASSWORD_HERE";
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		HttpSession session = request.getSession(false);
//		if (session == null || session.getAttribute("user") == null) {
//			response.sendRedirect("index.html");
//			return;
//		}
//		String staffName = (String) session.getAttribute("user");
//
//		try {
//			Class.forName(DRIVER);
//			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
//
//			PreparedStatement pst = con
//					.prepareStatement("SELECT p.payment_id, c.customer_name, s.service_name, p.amount, p.payment_date "
//							+ "FROM payment_details p " + "JOIN customer_details c ON p.customer_id=c.customer_id "
//							+ "JOIN service_details s ON p.service_id=s.service_id " + "WHERE p.added_by_staff=?");
//			pst.setString(1, staffName);
//			ResultSet rs = pst.executeQuery();
//
//			out.println("<html><head><title>Payments</title></head><body style='font-family:Arial,sans-serif;'>");
//			out.println("<h1>Payments Collected by You</h1>");
//			out.println("<a href='staff'>Back to Dashboard</a><br><br>");
//
//			out.println("<table border='1' style='border-collapse:collapse;width:80%'>");
//			out.println(
//					"<tr style='background-color:#007bff;color:white;'><th>SL</th><th>Customer</th><th>Service</th><th>Amount</th><th>Date</th></tr>");
//			int i = 1;
//			while (rs.next()) {
//				out.println("<tr>");
//				out.println("<td>" + i + "</td>");
//				out.println("<td>" + rs.getString("customer_name") + "</td>");
//				out.println("<td>" + rs.getString("service_name") + "</td>");
//				out.println("<td>" + rs.getDouble("amount") + "</td>");
//				out.println("<td>" + rs.getDate("payment_date") + "</td>");
//				out.println("</tr>");
//				i++;
//			}
//			out.println("</table>");
//			out.println("</body></html>");
//
//			rs.close();
//			pst.close();
//			con.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
