package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import model.User;
import database.ConnectionManager;
import database.DatabasePrefs;
import database.PostgreSQLUserDAO;
import exceptions.DatabaseException;
import exceptions.UserNotFoundException;

public class PostgreSQLUserDAO implements UserDAO {

	private final ConnectionManager manager;
	private static PostgreSQLUserDAO instance;

	public static PostgreSQLUserDAO getInstance() {
		if (instance == null)
			instance = new PostgreSQLUserDAO();

		return instance;
	}

	private PostgreSQLUserDAO() {
		manager = new ConnectionManager(DatabasePrefs.driver, DatabasePrefs.connectionString, DatabasePrefs.username,
				DatabasePrefs.password);
	}

	@Override
	public User retrieve(String email) {
		User user = null;
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM Users WHERE email = ?");
			statement.setString(1, email);

			ResultSet results = statement.executeQuery();
			if (results.next())
				user = createUser(results);
			else
				throw new UserNotFoundException();
			connection.close();
		} catch (SQLException e) {
			throw new UserNotFoundException();
		}
		return user;
	}

	@Override
	public void save(User user) {
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement;
			if (user.isNew())
				statement = connection
					.prepareStatement("INSERT INTO Users(name, surname, password, email, secretquestion, secretanswer, birthDate, vip) values(?, ?, ?, ?, ?, ?, ?, ?)");
			else {
				statement = connection
						.prepareStatement("UPDATE Users SET name = ?, surname = ?, password = ?, email = ?, secretquestion = ?, secretanswer = ?, birthDate = ?, vip = ? WHERE email = ?");
				statement.setString(9, user.getEmail());
			}
			statement.setString(1, user.getName());
			statement.setString(2, user.getSurname());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getSecretQuestion());
			statement.setString(6, user.getSecretAnswer());
			statement.setDate(7, new java.sql.Date(user.getBirthDate().getTime()));
			statement.setBoolean(8, user.getVip());
			
			statement.execute();
			connection.commit();
			connection.close();
		} catch (SQLException e) { throw new DatabaseException(); }
	}

	private User createUser(ResultSet results) throws SQLException {
		int id = results.getInt(1);
		String name = results.getString(2);
		String surname = results.getString(3);
		String password = results.getString(4);
		String email = results.getString(5);
		String secretQuestion = results.getString(6);
		String secretAnswer = results.getString(7);
		Date birthDate = results.getDate(8);
		boolean vip = results.getBoolean(9);
		User user = new User(name, surname, email, password,
				secretQuestion, secretAnswer, birthDate, vip);
		user.setId(id);

		return user;
	}
	
}
