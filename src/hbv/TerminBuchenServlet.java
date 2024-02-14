package hbv;

import java.io.*;
import java.time.*;
import java.time.format.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class TerminBuchenServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(true);
        // String useremail = (String)session.getAttribute("useremail");
        // SqlRequestData sql = new SqlRequestData();
        // String cityPara = request.getParameter("city");
        // String locationPara = request.getParameter("location");
        // //To reset the dropdowns on a hard refresh of the website, when the condition is true, 
        // if(cityPara == null && locationPara == null) {
        //     session.removeAttribute("city");
        //     session.removeAttribute("location");
        // }
        
        // //To set the selected options
        // if(cityPara != null) { 
        //     session.setAttribute("city", cityPara);
        //     cityPara = null; //Set to null for a refresh of a page, without using the dropdown
        // }
        // if(locationPara != null) {
        //     session.setAttribute("location", locationPara);
        // }


        //HTML HEAD
        String head = "<html><head><meta charset='UTF-8' name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<link rel='stylesheet' type='text/css' href='css/team12.css'>"
            + "<title>Termin - Buchen</title>"
            + "<link rel='stylesheet' type='text/css' href='https://cdn.jsdelivr.net/npm/pikaday/css/pikaday.css'>"
            + "<script src='https://cdn.jsdelivr.net/npm/pikaday/pikaday.js'></script>"
            + "</head><body>";



        //Calener Script
        String calenderScript = "<script>\n" +
                        "document.addEventListener('DOMContentLoaded', function() {\n" +
                        "    var dateInput = document.getElementById('date');\n" +
                        "    var geburtsdatumInput = document.getElementById('geburtsdatum');\n" +
                        "    var picker = new Pikaday({\n" +
                        "        field: dateInput,\n" +
                        "        format: 'YYYY-MM-DD',\n" +
                        "        onSelect: function(date) {\n" +
                        "            dateInput.value = picker.toString();\n" +
                        "        }\n" +
                        "    });\n" +
                        "       var picker2 = new Pikaday({\n" +
                        "        field: geburtsdatumInput,\n" +
                        "        format: 'YYYY-MM-DD',\n" +
                        "        onSelect: function(date) {\n" +
                        "            geburtsdatumInput.value = picker.toString();\n" +
                        "        }\n" +
                        "    });\n" +
                        "    dateIngeburtsdatumInputput.addEventListener('click', function() {\n" +
                        "        picker2.show();\n" +
                        "    });\n" +
                        "});\n" +
                        "</script>";


        // create logo hs-logo
        String logo = "<div class='hs-logo'><img src='images/hs-logo.png' alt='Hochschule Bremerhaven Logo'></img></div>";//Create the logo bar on top
        // create text content
        String text = "<h1>Terminauswahl</h1><p>Auf dieser Seite wählen Sie bitte die Stadt und das Impfzentrum, sowie den bevorzugten Impfstoff und die Uhrzeit aus.</p>";
        
        //Form Input
        //input Nachname
        String nachnameInput = "<label class='divInputForm'for='nachname'>Nachname : </label>"
            + "<input type='text' name='nachname' value='' required>";
        String nInput = "<div class='centerForm'>" + nachnameInput + "</div>";

        //input geburtsdatum
        String geburtsdatumInput = "<div class='divInputForm'><label for='geburtsdatum'>geburtsdatum :</label><input type='text' id='geburtsdatum' name='geburtsdatum' required></div>";
        String gdInput = "<div class='centerForm'>" + geburtsdatumInput + "</div>";
        
        //City dropdown generation
        // String sAttrC = (String) session.getAttribute("city");
        // String[][] sqlValues = doSqlQuery("", "SELECT DISTINCT Stadt FROM Impfzentrum;");     
        String cityDrop = "<div class='divInputForm '> " 
        +"<label for='city'>Wählen Sie die Stadt aus: </label> "
        +"<select name='city' id='city' required> "
        +"<option value='' disabled selected>Wählen</option> "
        +"<option value='' selected></option></select></div>";
        //City dropdown generation
        // String sAttrC = (String) session.getAttribute("city");
        // String[][] sqlValues = doSqlQuery("", "SELECT DISTINCT Stadt FROM Impfzentrum;");     
        String locDrop = "<div class='divInputForm '> " 
        +"<label for='impfzentrum'>Wählen Sie das Impfzentrum aus:</label> "
        +"<select name='impfzentrum' id='impfzentrum' required> "
        +"<option value='' disabled selected>Wählen</option> "
        +"<option value='' selected></option></select></div>";
        
        

        //Vaccine dropdown generation
        // String sqlValues = doSqlQuery(sAttrL, "SELECT Bezeichnung FROM Impfangebot WHERE ImpfzentrumsID='" + sAttrL + "'");
        String vacDrop = "<div class='divInputForm '> " 
        +"<label for='impfstoff'>Wählen Sie das Impfzentrum aus:</label> "
        +"<select name='impfstoff' id='impfstoff' required> "
        +"<option value='' disabled selected>Wählen</option> "
        +"<option value='' selected></option></select></div>";

        //Date input
        String calendarInput = "<div class='divInputForm'><label for='date'>Wählen Sie das Datum aus:</label><input type='text' id='date' name='date' required></div>";
        //Time dropdown generation
        // String sqlValues = doSqlQuery(sAttrL, "SELECT SlotID, Uhrzeit FROM Slot WHERE ImpfzentrumsID='" + sAttrL + "';");
        String timeDrop = "<div class='divInputForm '> " 
        +"<label for='time'>Wählen Sie die Zeituhr aus:</label> "
        +"<select name='time' id='time' required> "
        +"<option value='' disabled selected>Wählen</option> "
        +"<option value='' selected></option></select></div>";

     

        //Buchen Button
        String button = "<div class='centerForm'><button class='buttonStyleBig' type='submit' >Buchen</button></div>";
        //div form
        String form = "<form class=''action='' method='post'>"+ nInput + gdInput + cityDrop + locDrop + vacDrop + calendarInput + timeDrop + button + "</form>";
        //Main Conten Div
        String bodyContent = "<div class='terminauswahlContent'>" + text + form +"</div>";
        //body div
        String body = "<div class='divSiteConten'> "+bodyContent+ logo +"</div>";


        //footer div
        String footer = calenderScript + "</body></html> ";
        PrintWriter out = response.getWriter();
        out.println(head+body+footer);
        out.close();
    }

    //Only do a SQL query, if the required parameter is set
    private String[][] doSqlQuery(String para, String query) {
        String[][] sqlValues = null;
        if(para != null) {
            sqlValues = new SqlRequestData().requestSqlValues(query);
        }
        return sqlValues;
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

}
