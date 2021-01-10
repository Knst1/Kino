package ru.rsoi.apigateway.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.apigateway.exception.MovieException;
import ru.rsoi.apigateway.exception.MovieServiceAccessException;
import ru.rsoi.apigateway.model.Movie;
import ru.rsoi.apigateway.model.ShortMovie;
import ru.rsoi.apigateway.services.movie.MovieService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/movie")
class MovieController {
    private static final Logger logger = getLogger(MovieController.class);

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(path = "/{movieId}")
    public Movie getMovie(@PathVariable Integer movieId) {
        logger.info("GET запрос (/movie/{}). Чтение фильма с ID {}", movieId.toString(), movieId.toString());
        Movie movie = movieService.getMovie(movieId);
        if (movie.getCost() != null && movie.getCost() == -1)
            throw new MovieServiceAccessException();
        if (movie.getName() == null)
            throw new MovieException();
        return movie;
    }

    @GetMapping(params = {"page", "size"})
    public List<ShortMovie> allMovies(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        logger.info("GET запрос (/movie, params = [page, size]). Чтение фильмов. Пагинация: страница {}, элементов {}.",
                page.toString(), size.toString());
        List<ShortMovie> movies = movieService.allMovies(page, size);
        if (movies == null)
            throw new MovieServiceAccessException();
        return movies;
    }
}
