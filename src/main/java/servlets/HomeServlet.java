package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Movie;
import services.MovieService;

public class HomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	MovieService movieService = MovieService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Movie> topmovies = movieService.getTopFive();
		List<Movie> movies = movieService.getAll();
		req.setAttribute("topmovies", topmovies);
		req.setAttribute("movies", movies);
		req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		// nothing..
	}
}
