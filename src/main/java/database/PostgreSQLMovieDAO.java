package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import model.Movie;
import exceptions.DatabaseException;
import exceptions.MovieNotFoundException;
import exceptions.UserNotFoundException;

public class PostgreSQLMovieDAO implements MovieDAO {

	private final ConnectionManager manager;
	private static MovieDAO instance;

	public static MovieDAO getInstance() {
		if (instance == null)
			instance = new PostgreSQLMovieDAO();

		return instance;
	}

	private PostgreSQLMovieDAO() {
		manager = new ConnectionManager(DatabasePrefs.driver, DatabasePrefs.connectionString, DatabasePrefs.username,
				DatabasePrefs.password);
	}

	@Override
	public Movie retrieve(String name) {
		Movie movie = null;
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM Movies WHERE name = ?");
			statement.setString(1, name);

			ResultSet results = statement.executeQuery();
			if (results.next())
				movie = createMovie(results);
			else
				throw new UserNotFoundException();
			connection.close();
		} catch (SQLException e) {
			throw new UserNotFoundException();
		}
		return movie;
	}

	@Override
	public void save(Movie movie) {
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement;
			if (movie.isNew())
				statement = connection
					.prepareStatement("INSERT INTO Movies(name, releaseDate, genre, director, duration, description) values(?, ?, ?, ?, ?, ?)");
			else {
				statement = connection
						.prepareStatement("UPDATE Movies SET name = ?, releaseDate = ?, genre = ?, director = ?, duration = ?, description = ? WHERE name = ?");
				statement.setString(7, movie.getName());
			}
			statement.setString(1, movie.getName());
			statement.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
			statement.setString(3, movie.getGenre());
			statement.setString(4, movie.getDirector());
			statement.setInt(5, movie.getDuration());
			statement.setString(6, movie.getDescription());
			
			statement.execute();
			connection.commit();
			connection.close();
		} catch (SQLException e) { throw new DatabaseException(); }
	}

	private Movie createMovie(ResultSet results) throws SQLException {
		int id = results.getInt(1);
		String name = results.getString(2);
		String genre = results.getString(3);
		String director = results.getString(4);
		int duration = results.getInt(5);
		String description = results.getString(6);
		Date releaseDate = results.getDate(7);
		
		Movie movie = new Movie(name, releaseDate, genre, director, duration, description);
		movie.setId(id);

		return movie;
	}

	@Override
	public ArrayList<Movie> getTopFive() {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("SELECT m.* FROM (SELECT movie, AVG(rating) avgra FROM Comments GROUP BY movie) as aux, Movies m "
							+ "WHERE aux.movie = m.id ORDER BY aux.avgra ASC");

			ResultSet results = statement.executeQuery();
			for (int i=0; i<5 && results.next() ; i++)
				movies.add(createMovie(results));

			connection.close();
		} catch (SQLException e) { throw new MovieNotFoundException(); }
		return movies;
	}

	@Override
	public ArrayList<Movie> getFrom(String director) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement = connection
				.prepareStatement("SELECT * FROM Movies WHERE director = ? ");
			statement.setString(1, director);

			ResultSet results = statement.executeQuery();
			while (results.next())
				movies.add(createMovie(results));

			connection.close();
		} catch (SQLException e) { throw new MovieNotFoundException(); }
		return movies;
	}

	@Override
	public ArrayList<Movie> getAll() {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement = connection
				.prepareStatement("SELECT * FROM Movies ORDER BY releasedate DESC");

			ResultSet results = statement.executeQuery();
			while (results.next())
				movies.add(createMovie(results));

			connection.close();
		} catch (SQLException e) { throw new MovieNotFoundException(); }
		return movies;
	}
	
}
