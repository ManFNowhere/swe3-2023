package hbv;

import java.io.*;
import java.util.*;
import java.time.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ValidateAppointmentServlet extends HttpServlet {

    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(true);

        //Get all the data, required for a booking
        CalculateCalendar calculate = (CalculateCalendar) session.getAttribute("calendar");

        String today = calculate.getTodaysDate().toString();//Get the current date as a string
        String patientFN = request.getParameter("patientFN");
        String patientSN = request.getParameter("patientSN");

        String vacDate = (String) session.getAttribute("date");
        

        String vaccine = (String) session.getAttribute("vaccine");
        String vQuery = "SELECT Bezeichnung FROM Impfstoff;";

        String locationID = (String) session.getAttribute("location");
        String lQuery = "SELECT ImpfzentrumsID FROM Impfzentrum;";

        String slotID = (String) session.getAttribute("slotID");  
        String sQuery = "SELECT SlotID FROM Slot WHERE ImpfzentrumsID='" + locationID + "'";

        String userID = (String) session.getAttribute("userid");

        //Get the userID after registration
        if(userID == null || userID == "") {
            String uQuery = "SELECT UserID FROM Nutzer WHERE Mail='" + (String) session.getAttribute("useremail") + "'";
            String[][] sqlValues = new SqlRequestData().requestSqlValues(uQuery);
            userID = sqlValues[1][0];
        }
       
        //Check the inserted data for errors and compare it with the values in the data base (day out of bounds of a month etc. (against injections of html code in the browser))
        if(validateDate(vacDate, calculate) && validateSqlData(vaccine, vQuery) && validateSqlData(slotID, sQuery) && patientFN != "" && patientSN != "") {
            String query = "INSERT INTO Buchung (Buchungsdatum, Vorname, Nachname, Impftermin, SlotID, UserID, Bezeichnung, ImpfzentrumsID)"
            + " VALUES ('" + today + "', '" + patientFN + "', '" + patientSN + "', '" + vacDate + "', '" 
            + slotID + "', '" + userID + "', '" + vaccine + "', '" + locationID + "');";
            int rows = new SqlRequestData().updateSqlValues(query);
            session.removeAttribute("slotID");
            session.removeAttribute("vaccine");
            session.removeAttribute("date");
            session.removeAttribute("locationID");
            response.sendRedirect("manageAppointments");
            
        }else {
            response.sendRedirect("bookingFailure.html");
        }
    }

    //To verify the date is an actual date
    private boolean validateDate(String date, CalculateCalendar calculate) {
        if(date != null) {
            int year = calculate.getYear();
            int month = calculate.getMonth() + 1;
            int daysInMonth = calculate.getDaysInMonth();
            String fM = calculate.getFormattedDate(month);
            for(int dayOfMonth = 1; dayOfMonth <= daysInMonth; ++dayOfMonth) {
                if(date.contentEquals(year + "-" + fM + "-" + calculate.getFormattedDate(dayOfMonth))) {
                    return true;
                }
            }
        }
        return false;
    }


    //To verify data with database values (wrong values, example vaccine name manipulated in html etc.)
    private boolean validateSqlData(String sAttr, String query) {
        String[][] sqlValues = new SqlRequestData().requestSqlValues(query);
        if(sqlValues != null) {
            for(int row = 1; row < sqlValues.length; ++row) { //row 1 -> To skip the column heading 
                for(int col = 0; col < sqlValues[row].length; ++col) {
                    if(sAttr.contentEquals(sqlValues[row][col])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

