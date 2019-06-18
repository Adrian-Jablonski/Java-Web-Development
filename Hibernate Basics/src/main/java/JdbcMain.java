import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;

public class JdbcMain {
    public static void main(String[] args) throws ClassNotFoundException {
        // TODO: Load the SQLite JDBC driver (JDBC class implements java.sql.Driver)

        // TODO: Create a DB connection
        try(Connection connection = null) {

            // TODO: Create a SQL statement

            // TODO: Create a DB table

            // TODO: Insert a couple contacts

            // TODO: Fetch all the records from the contacts table

            // TODO: Iterate over the ResultSet & display contact info

        } catch (SQLException ex) {
            // Display connection or query errors
            System.err.printf("There was a database error: %s%n",ex.getMessage());
        }
    }
}