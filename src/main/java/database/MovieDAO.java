package database;

import java.util.ArrayList;

import model.Movie;

public interface MovieDAO {
	
	public Movie retrieve(String name);
	public void save(Movie movie);
	public ArrayList<Movie> getTopFive();
	public ArrayList<Movie> getFrom(String director);
	public ArrayList<Movie> getAll();

}
