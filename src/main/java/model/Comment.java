package model;

public class Comment extends AbstractEntity {
	
	private User owner;
	private Movie movie;
	private int rating;
	private String description;

	
	public Comment(User owner, Movie movie, int rating, String description) throws IllegalArgumentException {

		setId(-1);
		setOwner(owner);
		setMovie(movie);
		setRating(rating);
		setDescription(description);
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) throws IllegalArgumentException {
		if(owner == null)
			throw new IllegalArgumentException();
		else
			this.owner = owner;
	}
	
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) throws IllegalArgumentException {
		if(movie == null)
			throw new IllegalArgumentException();
		else
			this.movie = movie;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) throws IllegalArgumentException {
		if (rating < 0 || rating > 5)
			throw new IllegalArgumentException();
		else
			this.rating = rating;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws IllegalArgumentException {
		if(description == null)
			throw new IllegalArgumentException();
		else
			this.description = description;
	}
}
