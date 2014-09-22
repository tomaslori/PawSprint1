package services;

import java.util.List;

import model.Comment;
import model.Movie;
import model.User;
import database.CommentDAO;
import database.PostgreSQLCommentDAO;
import exceptions.CantCommentAgainException;


public class CommentService {

	private CommentDAO commentDAO;

	private static CommentService instance;

	private CommentService() {
		commentDAO = PostgreSQLCommentDAO.getInstance();
	}
	
	public static CommentService getInstance() {
		if (instance == null)
			instance = new CommentService();

		return instance;
	}

	public List<Comment> getCommentsFor(Movie movie) {
		return commentDAO.getComments(movie);
	}

	public void addComment(Comment comment) {
		boolean alreadyCommented = commentDAO.hasCommented(comment.getOwner(), comment.getMovie());
		if(!alreadyCommented)
			commentDAO.save(comment);
		else
			throw new CantCommentAgainException();
	}

	public boolean hasCommented(User user, Movie movie) {
		return commentDAO.hasCommented(user, movie);
	}
}
