package hbv;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class LoginUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            response.setContentType("text/html");
            
            //Get the necessary parameters and check, if they are valid
            String useremail = request.getParameter("useremail");
            String userpassword = request.getParameter("userpassword");  
            byte[] encodedarr = userpassword.getBytes("UTF-8");
            String encodedpw = Base64.getEncoder().encodeToString(encodedarr);
            PrintWriter out = response.getWriter();
            try {
                String login_request = 
                  "SELECT UserID, Vorname, Nachname FROM Nutzer " 
                      + "WHERE Mail='" + useremail + "' "
                      + "AND Passwort='" + encodedpw + "';";
                Context initCtx = new InitialContext();
                DataSource ds = (DataSource)initCtx.lookup("java:/comp/env/jdbc/mariadb");
                Connection connection = ds.getConnection();
                Statement stat;
                stat = connection.createStatement();  
                ResultSet rs = stat.executeQuery(login_request);    
                if(rs.next()) { //If the parameters are valid, then get a new session and set the attributes accordingly
                    HttpSession session = request.getSession(true);     
                    session.setAttribute("userid", rs.getString("UserID"));
                    session.setAttribute("useremail", useremail);
                    session.setAttribute("firstname", rs.getString("Vorname"));
                    session.setAttribute("surname", rs.getString("Nachname"));
                    response.sendRedirect("userMenu");
                } else {
                   response.setContentType("text/html");
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Falsche E-Mail-Adresse oder falsches Passwort. Bitte versuchen Sie es erneut');");
                    out.println("window.location.href='pages/login.html';");
                    out.println("</script>");
                }
                rs.close();
                stat.close();
                connection.close();
                out.close();
            } catch(Exception exp) {
                out.println(exp);
                exp.printStackTrace(out);
                out.close();
            }
    }
}
