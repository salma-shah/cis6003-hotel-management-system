package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// this follows the singleton creational design pattern
public class DBConnection {

    // database connection details and credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ocean_view_hotel_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "admin12345678";
    private static volatile DBConnection instance;  // private static field

    // private constructor
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Error connecting to database" + e.getMessage());
        }
    }

    // public method to return the instance
    public static DBConnection getInstance() {
        if (instance == null) {
            // we make it synchorinzied
            // thread safe
            synchronized (DBConnection.class) {
                if (instance == null) {}
            instance = new DBConnection();
        }
        }
        return instance;
    }

    // method to establish the connection to the database
    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
        catch (SQLException ex) {
            throw new SQLException("Error getting connection to database" + ex.getMessage());
        }
    }

//    // closing the connection
//    public void closeConnection() {
//        try
//        {
//            if ( !connection.isClosed() || connection != null)
//            {
//                connection.close();
//                System.out.println("Connection closed successfully");
//            }
//        }
//        catch (Exception e)
//        {
//            System.err.println("There was an error: " + e.getMessage());
//        }
//    }
}
