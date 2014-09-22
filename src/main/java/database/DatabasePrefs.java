package database;

public class DatabasePrefs {

	protected static String driver = "org.postgresql.Driver";
	protected static String connectionString;
	protected static String username;
	protected static String password;
	public static DatabasePrefs instance;

	public static void initialize(String connectionString,
			String username, String password) {
		if (instance == null)
			instance = new DatabasePrefs(connectionString, username, password);

	}

	private DatabasePrefs(String connectionString, String username,
			String password) {
		DatabasePrefs.connectionString = connectionString;
		DatabasePrefs.username = username;
		DatabasePrefs.password = password;
	}
	
}
