package servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Comment;
import model.Movie;
import model.User;
import services.CommentService;
import services.MovieService;

public class DetailsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	MovieService movieService = MovieService.getInstance();
	CommentService commentService = CommentService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String movieName = req.getParameter("movie");
		Movie movie = (Movie) req.getAttribute("movie");
		if(movieName == null && movie == null) 
			resp.sendRedirect("home");
		else {
			if(movieName != null)
				movie = movieService.getMovie(movieName);
			
			List<Comment> comments = commentService.getCommentsFor(movie);
			boolean canComment = false;
			boolean alreadyCommented = false;
			
			if((boolean)req.getAttribute("isLogged")) {
				User user = (User) req.getSession().getAttribute("user");
				alreadyCommented = commentService.hasCommented(user, movie);
				canComment = !alreadyCommented && (user.getVip() || movie.getReleaseDate().before(new Date()));
			}
			
			req.getSession().setAttribute("movie", movie);
			req.setAttribute("comments", comments);
			req.setAttribute("canComment", canComment);
			req.setAttribute("alreadyCommented", alreadyCommented);
			req.getRequestDispatcher("/WEB-INF/jsp/details.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		User user = (User) req.getSession().getAttribute("user");
		Movie movie = (Movie) req.getSession().getAttribute("movie");
		req.getSession().setAttribute("movie", null);
		req.setAttribute("movie", movie);
		String description = req.getParameter("description");
		int rating;

		try {
			rating = Integer.parseInt(req.getParameter("rating"));
			
			Comment comment = new Comment(user, movie, rating, description);
			commentService.addComment(comment);

		} catch (RuntimeException e) {
			
			req.setAttribute("error", "One or more of the fields was filled incorrectly.");
			req.getRequestDispatcher("/WEB-INF/jsp/details.jsp").forward(req, resp);
		}
		doGet(req, resp);
	}
}