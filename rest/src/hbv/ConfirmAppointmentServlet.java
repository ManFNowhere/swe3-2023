package hbv;

import java.io.*;
import java.util.*;

import html.*;

import java.time.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ConfirmAppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(true);
        

        
        session.setAttribute("date", request.getParameter("date"));
        String date = (String) session.getAttribute("date");//Appointment

        //Get the time using the slotID
        String slotID = (String) session.getAttribute("slotID");
        SqlRequestData sql = new SqlRequestData();
        String time = getIDs(sql.requestSqlValues("SELECT Uhrzeit FROM Slot WHERE SlotID='" + slotID + "'"));


        //Get the location of vacination with the correct ID
        String locationID = (String) session.getAttribute("location");
        String location = getIDs(sql.requestSqlValues("SELECT Ort FROM Impfzentrum WHERE ImpfzentrumsID='" + locationID + "'"));
        
        
        String bookingDate = new CalculateCalendar().getTodaysDate().toString();//Booking date

        
        String vaccine = (String) session.getAttribute("vaccine");//vaccine

        
        String userID = (String) session.getAttribute("userid");//UserID
        
        
        //Put all the required data into a list for easier use
        ArrayList<String> appointment = new ArrayList<String>();
        appointment.add(bookingDate);
        appointment.add(date);
        appointment.add(time);
        appointment.add(vaccine);
        appointment.add(location);



        //Get the page going
        GenerateDiv div = new GenerateDiv();
        GenerateForm gf = new GenerateForm();

        String logo = div.genDiv("divHeader", new GenerateImage().genImage());

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
             

        GenerateHeading gh = new GenerateHeading();
        String head = div.genDiv("", gh.genLargeHeading("Termin bestätigen"));

        GenerateParagraph gp = new GenerateParagraph();
        String paraAbove = div.genDiv("centerContent", gp.genParagraph("Hier sehen Sie noch einmal die Details zu den von Ihnen ausgewählten Buchungsdaten."));
        String paraUnder = div.genDiv("centerContent", gp.genParagraph("Geben Sie bitte noch an, auf welche Person die Buchung ausgestellt werden soll."
         + " Alle Pflichtfelder sind mit einem * markiert."));
        
        //Create the table with the booking info 
        GenerateConfirmAppointment gc = new GenerateConfirmAppointment(appointment);        
        String table = div.genDiv("divCenter", gc.genInfoTable());

        //Create the input fields for the name
        String firstname = div.genDiv("divInput", gf.genInput("labelConfirmApp", "patientFN",
         "Geben Sie bitte Ihren Vornamen ein:* ", "textfield", ""));
        String surname = div.genDiv("divInput", gf.genInput("labelConfirmApp",
         "patientSN", "Geben Sie bitte Ihren Nachnamen ein:* ", "textfield", ""));
        String button = div.genDiv("centerContent", gf.genButton("buttonStyle", "submit", "", "Bestätigen", "Bestätigen"));
        String formNames = gf.genForm("centerContent", "validateAppointment", "post", firstname  + surname +  button);

        String body =  div.genDiv("divSiteContent",  head + paraAbove + table + paraUnder + formNames);

        //Create the page
        GenerateFrame html = new GenerateFrame("Buchungsbestätigung", "style-team17.css", logo + navigation + body);
        PrintWriter out = response.getWriter();
        out.println(html.genFrame());
        out.close();
    }

    private String getIDs(String[][] sqlValues) {
        String id = "";
        if(sqlValues != null) {     
            for(int row = 1; row < sqlValues.length; ++row) {
                for(int col = 0; col < sqlValues[row].length; ++col) {
                    id = sqlValues[row][col]; 
                }
            }
        }
        return id;
    }
}
