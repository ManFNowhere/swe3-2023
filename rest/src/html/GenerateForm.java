package html;

public class GenerateForm {

    public String genForm(String cName, String action, String method, String content) {
        return "<form class='" + cName + "'action='" + action + "' method='" + method + "'>"
            + "" + content + "</form>";
    }

    public String genInput(String cName, String name, String content, String type, String value) {
        return "<label class='" + cName + "'for='" + name + "'>" + content + "</label>"
            + "<input type='" + type + "' name='" + name + "' value='" + value + "'>";
    }

    public String genButton(String cName, String type, String name, String value, String content) {
        return "<button class='" + cName + "'type='" + type + "' name='" + name + "' value='" + value + "'>" + content + "</button>";
    }

    public String genEventButton(String cName, String type, String event, String content) {
        return "<button class='" + cName + "'type='" + type + "'" + event + ">" + content + "</button>";
    }
}
