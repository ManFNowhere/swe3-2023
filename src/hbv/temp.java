package hbv;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class temp extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        
        // Mendapatkan nilai parameter dari input form
        String selectedDate = request.getParameter("date");
        String selectedTime = request.getParameter("time");
        String selectedVaccine = request.getParameter("vaccine");
        

        try {
            // Inisialisasi sumber data dan koneksi database
            Context initCtx = new InitialContext();
            DataSource ds = (DataSource)initCtx.lookup("java:/comp/env/jdbc/mariadb");
            Connection conn = ds.getConnection();
            // Mendapatkan nama pengguna dari sesi
            HttpSession session = request.getSession(false);
            String firstname = (String) session.getAttribute("firstname");
            String surname = (String) session.getAttribute("surname");
            String selectedLocation = (String) session.getAttribute("location");

            // Periksa jika nama pengguna tidak null
            if (firstname != null && surname != null) {
                // Membuat pernyataan SQL untuk memasukkan data booking
                String insertQuery = "INSERT INTO Buchung (Buchungsdatum, Vorname, Nachname, Impftermin, SlotID, UserID, Bezeichnung, ImpfzentrumsID) VALUES (CURDATE(), ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, firstname);
                pstmt.setString(2, surname);
                pstmt.setString(3, selectedDate);
                pstmt.setString(4, selectedTime);
                pstmt.setInt(5, getUserId(firstname, surname, conn));
                pstmt.setString(6, selectedVaccine);
                pstmt.setString(7, selectedLocation);
                       
                // Menjalankan pernyataan SQL untuk memasukkan data ke database
                int rowsAffected = pstmt.executeUpdate();
            
                
                // Mengarahkan pengguna ke halaman userMenu jika data berhasil dimasukkan
                if (rowsAffected > 0) {
                    response.sendRedirect("userMenu");
                } else {
                    response.getWriter().println("Failed to book appointment.");
                }

                // Menutup pernyataan
                pstmt.close();
            } else {
                response.getWriter().println("Failed to retrieve user information.");
            }

            // Menutup koneksi
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("An error occurred: " + e.getMessage());
        }
    }

    // Mendapatkan ID pengguna berdasarkan nama pengguna
    private int getUserId(String firstName, String lastName, Connection conn) throws SQLException {
        int userId = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Membuat pernyataan SQL untuk mencari pengguna berdasarkan nama pengguna
            String query = "SELECT UserID FROM Nutzer WHERE Vorname = ? AND Nachname = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            rs = pstmt.executeQuery();

            // Jika pengguna ditemukan, ambil UserID
            if (rs.next()) {
                userId = rs.getInt("UserID");
            }
        } finally {
            // Menutup pernyataan dan hasil set
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return userId;
    }

}
