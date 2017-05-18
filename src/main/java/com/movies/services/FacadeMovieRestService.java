package com.movies.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.movies.data.MovieComment;
import com.movies.data.MovieDetail;
import com.movies.data.MovieInformation;

@RestController
@RequestMapping("/movies")
public class FacadeMovieRestService {

	
	private LRUCache<Long, MovieDetail> mapMovies = new LRUCache<>(10);
	private Map<Long, List<MovieComment>> mapMoviesComments = new HashMap<>();
	
	private AtomicLong idGenerator = new AtomicLong(100);
	
	@Autowired
	MoviesAsyncCallServices moviesAsyncCallServices;
	
    @RequestMapping(value = "/getall", method = RequestMethod.GET, produces = "application/json")
    public List<MovieInformation> getAllMovies() throws InterruptedException, ExecutionException {
    	long start = System.currentTimeMillis();
		
    	Future<List<MovieDetail>> futureDetail = moviesAsyncCallServices.getAllDetails();
    	List<MovieDetail> movieDetails = futureDetail.get();
    	List<MovieInformation>	movieInformations = new ArrayList<>();
    	for(MovieDetail movieDetail : movieDetails){
    		Future<List<MovieComment>> futureComment = moviesAsyncCallServices.getCommentsById(movieDetail.getId());
    		List<MovieComment> movieComments = futureComment.get();
    		MovieInformation movieInformation = new MovieInformation(movieDetail.getId(),movieDetail,movieComments);
    		movieInformations.add(movieInformation);
    	}
    	long diff = System.currentTimeMillis() - start;
    	System.out.println(diff);
    	return movieInformations;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public MovieInformation getMovieById(@PathVariable Long id) throws InterruptedException, ExecutionException, MovieException {
    	long start = System.currentTimeMillis();
    	List<MovieComment> movieComments = null;
    	Future<List<MovieComment>> futureComment = null;
    	try {
    		 futureComment = moviesAsyncCallServices.getCommentsById(id);
		} catch (Exception e) {
			movieComments = mapMoviesComments.get(id);
		}
    	
    	MovieDetail movieDetail = mapMovies.get(id);
    	if(movieDetail == null){
    		Future<MovieDetail> futureDetail = moviesAsyncCallServices.getDetailById(id);
        	movieDetail = futureDetail.get();
    	}
    	if(movieDetail == null){
    		throw new MovieException("Film not found for id "+id);
    	}
    	if(movieComments == null){
    		try {
        		movieComments = futureComment.get();
    	   		if(movieComments != null){
    	   			mapMoviesComments.put(id, movieComments);
    	   		}
    		} catch (Exception e) {
    			movieComments = mapMoviesComments.get(id);
    		}
    	}
    	
		MovieInformation movieInformation = new MovieInformation(movieDetail.getId(),movieDetail,movieComments);
    	long diff = System.currentTimeMillis() - start;
    	System.out.println(diff);
    	return movieInformation;
    }
    
    @Secured("R_ADMIN")
    @RequestMapping(value = "/details/create", method = RequestMethod.POST, consumes="application/json")
    public Long createDetails(@RequestBody MovieDetail movieDetail) throws InterruptedException, ExecutionException {
    	Long id = movieDetail.getId();
    	if(id == null){
    		movieDetail.setId(idGenerator.incrementAndGet());
    	}
    	mapMovies.put(movieDetail.getId(), movieDetail);
    	return movieDetail.getId();
    }
    
    @Secured("R_USER")
    @RequestMapping(value = "/comments/create/{id}", method = RequestMethod.POST, consumes="application/json")
    public void createComments(@PathVariable Long id, @RequestBody List<MovieComment> movieComments) throws InterruptedException, ExecutionException,MovieException {
    	if(mapMovies.get(id)== null){
    		throw new MovieException("Details film not found for film id :"+id);
    	}
    	mapMoviesComments.put(id, movieComments);
    }
    
    @ExceptionHandler(MovieException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleTodoNotFoundException(MovieException ex) {
      
    }
    
}