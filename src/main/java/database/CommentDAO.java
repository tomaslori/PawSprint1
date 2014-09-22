package database;

import java.util.List;

import model.Comment;
import model.Movie;
import model.User;

public interface CommentDAO {
	
	public Comment retrieve(User user, Movie movie);
	public void save(Comment comment);
	public List<Comment> getComments(Movie movie);
	public boolean hasCommented(User owner, Movie movie);

}
