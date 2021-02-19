//this file allows for connection to configured DB. 
//if there is no successful connection, the system will print errror msg.

import java.sql.*;

//connect to database class
public class ConnectDb {
	public static Connection connect() {
		//establish connection 
		Connection c = null;
		try {
		
			Class.forName("org.sqlite.JDBC");
		
			c =DriverManager.getConnection("jdbc:sqlite:autosDB.sqlite");
			System.out.println("SQLite DB Connected");
		
		} catch(Exception e) {
			System.out.println(e);
		}
		return c;
	
	}

}
