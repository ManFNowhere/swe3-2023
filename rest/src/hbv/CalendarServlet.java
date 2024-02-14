package hbv;

import java.io.*;
import java.util.*;

import html.*;


import java.text.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class CalendarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(true);
        
        
        String useremail = (String)session.getAttribute("useremail"); 
        if(useremail != null) {//Check for a login, otherwise redirect
            String prev = request.getParameter("prev");
            String next = request.getParameter("next");
                 
            String slotID = request.getParameter("time");
            String vaccine = request.getParameter("vaccine");

            //Set the session variables, only if the parameters aren't null
            if(slotID != null && vaccine != null) {
                session.setAttribute("slotID", slotID);
                session.setAttribute("vaccine", vaccine);
            }

            String vac = (String) session.getAttribute("vaccine");//vaccine
            String sID = (String) session.getAttribute("slotID");//time slot

            //Check if the specified attributes are set, if not, redirect to the previous selection
            if((vac == null || sID == null) || ("Wählen".equals(slotID) || "Wählen".equals(vaccine))) {
                session.setAttribute("error", "error");
                response.sendRedirect("selectLocation"); 
            }

            String locationID = (String) session.getAttribute("location");//location of vaccination
            
            CalculateCalendar defCal = new CalculateCalendar(); //to rebuild the current month on a page refresh
            //Get the calendar, that was created in the previous selection screen, to be globally available and keep it's state
            CalculateCalendar dyCal = (CalculateCalendar) session.getAttribute("calendar");

            //Check the given parameters and display the calendar accordingly
            PrintWriter out = response.getWriter();
            if(prev != null) {
                out.println(getCalendar(dyCal, prev, sID, locationID, useremail));
            }else if(next != null) {
                out.println(getCalendar(dyCal, next, sID, locationID, useremail));
            }else {
                out.println(getCalendar(defCal, null, sID, locationID, useremail));
            }
            out.close();
        }else {
            response.sendRedirect("index.html");
        }  

    }

    private String getCalendar(CalculateCalendar calculate, String buttonValue, String sID, String locationID, String useremail) {
        if(buttonValue != null) {//Update the month, if either previous or next was pressed  
            calculate.setMonth(Integer.parseInt(buttonValue));
        }
        
        //SQL query to determine, whether or not there are free spots 
        int daysInMonth = calculate.getDaysInMonth();
        int month = calculate.getMonth() + 1;
        String formattedMonth = calculate.getFormattedDate(month);
        int year = calculate.getYear();
        String firstDateOfMonth = year + "-" + formattedMonth + "-" + "01";   
        String lastDateOfMonth = year + "-" + formattedMonth + "-" + daysInMonth;
        String query = "SELECT Buchung.Impftermin FROM Buchung INNER JOIN Slot ON Buchung.SlotID = Slot.SlotID"
            +   " WHERE Buchung.Impftermin<='" + lastDateOfMonth + "' AND Buchung.Impftermin>='" + firstDateOfMonth + "'"
            +   " AND Slot.SlotID='" + sID + "'"
            +   " AND NOT EXISTS (SELECT * FROM Stornierung WHERE Buchung.BuchungsID=Stornierung.BuchungsID)";
        //Request the bookings and the number of vaccination spots in the set location
        SqlRequestData sql = new SqlRequestData();
        String[][] capacity = sql.requestSqlValues("SELECT Impfkapazität FROM Impfzentrum WHERE ImpfzentrumsID='" + locationID +"'");
        String[][] sqlBookings = sql.requestSqlValues(query);

        //Save the bookings in an ArrayList for better performance for later iteration -> foreach
        ArrayList<String> bookings = new ArrayList<String>();
        if(bookings != null) {
            for(int row = 1; row < sqlBookings.length; ++row) {
                for(int col = 0; col < sqlBookings[row].length; ++col) {
                    bookings.add(sqlBookings[row][col]);
                }
            }
        }

        //Get the capacity out of the array, while iterating over it, just in case of an error -> should only return a single value
        int cap = 0;
        if(capacity != null) {
            for(int row = 1; row < capacity.length; ++row) {
                for(int col = 0; col < capacity[row].length; ++col) {
                    cap = Integer.parseInt(capacity[row][col]);
                }
            }
        }

        //Build the componants of the site
        GenerateDiv div = new GenerateDiv();
        GenerateForm gf = new GenerateForm();
        
        String logo = div.genDiv("divHeader", new GenerateImage().genImage());//Headcontainer with the HS logo

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
             
        GenerateHeading genHead = new GenerateHeading();
        String heading = div.genDiv("", genHead.genLargeHeading("Terminauswahl"));
        
        GenerateParagraph genPara = new GenerateParagraph();
        String paragraph = div.genDiv("", genPara.genParagraph("Wählen Sie ein Datum Ihrer Wahl aus."
        + " Hierfür klicken Sie einfach auf ein Feld im Kalendar, welches weiß ist."
        + " Die Felder im Kalender, welche ausgegraut sind, haben an diesem Tag, keine freien Buchungsplätze mehr."
        + " Falls Sie einen Termin an einem bestimmten Tag suchen und dieser Tag ist in dem von Ihnen vorgezogenen Impfzentrum bereits belegt,"
        + " dann wählen Sie auf der Seite der Impfzentrumsauswahl ein anderes Impfzentrum aus. Vielen Dank!"));

        //Generate the calendar with the acquired data 
        GenerateCalendar gc = new GenerateCalendar(calculate, bookings, cap);
        String calHead = gc.genCalHead();
        String calWeek = gc.genWeekDays();
        String calBody = gc.genCalBody();
        String calendar = div.genDiv("divCenter", calHead + calWeek + calBody);


        String body =  div.genDiv("divSiteContent", heading + paragraph + calendar);

        //Generate the page with all it's components
        GenerateFrame frame = new GenerateFrame("Terminauswahl", "style-team17.css", logo + navigation + body); 
        return frame.genFrame();
    } 


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }
}
