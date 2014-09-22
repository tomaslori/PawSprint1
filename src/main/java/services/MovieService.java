package services;

import java.util.ArrayList;

import model.Movie;
import database.MovieDAO;
import database.PostgreSQLMovieDAO;


public class MovieService {

	private MovieDAO movieDAO;

	private static MovieService instance;

	private MovieService() {
		movieDAO = PostgreSQLMovieDAO.getInstance();
	}
	
	public static MovieService getInstance() {
		if (instance == null)
			instance = new MovieService();

		return instance;
	}
	
	public Movie getMovie(String name) {
		return movieDAO.retrieve(name);
	}
	
	public ArrayList<Movie> getFrom(String director) {
		return movieDAO.getFrom(director);
	}
	
	public ArrayList<Movie> getTopFive() {
		return movieDAO.getTopFive();
	}
	
	public ArrayList<Movie> getAll() {
		return movieDAO.getAll();
	}

}
