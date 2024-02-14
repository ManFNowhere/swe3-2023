package hbv;

import java.io.*;
import java.time.*;
import java.time.format.*;

import html.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ManageAppointmentsServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession(true);
      String useremail = (String)session.getAttribute("useremail");
      if(useremail != null) {//Check, if the user is logged in, otherwise redirect
        String userid = (String)session.getAttribute("userid");

        if(userid == null || userid == "") {//Get the userID after registration
          String uQuery = "SELECT UserID FROM Nutzer WHERE Mail='" + (String) session.getAttribute("useremail") + "'";
          String[][] sqlValues = new SqlRequestData().requestSqlValues(uQuery);
          userid = sqlValues[1][0];
        }
        
        String firstname = (String)session.getAttribute("firstname");
        String surname = (String)session.getAttribute("surname");
        SqlRequestData sql = new SqlRequestData();
        //Request the corresponding data for the user (bookings and cancellations), to display them
        String[][] bookings = sql.requestSqlValues("SELECT BuchungsID, Buchungsdatum, Vorname, Nachname, Impftermin, Uhrzeit, Stadt, Ort, bu.Bezeichnung AS Impfstoff"
                                                     + " FROM Buchung AS bu"
                                                     + " INNER JOIN Slot AS sl"
                                                     + " ON bu.SlotID=sl.SlotID"
                                                     + " INNER JOIN Impfzentrum AS iz"
                                                     + " ON bu.ImpfzentrumsID=iz.ImpfzentrumsID" 
                                                     + " INNER JOIN Impfstoff AS ist"
                                                     + " ON bu.Bezeichnung=ist.Bezeichnung"
                                                         + " WHERE bu.UserID='" + userid + "'"
                                                         + " AND NOT EXISTS (SELECT * FROM Stornierung AS st"
                                                             + " WHERE bu.BuchungsID = st.BuchungsID)"
                                                             + " ORDER BY bu.Buchungsdatum DESC, Impftermin DESC;"); 
        String[][] cancellations = sql.requestSqlValues("SELECT bu.BuchungsID, Stornierungsdatum, Buchungsdatum, Vorname, Nachname, Impftermin, Uhrzeit, Stadt, Ort, bu.Bezeichnung AS Impfstoff"
                                                     + " FROM Stornierung AS st"
                                                     + " INNER JOIN Buchung AS bu"
                                                     + " ON st.BuchungsID=bu.BuchungsID"
                                                     + " INNER JOIN Slot AS sl"
                                                     + " ON bu.SlotID=sl.SlotID"
                                                     + " INNER JOIN Impfzentrum AS iz"
                                                     + " ON bu.ImpfzentrumsID=iz.ImpfzentrumsID" 
                                                     + " INNER JOIN Impfstoff AS ist"
                                                     + " ON bu.Bezeichnung=ist.Bezeichnung"
                                                         + " WHERE bu.UserID='" + userid + "'"
                                                         + " ORDER BY Stornierungsdatum DESC, bu.Buchungsdatum DESC, Impftermin DESC;"); 
        

        GenerateDiv div = new GenerateDiv();
        GenerateForm gf = new GenerateForm();
        
        String logo = div.genDiv("divHeader", new GenerateImage().genImage());

        //Navigation bar left
        String naviUser = div.genDiv("naviUser", gf.genEventButton("naviButton", "button",
        "onclick=\"document.location='userMenu'\"", "Usermenü"));   
        String naviApp = div.genDiv("naviApp", gf.genEventButton("naviButton", "button",
        "onclick=\"document.location='selectLocation'\"", "Terminauswahl"));
        String naviVer = div.genDiv("naviVer", gf.genEventButton("naviButton", "button",
        "onclick=\"document.location='#'\"", "Terminverwaltung"));   //add manageAppointment later
    
        //Navigation logout
        String naviLog = div.genDiv("naviLog",gf.genEventButton("naviButton", "button",
        "onclick=\"document.location='logoutUser'\"", "Logout"));  

        String navigation = div.genDiv("naviDiv", naviUser + naviApp + naviVer + naviLog);
        
        String head = div.genDiv("", new GenerateHeading().genLargeHeading("Terminverwaltung - " + firstname + " " + surname));
        
        GenerateAppointments ga = new GenerateAppointments();
        String bookHead = div.genDiv("", new GenerateHeading().genMediumHeading("Buchungen"));
        String bookTable = div.genDiv("divTableBook", ga.genBookings(bookings));


        String cancelHead = new GenerateHeading().genMediumHeading("Stornierungen");
        String cancelTable = div.genDiv("divTableBook", ga.genCancellations(cancellations));

        //Generate the page with the requested data
        
        String body = div.genDiv("divSiteContent", head + bookHead + bookTable + cancelHead + cancelTable); 
        GenerateFrame html = new GenerateFrame("Terminverwaltung", "style-team17.css", logo + navigation + body);
        out.println(html.genFrame());
      } else {
        response.sendRedirect("index.html");
      }
      out.close();
  }
}
