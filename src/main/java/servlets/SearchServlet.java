package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Movie;
import services.MovieService;

public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private MovieService movieService = MovieService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/jsp/results.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String search = req.getParameter("search");

		if (search == null) {
			search = " ";
		}
		List<Movie> movies = movieService.getFrom(search);
		req.setAttribute("movies", movies);
		req.setAttribute("search", search);
		req.getRequestDispatcher("/WEB-INF/jsp/results.jsp").forward(req, resp);
	}
}
