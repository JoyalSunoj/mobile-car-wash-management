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

@WebServlet("/flat")
public class FlatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/luminar_servlet";
	final String User = "root";
	final String Password = "YOUR_MYSQL_PASSWORD_HERE";

	Connection con = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int i = 1;
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, User, Password);
			pst = con.prepareStatement("select*from flat_details");
			rs = pst.executeQuery();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Flat Management - WashOnWheels</title>");
			out.println("</head>");
			out.println("<body>");
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
			out.println("<form name='flat' method='post' action='flatSave'>");
			out.println("Flat Name: <input type='text' name='flat-name'/><br><br>");
			out.println("Flat Address: <input type='text' name='flat-address'/><br><br>");
			out.println("<input type='submit' value='Save'/>");
			out.println("</form>");
			out.println("<hr><br>");
			out.println("<table border='2' cellpadding='10' cellspacing='0'>");
			out.println("<tr><th>SLNO</th>");
			out.println("<th>FLAT NAME</th>");
			out.println("<th>FLAT ADDRESS</th>");
			out.println("<th colspan='2'>ACTION</th>");

			out.println("</tr>");
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString(2) + "</td>");
				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td><a href='flatEdit?fid=" + rs.getInt(1) + " '>Edit</a></td>");
				out.println("<td><a href='flatDelete?fid=" + rs.getInt(1) + " ' onclick=\"return confirm('Are you sure you want to delete this flat?');\">Delete</a></td>");
				out.println("</tr>");
				i++;
			}
			out.println("</table>");
			out.println("<center>");
			out.println("</body></html>");
		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
