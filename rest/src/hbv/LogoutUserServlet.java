package hbv;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class LogoutUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            HttpSession session = request.getSession(true);
            if(session.getAttribute("useremail") != null) {//Logout the user
                session.removeAttribute("userid");
                session.removeAttribute("useremail");
                session.removeAttribute("firstname");
                session.removeAttribute("surname");
                response.sendRedirect("pages/login.html");
            }else {
                response.sendRedirect("index.html");
            }
    }
}
