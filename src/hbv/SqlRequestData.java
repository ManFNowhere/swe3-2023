package hbv;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class SqlRequestData {

    //PreparedStatement for Data Manipulation (DML: Select, Insert, Update, ...) 
    //and Statement for Data Definition (DDL: Create, Alter, ...)

    public String[][] requestSqlValues(String query) {
        String[][] result = null;
        try {
            Context initCtx = new InitialContext();
            DataSource ds = (DataSource)initCtx.lookup("java:/comp/env/jdbc/mariadb");            
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.last();
            int rows = rs.getRow() + 1; //Get number of rows of the ResultSet and add 1 row to fit the head of the col too
            rs.beforeFirst();
            ResultSetMetaData rsm = rs.getMetaData();
            int columns = rsm.getColumnCount(); //Get the number of columns of the ResultSet 
            result = new String[rows][columns]; //A two dimensional array with the length of the ResultSet, to be flexible, regardless of the query
            for(int row = 0; row < result.length; ++row) {
                for(int col = 0; col < result[row].length; ++col) {
                    if(row == 0) {
                        result[row][col] = rsm.getColumnLabel(col + 1); //Get the column name and always save it first inside the array (start with 1 = first column)
                    }else {
                        result[row][col] = rs.getString(result[0][col]); //Get the corresponding values for each column 
                    }
                }
                rs.next(); //Check if there is another row and step into it, if it exists
            }
            rs.close();
            ps.close();
            conn.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    //Method for updating and inserting new values into the DB
    public int updateSqlValues(String query) {
       int rows = 0;
       try {
           Context initCtx = new InitialContext();
           DataSource ds = (DataSource)initCtx.lookup("java:/comp/env/jdbc/mariadb");            
           Connection conn = ds.getConnection();
           PreparedStatement ps = conn.prepareStatement(query);
           rows = ps.executeUpdate();
           ps.close();
           conn.close();
       } catch(Exception e) {
           e.printStackTrace();
       } 
       return rows;
    }
}
