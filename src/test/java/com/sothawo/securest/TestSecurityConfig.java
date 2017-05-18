package com.sothawo.securest;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import com.movies.services.FacadeMovieRestService;
import com.movies.services.MoviesAsyncCallServices;
@Configuration
@PropertySource("classpath:app.properties")
public class TestSecurityConfig{
   @Bean
   RestTemplate getRestTemplate(){
	   RestTemplate restTemplate = mock(RestTemplate.class);
	   return restTemplate;
   }
   
   
   @Bean
   FacadeMovieRestService getFacedeMovieRestService(){
	   FacadeMovieRestService facadeMovieRestService = new FacadeMovieRestService();
	   return facadeMovieRestService;
   }
   
   @Bean
   MoviesAsyncCallServices getMoviesAsyncCallServices(){
	   return mock(MoviesAsyncCallServices.class);
   }
}