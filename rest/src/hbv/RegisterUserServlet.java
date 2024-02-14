package hbv;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class RegisterUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
          
        String firstname = request.getParameter("firstname");  
        String surname = request.getParameter("surname");
        String useremail = request.getParameter("useremail");
        String userpassword = request.getParameter("userpassword");
        byte[] encodedarr = userpassword.getBytes("UTF-8");
        String encodedpw = Base64.getEncoder().encodeToString(encodedarr);

        try {
            String insert_user = 
                "INSERT INTO Nutzer (Vorname, Nachname, Mail, Passwort)" 
                    + "VALUES(" 
                    + "'" + firstname + "', "
                    + "'" + surname + "', " 
                    + "'" + useremail + "', "
                    + "'" + encodedpw + "');";
            Context initCtx = new InitialContext();
            DataSource ds = (DataSource)initCtx.lookup("java:/comp/env/jdbc/mariadb");
            Connection connection = ds.getConnection();
            PreparedStatement ps;
            ps = connection.prepareStatement(insert_user);  
            int rows = ps.executeUpdate(); 
            if(rows > 0) {
                HttpSession session = request.getSession(true); 
                session.setAttribute("useremail", useremail);
                session.setAttribute("firstname", firstname);
                session.setAttribute("surname", surname);
                response.sendRedirect("userMenu");
            }
            ps.close();
            connection.close();
            out.close();
        } catch (Exception exp) {
            response.setContentType("text/html");
            out.println("<script type=\"text/javascript\">");
            out.println("alert('E-Mail wurde registriert. Bitte verwenden Sie eine andere E-Mail.');");
            out.println("window.location.href='pages/login.html';");
            out.println("</script>");
        }      
        
    }
}
