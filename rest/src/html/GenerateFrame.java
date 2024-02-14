package html;


public class GenerateFrame{

    String title;
    String cssFilePath;
    String body;

    public GenerateFrame(String title, String cssFilePath, String body) {
        this.title = title;
        this.cssFilePath = cssFilePath;
        this.body = body;
    }

    public String genFrame() {
        return "<html><head><meta charset='UTF-8' name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<link rel='stylesheet' type='text/css' href='" + cssFilePath + "'>"
            + "<title>" + title + "</title>"
            + "</head><body>" + body + "</body></html>"
            + "<script src='https://cdn.jsdelivr.net/npm/pikaday/pikaday.js'></script>"
            + "<link rel='stylesheet' type='text/css' href='https://cdn.jsdelivr.net/npm/pikaday/css/pikaday.css'>";
    }
}
