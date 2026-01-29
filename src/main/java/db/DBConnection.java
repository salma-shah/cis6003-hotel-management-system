package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// this follows the singleton creational design pattern
public class DBConnection {

    // database connection details and credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ocean_view_hotel_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin12345678";

    private Connection connection;

    private DBConnection() {
        try {
            // this is the actual db connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to database successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Error connecting to database" + e.getMessage());
        }
    }

    // but this is the INNER static field which holds the single instance
    private static DBConnection instance = new DBConnection();

    // public method to return the instance
    public static DBConnection getInstance() {
        return instance;
    }

    // method to access the jdbc connection instance
    public Connection getConnection() {
        try {
            // checking if conn is still open
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection was close ; re-establishing connection");
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            }
        }
        catch (Exception e) {
            System.err.println("There was an error: " + e.getMessage());
        }
        return connection;
    }

    // closing the connection
    public void closeConnection() {
        try
        {
            if ( connection != null || !connection.isClosed())
            {
                connection.close();
                System.out.println("Connection closed successfully");
            }
        }
        catch (Exception e)
        {
            System.err.println("There was an error: " + e.getMessage());
        }
    }
}
