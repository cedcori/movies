package com.movies.services;

import java.util.List;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.movies.data.MovieComment;
import com.movies.data.MovieDetail;
import com.movies.data.Transporteur;

@Component
public class MoviesAsyncCallServices {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Environment env;

	private String moviesDetailsAddress;

	private String moviesCommentsAddress;
	
	
	
	@Async
	public Future<List<MovieDetail>> getAllDetails() {
		List<MovieDetail> movieDetails = ((Transporteur) restTemplate
				.getForObject(moviesDetailsAddress + "/movies/details/getall", Transporteur.class)).getMovieDetails();
		sleep(2000);
		return new AsyncResult<List<MovieDetail>>(movieDetails);
	}
	@Async
	public Future<MovieDetail> getDetailById(Long id) {
		MovieDetail movieDetail = (MovieDetail) restTemplate
				.getForObject(moviesDetailsAddress + "/movies/details/"+id, MovieDetail.class);
		sleep(2000);
		return new AsyncResult<MovieDetail>(movieDetail);
	}

	@Async
	public Future<List<MovieComment>> getCommentsById(Long id) {
		List<MovieComment> movieDetails = (List<MovieComment>) restTemplate
				.getForObject(moviesCommentsAddress + "/movies/comments/"+id, List.class);
		sleep(2000);
		return new AsyncResult<List<MovieComment>>(movieDetails);
	}

	@PostConstruct
	public void init() {
		moviesDetailsAddress = "http://" + env.getProperty("movies.details.host") + ":"
				+ env.getProperty("movies.details.port");
		moviesCommentsAddress =  "http://" + env.getProperty("movies.comments.host") + ":" + env.getProperty("movies.comments.port");
	}

	public String getMoviesCommentsAddress() {
		return moviesCommentsAddress;
	}

	public String getMoviesDetailsAddress() {
		return moviesDetailsAddress;
	}
	
	private void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
