package html;

public class GenerateHeading {


    public String genLargeHeading(String heading) {
        return "<h1>" + heading + "</h1>";
    }

    public String genMediumHeading(String heading) {
        return "<h2>" + heading + "</h2>";
    }

    public String genSmallHeading(String heading) {
        return "<h3>" + heading + "</h3>";
    }
}
