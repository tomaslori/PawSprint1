package model;

import java.util.Date;

public class Movie extends AbstractEntity{

	private Date releaseDate;
	private String name;
	private String genre;
	private String director;
	private int duration;
	private String description;

	
	public Movie(String name, Date releaseDate, String genre, String director,
			int duration, String description) throws IllegalArgumentException {

		setId(-1);
		setName(name);
		setGenre(genre);
		setDirector(director);
		setDuration(duration);
		setDescription(description);
		setReleaseDate(releaseDate);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws IllegalArgumentException {
		if(name == null)
			throw new IllegalArgumentException();
		else
			this.name = name;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) throws IllegalArgumentException {
		if(genre == null)
			throw new IllegalArgumentException();
		else
			this.genre = genre;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) throws IllegalArgumentException {
		if (director == null)
			throw new IllegalArgumentException();
		else
			this.director = director;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) throws IllegalArgumentException {
		if (duration <= 0)
			throw new IllegalArgumentException();
		else
			this.duration = duration;
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


	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) throws IllegalArgumentException {
		if(releaseDate == null)
			throw new IllegalArgumentException();
		else
			this.releaseDate = releaseDate;
	}
}
