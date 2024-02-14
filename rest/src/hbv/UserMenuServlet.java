package hbv;
import java.io.*;
import java.time.*;
import java.time.format.*;

import html.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class UserMenuServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
      response.setContentType("text/html");
      
      HttpSession session = request.getSession(true);
      String userid = (String)session.getAttribute("userid");
      String useremail = (String)session.getAttribute("useremail");
      String firstname = (String)session.getAttribute("firstname");
      String surname = (String)session.getAttribute("surname");

      if(useremail != null) {//Check if the user is logged in, otherwise redirect
        GenerateDiv div = new GenerateDiv();
        GenerateForm gf = new GenerateForm();

        // String logo = div.genDiv("divHeader", new GenerateImage().genImage());//Create the logo bar on top
        
        // //Navigation bar left
        // String naviUser = div.genDiv("naviUser", "");
        // String naviApp = div.genDiv("naviApp", "");
        // String naviVer = div.genDiv("naviVer", "");

        //Navigation logout
        String naviLog = div.genDiv("naviLog", gf.genEventButton("naviButton", "button",
        "onclick=\"document.location='logoutUser'\"", "Logout")); 
        
        // String navigation = div.genDiv("naviDiv", naviUser + naviApp + naviVer + naviLog);
         

        String head = div.genDiv("", new GenerateHeading().genLargeHeading("Nutzer-Bereich - " + firstname + " " + surname));
        String para = div.genDiv("", new GenerateParagraph().genParagraph("Sie können hier Ihre Terminregistrierungen vornehmen und verwalten."));
        
        // //Navigation buttons in the user menu get created
        // String resBut = gf.genEventButton("buttonStyleBig", "button", "onclick=\"document.location='selectLocation'\"", "Terminreservierung");
        // String verBut = gf.genEventButton("buttonStyleBig", "button", "onclick=\"document.location='manageAppointments'\"", "Terminverwaltung");
        // String buttons = div.genDiv("centerContent", resBut + verBut);

        //Put all elements into the body and into a frame
        String body = div.genDiv("divSiteContent", head + para); //add button
        GenerateFrame html = new GenerateFrame("Nutzer-Menü", "style-team17.css", body);
        PrintWriter out = response.getWriter();
        out.println(html.genFrame());
        out.close();
      } else {
        response.sendRedirect("index.html");
      }
  }
}
