package hbv;

import java.io.*;
import java.time.*;
import java.time.format.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.nio.file.*;

import java.awt.image.*;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;


public class QrCodeServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, 
      HttpServletResponse response)
      throws IOException, ServletException {
      HttpSession session = request.getSession(true); 
      String useremail = (String)session.getAttribute("useremail"); 
      if(useremail != null) {
        try{
          String buchungsID = request.getParameter("buchungsID");
          SqlRequestData sql = new SqlRequestData();
          String[][] booking = sql.requestSqlValues("SELECT BuchungsID, Buchungsdatum, Vorname, Nachname, Impftermin, Uhrzeit, Stadt, Ort, bu.Bezeichnung AS Impfstoff"
                                                     + " FROM Buchung AS bu"
                                                     + " INNER JOIN Slot AS sl"
                                                     + " ON bu.SlotID=sl.SlotID"
                                                     + " INNER JOIN Impfzentrum AS iz"
                                                     + " ON bu.ImpfzentrumsID=iz.ImpfzentrumsID" 
                                                     + " INNER JOIN Impfstoff AS ist"
                                                     + " ON bu.Bezeichnung=ist.Bezeichnung"
                                                         + " WHERE bu.BuchungsID=" + buchungsID); 
          String information = getValues(booking);//Created the qr code with the booking info
          response.setContentType("image/png");
          int width=256,height=256;
          BitMatrix matrix = new MultiFormatWriter().encode(information, BarcodeFormat.QR_CODE, width, height);
          BufferedImage bi = MatrixToImageWriter.toBufferedImage(matrix);
          ImageIO.write(bi, "PNG", response.getOutputStream());
        }catch(Exception e){
          e.printStackTrace();
        }
      } else {
          response.sendRedirect("index.html");
      }
  }

  public String getValues(String[][] booking) {
      String dataset = "-- | Impftermin-Infos | ";
      for(int row = 1; row < booking.length; ++row) {
        for(int col = 0; col < booking[row].length; ++col) {
            dataset += booking[row][col] + " | ";
        }
      }
      return dataset + " --";
  }

}
