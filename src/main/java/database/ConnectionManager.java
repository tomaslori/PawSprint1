package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import dao.DatabaseException;

public class ConnectionManager {

	private String username;
	private String password;
	private String connectionString;

	public ConnectionManager(String driver, String connectionString,
			String username, String password) {
		this.connectionString = connectionString;
		this.username = username;
		this.password = password;
		
		// TODO: Excepciones piola
//		try {
//			Class.forName(driver);
//		} catch (ClassNotFoundException e) {
//			throw new DatabaseException(e.getMessage(), e);
//		}
	}

	public Connection getConnection() {
		try {
			Connection connection = DriverManager.getConnection(
					connectionString, username, password);
			connection.setAutoCommit(false);
			return connection;
		} catch (SQLException e) {
			return null; //TODO: sacar esto!!
		//	throw new DatabaseException(e.getMessage(), e);
		}
	}
	
}
