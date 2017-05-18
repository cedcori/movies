package com.movies.data;

public class MovieComment {
	
	private long movieId;
	
	private String userName;
	
	private String message;
	
	public MovieComment(){
	}

	public MovieComment(long movieId, String userName, String message) {
		super();
		this.movieId = movieId;
		this.userName = userName;
		this.message = message;
	}

	public long getMovieId() {
		return movieId;
	}

	public void setMovieId(long movieId) {
		this.movieId = movieId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
