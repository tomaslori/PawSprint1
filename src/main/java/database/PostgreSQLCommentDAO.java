package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exceptions.CommentNotFoundException;
import exceptions.DatabaseException;
import exceptions.MovieNotFoundException;
import model.Comment;
import model.Movie;
import model.User;

public class PostgreSQLCommentDAO implements CommentDAO{

	private final ConnectionManager manager;
	private static CommentDAO instance;
	private UserDAO userDAO;
	private MovieDAO movieDAO;

	public static CommentDAO getInstance() {
		if (instance == null)
			instance = new PostgreSQLCommentDAO();

		return instance;
	}

	private PostgreSQLCommentDAO() {
		manager = new ConnectionManager(DatabasePrefs.driver, DatabasePrefs.connectionString, DatabasePrefs.username, DatabasePrefs.password);
		userDAO = PostgreSQLUserDAO.getInstance();
		movieDAO = PostgreSQLMovieDAO.getInstance();
	}

	
	@Override
	public Comment retrieve(User user, Movie movie) {
		Comment comment = null;
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("SELECT c.id, u.email, m.name, c.rating, c.description FROM Comments c, Users u, Movies m WHERE c.user = u.id AND c.movie = m.id AND c.user = ? AND c.movie = ?");
			statement.setInt(1, user.getId());
			statement.setInt(2, user.getId());

			ResultSet results = statement.executeQuery();
			if (results.next())
				comment = createComment(results);
			else
				throw new CommentNotFoundException();
			connection.close();
		} catch (SQLException e) {
			throw new CommentNotFoundException();
		}
		return comment;
	}
	
	@Override
	public void save(Comment comment) {
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement;
			if (comment.isNew())
				statement = connection
					.prepareStatement("INSERT INTO Comments values(?, ?, ?, ?)");
			else {
				statement = connection
						.prepareStatement("UPDATE Movies SET user = ?, movie = ?, rating = ?, description = ? WHERE user = ? AND movie = ?");
				statement.setInt(5, comment.getOwner().getId());
				statement.setInt(6, comment.getMovie().getId());
			}
			
			statement.setInt(1, comment.getOwner().getId());
			statement.setInt(2, comment.getMovie().getId());
			statement.setInt(3, comment.getRating());
			statement.setString(4, comment.getDescription());
			
			statement.execute();
			connection.commit();
			connection.close();
		} catch (SQLException e) { throw new DatabaseException(); }
		
	}
	
	private Comment createComment(ResultSet results) throws SQLException {
		
		int id = results.getInt(1);
		String userEmail = results.getString(2);
		String movieName = results.getString(3);
		int rating = results.getInt(4);
		String description = results.getString(5);
		
		User owner = userDAO.retrieve(userEmail);
		Movie movie = movieDAO.retrieve(movieName);
		Comment comment = new Comment(owner, movie, rating, description);
		comment.setId(id);

		return comment;
	}

	@Override
	public List<Comment> getComments(Movie movie) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement = connection
				.prepareStatement("SELECT c.id, u.email, m.name, c.rating, c.description FROM Comments c, Movies m, Users u WHERE c.movie = m.id AND c.user = u.id AND m.name = ? ");
			statement.setString(1, movie.getName());

			ResultSet results = statement.executeQuery();
			while (results.next())
				comments.add(createComment(results));

			connection.close();
		} catch (SQLException e) { throw new CommentNotFoundException(); }
		return comments;
	}

	@Override
	public boolean hasCommented(User owner, Movie movie) {
		boolean aux = false;
		try {
			Connection connection = manager.getConnection();
			PreparedStatement statement = connection
				.prepareStatement("SELECT c.* FROM Comments c, Movies m, Users u WHERE c.movie = m.id AND c.user = u.id AND m.name = ? AND u.email = ?");
			statement.setString(1, movie.getName());
			statement.setString(2, owner.getEmail());

			ResultSet results = statement.executeQuery();
			aux = results.next();
			connection.close();
		} catch (SQLException e) { throw new MovieNotFoundException(); }
		return aux;
	}

}
