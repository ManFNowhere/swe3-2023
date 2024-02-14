package hbv;

import java.time.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ValidateCancellationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            String buchungsID = request.getParameter("buchungsID");  
            if(buchungsID != null) { //Confirm the cancelling of a booking, if the booking id is set, otherwise redirect
                String today = new CalculateCalendar().getTodaysDate().toString();
                SqlRequestData sql = new SqlRequestData();
                sql.updateSqlValues("INSERT INTO Stornierung (BuchungsID, Stornierungsdatum)"
                                      + " VALUES(" + buchungsID + ", '" + today + "');");       
            }
            response.sendRedirect("manageAppointments");
    }
}
