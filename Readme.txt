Hello,
Main boot class is :
 FacadeMovieRestService, MoviesCommentService, MoviesDetailsService

Conf for host and port are in file :
/movies/src/main/resources/app.properties

one 3 stand alone service started you can call services 

facade
http://localhost:8080/movies/getall
http://localhost:8080/movies/1
http://localhost:8080/movies/details/create
http://localhost:8080/movies/comments/create/{id}


comments
http://localhost:8035/movies/comments/1


details
http://localhost:8034/movies/details/getall/
http://localhost:8034/movies/details/1

