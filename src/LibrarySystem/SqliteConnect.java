package LibrarySystem;

import java.sql.*;
import java.sql.DriverManager;

public class SqliteConnect {
		
		public static Connection connect() {
			Connection conn = null;
	        try {
	        	// Load Sqlite JDBC Driver
	            Class.forName("org.sqlite.JDBC");
	            String url = "jdbc:sqlite:C:/Users/Kevin Nguyen/eclipse-workspace/FinalProject/src/LibraryDatabase.db";
	            
	            // Create a connection to the database
	            conn = DriverManager.getConnection(url);           
	            System.out.println("Connection to SQLite has been established.");
	            
	        } catch (Exception e) {
	            	return null;
	        }
			return conn;
	    }
}
