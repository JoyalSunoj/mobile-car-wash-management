package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<html>");
        out.print("<head>");
        out.print("<title>Logged Out</title>");
        out.print("<script type='text/javascript'>"
                + "function preventBack() { window.history.forward(); }"
                + "setTimeout('preventBack()', 0);"
                + "window.onunload = function() { null };"
                + "setTimeout(function(){ window.location.href='index.html'; }, 1000);"
                + "</script>");
        out.print("</head>");
        out.print("<body>");
        out.print("</body>");
        out.print("</html>");

        out.close();
    }
}
