package com.rest.server.comments;

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
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.movies.data.MovieComment;
import com.movies.services.SpringBootRestApiApp;

@RestController
@RequestMapping("/movies/comments")
@SpringBootApplication (scanBasePackages={"com.rest.server.comments"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
@PropertySource("classpath:app.properties")
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
public class MoviesCommentService {
	
	Map<Long, List<MovieComment>> mapComments = new HashMap<>();
	
	@Autowired
	Environment env;
	
	public MoviesCommentService() {
		List<MovieComment> movieComments = new ArrayList<>();
		
		MovieComment movieComment1 = new MovieComment(1l,"Johnny coriolan","Interesting documentary.");
		MovieComment movieComment2 = new MovieComment(1l,"Martine","Borring travel");
		movieComments.add(movieComment1);
		movieComments.add(movieComment2);
		mapComments.put(1l, movieComments);
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json" )
	List<MovieComment> getCommentsById(@PathVariable Long id){
		List<MovieComment> movieComments = mapComments.get(id);
		if(movieComments == null){
			movieComments = new ArrayList<>();
		}
		return movieComments;
	}
	
	
	
	 @Bean
	    public EmbeddedServletContainerCustomizer containerCustomizer() {
	        return (container -> {
	            container.setPort(env.getProperty("movies.comments.port", Integer.class));
	        });
	    }
	
	public static void main(String[] args) {
        SpringApplication.run(MoviesCommentService.class, args);
	}
}
