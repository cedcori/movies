package com.rest.server.details;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.movies.data.MovieComment;
import com.movies.data.MovieDetail;
import com.movies.data.Transporteur;
import com.movies.services.SpringBootRestApiApp;

@RestController
@RequestMapping("/movies/details")
@SpringBootApplication (scanBasePackages={"com.rest.server.details"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
@PropertySource("classpath:app.properties")
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
public class MoviesDetailsService {
	
	
	@Autowired
	Environment env;
	
	private Map<Long, MovieDetail> mapMovieDetails = new HashMap<>();
	
	public MoviesDetailsService() {
		MovieDetail movieDetail = new MovieDetail(1l,"War","film de guerre");
		mapMovieDetails.put(movieDetail.getId(), movieDetail);
	}
	
	@RequestMapping(value = "/getall", method = RequestMethod.GET, produces = "application/json" )
	public Transporteur getAllComments(){
		List<MovieDetail> movieDetails = new ArrayList<>(mapMovieDetails.values());
		Transporteur transporteur = new Transporteur();
		transporteur.setMovieDetails(movieDetails);
		return transporteur;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json" )
	public MovieDetail getCommentsById(@PathVariable Long id){
		return mapMovieDetails.get(id);
	}
	
	 @Bean
	    public EmbeddedServletContainerCustomizer containerCustomizer() {
	        return (container -> {
	            container.setPort(env.getProperty("movies.details.port", Integer.class));
	        });
	    }
	
	public static void main(String[] args) {
        SpringApplication.run(MoviesDetailsService.class, args);
    }
}
