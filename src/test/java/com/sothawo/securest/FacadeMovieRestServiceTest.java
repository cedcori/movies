package com.sothawo.securest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.data.MovieComment;
import com.movies.data.MovieDetail;
import com.movies.data.MovieInformation;
import com.movies.services.FacadeMovieRestService;
import com.movies.services.MoviesAsyncCallServices;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestSecurityConfig.class)
public class FacadeMovieRestServiceTest {

	@Autowired
	private FacadeMovieRestService facedeMovieRestService;
	
	@Autowired
	private MoviesAsyncCallServices moviesAsyncCallServices;

	private MockMvc mockMvc;
	
	@Before
	public void setUp(){
		mockMvc = standaloneSetup(facedeMovieRestService).build();
	}
	

	// @Test
	public void testGetAllMovies() {
		System.out.println("Testing listAllUsers API-----------");

		RestTemplate restTemplateTest = new RestTemplate();
		List<MovieDetail> movieDetails = restTemplateTest.getForObject("http://localhost:8080/movies/getall",
				List.class);
		assertThat(movieDetails).isNotEmpty();
	}

	//@Test
	public void validate_get_address() throws Exception {
		mockMvc.perform(get("/movies/getall")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	public void whenCallAllMoviesThenGellAllMovies() throws Exception {
		List<MovieDetail> movieDetails = new ArrayList<>();
		MovieDetail movieDetail = createmovieDetail1();
		movieDetail.setId(1l);
		movieDetail.setDescription("film de guerre");
		movieDetail.setTitle("War");
		movieDetails.add(movieDetail);
		Future<List<MovieDetail>> futureDetails = new AsyncResult<List<MovieDetail>>(movieDetails);
		when(moviesAsyncCallServices.getAllDetails()).thenReturn(futureDetails);
		
		List<MovieComment> movieComments =createMovieComments1();
		Future<List<MovieComment>> futureComments = new AsyncResult<List<MovieComment>>(movieComments);
		when(moviesAsyncCallServices.getCommentsById(movieDetail.getId())).thenReturn(futureComments);
		
		MovieInformation movieInformation = new MovieInformation(movieDetail.getId(),movieDetail,movieComments);
		
		List<MovieInformation> movieInformations = new ArrayList<>();
		movieInformations.add(movieInformation);
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedJson = mapper.writeValueAsString(movieInformations);
		String content = mockMvc.perform(get("/movies/getall")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(1))).andReturn().getResponse().getContentAsString();
		System.out.println(content);
		assertEquals(expectedJson, content);
	}
	
	@Test
	public void whenCallMovieIdOneThenGetMovieOne() throws Exception {
		MovieDetail movieDetail = createmovieDetail1();
		Future<MovieDetail> futureDetail = new AsyncResult<MovieDetail>(movieDetail);
		when(moviesAsyncCallServices.getDetailById(movieDetail.getId())).thenReturn(futureDetail);
		
		List<MovieComment> movieComments = createMovieComments1();
		Future<List<MovieComment>> futureComments = new AsyncResult<List<MovieComment>>(movieComments);
		when(moviesAsyncCallServices.getCommentsById(movieDetail.getId())).thenReturn(futureComments);
		
		MovieInformation movieInformation = new MovieInformation(movieDetail.getId(),movieDetail, movieComments);
		
		ObjectMapper mapper = new ObjectMapper();
		String expectedJson = mapper.writeValueAsString(movieInformation);
		String content = mockMvc.perform(get("/movies/"+movieDetail.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andReturn().getResponse().getContentAsString();
		System.out.println(content);
		assertEquals(expectedJson, content);
	}
	
	@Test
	public void whenCreateAnNewMovieThenGetThisNewMovie() throws Exception {
		MovieDetail movieDetail = createmovieDetail1();
		Future<MovieDetail> futureDetail = new AsyncResult<MovieDetail>(movieDetail);
		when(moviesAsyncCallServices.getDetailById(movieDetail.getId())).thenReturn(futureDetail);
		
		List<MovieComment> movieComments = createMovieComments1();
		Future<List<MovieComment>> futureComments = new AsyncResult<List<MovieComment>>(movieComments);
		when(moviesAsyncCallServices.getCommentsById(movieDetail.getId())).thenReturn(futureComments);
		
		MovieInformation movieInformation = new MovieInformation(movieDetail.getId(),movieDetail, movieComments);
		
		List<MovieInformation> movieInformations = new ArrayList<>();
		movieInformations.add(movieInformation);
		
		MovieDetail movieDetail2 = createmovieDetail2();
		// create on services
		ObjectMapper mapper = new ObjectMapper();
		String detail2ToJson = mapper.writeValueAsString(movieDetail2); 
		String id2Str =  mockMvc.perform(post("/movies/details/create").contentType(MediaType.APPLICATION_JSON_VALUE).content(detail2ToJson)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Long id2 = Long.valueOf(id2Str);
		movieDetail2.setId(id2);
		MovieInformation movieInformation2 = new MovieInformation(movieDetail2.getId(), movieDetail2, null);
		
		// retreive from services
		String expectedJson = mapper.writeValueAsString(movieInformation2);
		String content = mockMvc.perform(get("/movies/"+movieDetail2.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				).andReturn().getResponse().getContentAsString();
		System.out.println(content);
		assertEquals(expectedJson, content);
	}
	
	private MovieDetail createmovieDetail1(){
		return new MovieDetail(1l,"War","film de guerre");
	}
	
	@Test
	public void whenCreateAnNewMovieDetailsAndCommentThenGetThisNewMovieDetailsWithComments() throws Exception {
		MovieDetail movieDetail = createmovieDetail1();
		Future<MovieDetail> futureDetail = new AsyncResult<MovieDetail>(movieDetail);
		when(moviesAsyncCallServices.getDetailById(movieDetail.getId())).thenReturn(futureDetail);
		
		List<MovieComment> movieComments = createMovieComments1();
		Future<List<MovieComment>> futureComments = new AsyncResult<List<MovieComment>>(movieComments);
		when(moviesAsyncCallServices.getCommentsById(movieDetail.getId())).thenReturn(futureComments);
		
		MovieInformation movieInformation = new MovieInformation(movieDetail.getId(),movieDetail, movieComments);
		
		List<MovieInformation> movieInformations = new ArrayList<>();
		movieInformations.add(movieInformation);
		
		MovieDetail movieDetail2 = createmovieDetail2();
		
		// save new details on services
		ObjectMapper mapper = new ObjectMapper();
		String detail2ToJson = mapper.writeValueAsString(movieDetail2); 
		String id2Str =  mockMvc.perform(post("/movies/details/create").contentType(MediaType.APPLICATION_JSON_VALUE).content(detail2ToJson)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Long id2 = Long.valueOf(id2Str);
		movieDetail2.setId(id2);
		
		// save new comments on services
		List<MovieComment> movieComments2 = createMovieComments2(id2);
		String comments2ToJson = mapper.writeValueAsString(movieComments2); 
		mockMvc.perform(post("/movies/comments/create/"+id2).contentType(MediaType.APPLICATION_JSON_VALUE).content(comments2ToJson)).andExpect(status().isOk());
		
		MovieInformation movieInformation2 = new MovieInformation(movieDetail2.getId(), movieDetail2, movieComments2);
		
		// retreive from services
		String expectedJson = mapper.writeValueAsString(movieInformation2);
		String content = mockMvc.perform(get("/movies/"+movieDetail2.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				).andReturn().getResponse().getContentAsString();
		System.out.println(content);
		assertEquals(expectedJson, content);
	}
	
	private List<MovieComment> createMovieComments1(){
		List<MovieComment> movieComments = new ArrayList<>();
		MovieComment movieComment1 = new MovieComment(1,"Cedrick coriolan","It's a very good movie.");
		MovieComment movieComment2 = new MovieComment(1,"Tenzin","Can do better.");
		
		movieComments.add(movieComment1);
		movieComments.add(movieComment2);
		return movieComments;
	}
	
	@Test
	public void whenCreateCommentWenTheGetMovieInformationThenGetError404() throws Exception {
		MovieDetail movieDetail = createmovieDetail1();
		Future<MovieDetail> futureDetail = new AsyncResult<MovieDetail>(movieDetail);
		when(moviesAsyncCallServices.getDetailById(movieDetail.getId())).thenReturn(futureDetail);
		
		List<MovieComment> movieComments = createMovieComments1();
		Future<List<MovieComment>> futureComments = new AsyncResult<List<MovieComment>>(movieComments);
		when(moviesAsyncCallServices.getCommentsById(movieDetail.getId())).thenReturn(futureComments);
		
		MovieInformation movieInformation = new MovieInformation(movieDetail.getId(),movieDetail, movieComments);
		
		List<MovieInformation> movieInformations = new ArrayList<>();
		movieInformations.add(movieInformation);
		
		MovieDetail movieDetail2 = createmovieDetail2();
		
		// save new details on services
		ObjectMapper mapper = new ObjectMapper();
		
		// save new comments on services
		Long id2 = 2345l;
		List<MovieComment> movieComments2 = createMovieComments2(id2);
		String comments2ToJson = mapper.writeValueAsString(movieComments2); 
		mockMvc.perform(post("/movies/comments/create/"+id2).contentType(MediaType.APPLICATION_JSON_VALUE).content(comments2ToJson)).andExpect(status().isNotFound());
		
		MovieInformation movieInformation2 = new MovieInformation(movieDetail2.getId(), movieDetail2, movieComments2);
		
		// retreive from services
		String expectedJson = mapper.writeValueAsString(movieInformation2);
		mockMvc.perform(get("/movies/"+movieDetail2.getId())).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
	}
	
	
	
	private MovieDetail createmovieDetail2(){
		return new MovieDetail(null,"Travel","Documentary film");
	}
	
	private List<MovieComment> createMovieComments2(Long id){
		List<MovieComment> movieComments = new ArrayList<>();
		MovieComment movieComment1 = new MovieComment(id,"Johnny coriolan","Interesting documentary.");
		MovieComment movieComment2 = new MovieComment(id,"Martine","Borring travel");
		movieComments.add(movieComment1);
		movieComments.add(movieComment2);
		return movieComments;
	}
	
}
