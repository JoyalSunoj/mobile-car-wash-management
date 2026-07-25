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
//@WebServlet("/staffBookService")
//public class StaffBookService extends HttpServlet {
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
//			// Fetch services for dropdown
//			PreparedStatement pstService = con.prepareStatement("SELECT * FROM service_details");
//			ResultSet rsService = pstService.executeQuery();
//
//			// Fetch customers added by this staff
//			PreparedStatement pstCustomer = con
//					.prepareStatement("SELECT * FROM customer_details WHERE added_by_staff=?");
//			pstCustomer.setString(1, staffName);
//			ResultSet rsCustomer = pstCustomer.executeQuery();
//
//			out.println("<html><head><title>Book Service</title></head><body style='font-family:Arial,sans-serif;'>");
//			out.println("<h1>Book Service</h1>");
//			out.println("<a href='staff'>Back to Dashboard</a><br><br>");
//
//			// BOOK SERVICE FORM
//			out.println("<form method='post' action='staffBookServiceSave'>");
//			out.println("Select Customer: <select name='customer_id'>");
//			while (rsCustomer.next()) {
//				out.println("<option value='" + rsCustomer.getInt("customer_id") + "'>"
//						+ rsCustomer.getString("customer_name") + "</option>");
//			}
//			out.println("</select><br><br>");
//
//			out.println("Select Service: <select name='service_id'>");
//			while (rsService.next()) {
//				out.println(
//						"<option value='" + rsService.getInt("service_id") + "'>" + rsService.getString("service_name")
//								+ " - " + rsService.getString("service_rate") + "</option>");
//			}
//			out.println("</select><br><br>");
//
//			out.println("Date: <input type='date' name='service_date' required/><br><br>");
//			out.println("<input type='submit' value='Book Service'/>");
//			out.println("</form>");
//
//			out.println("</body></html>");
//			rsService.close();
//			pstService.close();
//			rsCustomer.close();
//			pstCustomer.close();
//			con.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
