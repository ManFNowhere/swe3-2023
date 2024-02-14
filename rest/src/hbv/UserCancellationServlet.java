package hbv;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.time.*;
import java.sql.*;
import javax.sql.*;
import html.*;

import javax.naming.*;

public class UserCancellationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            response.setContentType("text/html");
            HttpSession session = request.getSession(true);
            
            
            String buchungsID = request.getParameter("buchungsID");//Booking id, required for the confirmation for cancelling a booking

            GenerateDiv div = new GenerateDiv();
            GenerateForm gf = new GenerateForm();

            String logo = div.genDiv("divHeader", new GenerateImage().genImage());//Create the logo bar on top

            //Navigation bar left
            String naviUser = div.genDiv("naviUser", gf.genEventButton("naviButton", "button",
            "onclick=\"document.location='userMenu'\"", "Usermenü"));   
            String naviApp = div.genDiv("naviApp", gf.genEventButton("naviButton", "button",
            "onclick=\"document.location='selectLocation'\"", "Terminauswahl"));
            String naviVer = div.genDiv("naviVer", gf.genEventButton("naviButton", "button",
            "onclick=\"document.location='manageAppointments'\"", "Terminverwaltung"));   
    
            //Navigation logout
            String naviLog = div.genDiv("naviLog",gf.genEventButton("naviButton", "button",
        "onclick=\"document.location='logoutUser'\"", "Logout"));  

            String navigation = div.genDiv("naviDiv", naviUser + naviApp + naviVer + naviLog);

            String head = div.genDiv("", new GenerateHeading().genLargeHeading("Stornierungsbestätigung"));
            String para = div.genDiv("", new GenerateParagraph().genParagraph("Sind Sie sich sicher, dass Sie eine Stornierung vornehmen möchten?"));
            
            //Create the confirm/decline buttons
            String yesBut = gf.genButton("buttonStyle", "submit", "buchungsID", buchungsID, "Bestätigen");
            String noBut = gf.genButton("buttonStyle", "submit", "buchungsID", "", "Abbrechen");
            String divButton = div.genDiv("divInput", yesBut + noBut);
            String butForm = gf.genForm("centerContent", "validateCancellation", "post", divButton);

            //Put all elements into the body and into a frame
            String body =  div.genDiv("divSiteContent", head + para + butForm);
            GenerateFrame html = new GenerateFrame("Stornierung", "style-team17.css", logo + navigation + body); 
            PrintWriter out = response.getWriter();
            out.println(html.genFrame());
            out.close();
    }
}
