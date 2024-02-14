package html;

public class GenerateAppointments { 


    //Generate the table of bookings for a user
    public String genBookings(String[][] values) {    
        String table = "<table>";
        for(int row = 0; row < values.length; ++row) {
            table += "<tr>";
            for(int col = 1; col < values[row].length; ++col) {
                if(row == 0) {
                    if(col < values[row].length - 1) {
                        table += "<th>" + values[row][col] + "</th>";
                    } else { 
                        table += "<th>" + values[row][col] + "</th>";
                        table += "<th>Stornierung</th>";
                        table += "<th>QR-Code</th>";
                    }  
                } else {
                    if(col < values[row].length - 1) {
                        table += "<td>" + values[row][col] + "</td>";
                    } else { 
                        table += "<td>" + values[row][col] + "</td>";
                        table += "<td><form style='display:inline;' action='userCancellation' method='post'>"
                            + "<button type='submit' name='buchungsID' value='" + values[row][0] + "'>stornieren</button>"
                            + "</form>"
                            + "</td>";
                        table += "<td><form style='display:inline;' action='qrCode' method='post'>"
                            + "<button type='submit' name='buchungsID' value='" + values[row][0] + "'>öffnen</button>"
                            + "</form>"
                            + "</td>";
                    } 
                }   
            } 
            table += "</tr>";
        }
        table += "</table>";
        return table;
    }

    //Generate the table with a cancelled bookings of a user
   public String genCancellations(String[][] values) {
    String table = "<table>";
    for(int row = 0; row < values.length; ++row) {
        table += "<tr>";
        for(int col = 1; col < values[row].length; ++col) {
            if(row == 0) {
                table += "<th>" + values[row][col] + "</th>";
            } else {
                table += "<td>" + values[row][col] + "</td>"; 
            }
        } 
        table += "</tr>";
    }
    table += "</table>";
    return table;
   } 
}
