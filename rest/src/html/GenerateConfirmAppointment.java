package html;

import java.util.*;

public class GenerateConfirmAppointment {

    ArrayList<String> appointment;

    public GenerateConfirmAppointment(ArrayList<String> appointment) {
        this.appointment = appointment;
    }

    //Generate the table to display the current booking info, before submitting it
    public String genInfoTable(){
        StringBuilder html = new StringBuilder();
        html.append("<table><tr>");
        html.append("<th>Buchungsdatum</th>");
        html.append("<th>Impftermin</th>");
        html.append("<th>Uhrzeit</th>");
        html.append("<th>Impfstoff</th>");
        html.append("<th>Impfzentrum</th></tr><tr>");
        for(String value : appointment) {
            html.append("<td>" + value + "</td>");
        }
        html.append("</tr></table>");
        return html.toString();
    }  
}
