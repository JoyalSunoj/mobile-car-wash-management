package admin.home.flat;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/flatEdit")
public class FlatEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);

			String fid = request.getParameter("fid");
			int flatId = Integer.parseInt(fid);

			pst = con.prepareStatement("select*from flat_details where flat_id=?");
			pst.setInt(1, flatId);

			rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			out.println("<html><body>");
			out.println("<center>");

			out.println("<h1>WashOnWheels - Admin Portal</h1>");
			out.println("<h2>Welcome " + name + "</h2><br>");
			out.println("<hr>");
			out.println("<table border='2' width='100%' cellspacing='0'>");
			out.println("<tr>");
			out.println("<td align='center'><a href='admin'>Home</a></td>");
			out.println("<td align='center'><a href='customer'>Customer</a></td>");
			out.println("<td align='center'><a href='logout'>Logout</a></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<h1>Flat Details</h1>");
			out.println("<form name='flat' method='post' action='flatUpdate'>");

			while (rs.next()) {

				out.println("<input type='hidden' name='flat-id' value='" + rs.getInt(1) + "'/><br>");
				out.println("Flat Name: <input type='text' name='flat-name' value='" + rs.getString(2) + "'/><br><br>");
				out.println("Flat Address: <input type='text' name='flat-address' value='" + rs.getString(3)
						+ "'/><br><br>");
			}
			out.println("<input type='submit' value='Update'/>");
			out.println("<a href='flat'><button type='button' >Cancel</button></a>");
			out.println("</form>");

			out.println("</body></html>");

		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}