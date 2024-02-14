package html;

public class GenerateDropdown {


    public String genDropdown(String name, String label, String attr, String sessionAttr, String[][] sqlValues) {
        StringBuilder html = new StringBuilder();
        html.append("<label for='" + name + "'>" + label + "</label>");
        html.append("<select name='"+ name + "' id='"+name+"' " + attr + "' required>");
        html.append("<option value='' disabled selected>Wählen</option>");
        if(sqlValues != null) {
            genSelectOptions(sqlValues, sessionAttr, html);
        }
        html.append("</select>");
        return html.toString();
    }

    public void genSelectOptions(String[][] sqlValues, String sessionAttr, StringBuilder html) { 
        int columnLength = sqlValues[0].length - 1; 
        for(int row = 1; row < sqlValues.length; ++row) { // row = 1 to skip the table header with 
            if(sessionAttr != null && sessionAttr.contentEquals(sqlValues[row][0])) {
                html.append("<option value='" + sqlValues[row][0] + "' selected>" + sqlValues[row][columnLength] + "</option>");
            }else {
                html.append("<option value='" + sqlValues[row][0] + "'>" + sqlValues[row][columnLength] + "</option>");
            }
        }   
    }
}
