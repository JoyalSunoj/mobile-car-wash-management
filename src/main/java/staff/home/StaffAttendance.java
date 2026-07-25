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
//@WebServlet("/staffAttendance")
//public class StaffAttendance extends HttpServlet {
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
//			// Show attendance
//			PreparedStatement pst = con
//					.prepareStatement("SELECT * FROM attendance WHERE staff_name=? ORDER BY attendance_date DESC");
//			pst.setString(1, staffName);
//			ResultSet rs = pst.executeQuery();
//
//			out.println("<html><head><title>Attendance</title></head><body style='font-family:Arial,sans-serif;'>");
//			out.println("<h1>Your Attendance</h1>");
//			out.println("<a href='staff'>Back to Dashboard</a><br><br>");
//
//			// Attendance Form
//			out.println("<form method='post' action='staffAttendanceSave'>");
//			out.println("Date: <input type='date' name='attendance_date' required/><br>");
//			out.println(
//					"Status: <select name='status'><option value='Present'>Present</option><option value='Absent'>Absent</option></select><br>");
//			out.println("<input type='submit' value='Mark Attendance'/>");
//			out.println("</form><br>");
//
//			// Attendance Table
//			out.println("<table border='1' style='border-collapse:collapse;width:50%'>");
//			out.println(
//					"<tr style='background-color:#007bff;color:white;'><th>SL</th><th>Date</th><th>Status</th></tr>");
//			int i = 1;
//			while (rs.next()) {
//				out.println("<tr>");
//				out.println("<td>" + i + "</td>");
//				out.println("<td>" + rs.getDate("attendance_date") + "</td>");
//				out.println("<td>" + rs.getString("status") + "</td>");
//				out.println("</tr>");
//				i++;
//			}
//			out.println("</table></body></html>");
//
//			rs.close();
//			pst.close();
//			con.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
