package com.movies.data;

import java.util.List;

public class MovieInformation {
	private Long id;
	private MovieDetail movieDetail;
	private List<MovieComment> movieComments;
	
	public MovieInformation(){
	}
	
	public MovieInformation(Long id, MovieDetail movieDetail, List<MovieComment> movieComments) {
		super();
		this.id = id;
		this.movieDetail = movieDetail;
		this.movieComments = movieComments;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MovieDetail getMovieDetail() {
		return movieDetail;
	}
	public void setMovieDetail(MovieDetail movieDetail) {
		this.movieDetail = movieDetail;
	}
	public List<MovieComment> getMovieComments() {
		return movieComments;
	}
	public void setMovieComments(List<MovieComment> movieComments) {
		this.movieComments = movieComments;
	}
	
	

}
