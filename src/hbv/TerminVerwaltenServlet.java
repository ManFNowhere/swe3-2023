package hbv;

import java.io.*;
import java.time.*;
import java.time.format.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class TerminVerwaltenServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession(true);
        
        //HTML HEAD
        String head = "<html><head><meta charset='UTF-8' name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<link rel='stylesheet' type='text/css' href='css/team12.css'>"
            + "<title>Termin - Verwalten</title>"
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
        String text = "<h1>Terminverwaltung</h1><p>Suchen Sie Ihre Termin.</p>";
        
        //Form Input
        //input Nachname
        String nachnameInput = "<label class='divInputForm'for='nachname'>Nachname : </label>"
            + "<input type='text' name='nachname' value='' required>";
        String nInput = "<div class='centerForm'>" + nachnameInput + "</div>";

        //input geburtsdatum
        String geburtsdatumInput = "<div class='divInputForm'><label for='geburtsdatum'>geburtsdatum :</label><input type='text' id='geburtsdatum' name='geburtsdatum' required></div>";
        String gdInput = "<div class='centerForm'>" + geburtsdatumInput + "</div>";
       
     

        //Buchen Button
        String button = "<div class='centerForm'><button class='buttonStyleBig' type='submit' >Suchen</button></div>";
        //div form
        String form = "<form class=''action='' method='post'>"+ nInput + gdInput + button + "</form>";
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
