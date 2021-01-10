package ru.rsoi.movieservice.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.movieservice.model.IncMovieCountResponse;
import ru.rsoi.movieservice.model.MovieInfo;
import ru.rsoi.movieservice.service.MovieService;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private static final Logger logger = getLogger(MovieController.class);

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService moviesService) {
        this.movieService = moviesService;
    }

    @GetMapping(path = "/{movieId}")
    public MovieInfo getMovieInfo(@PathVariable Integer movieId) {
        logger.info("GET запрос (/movie/{}). Чтение фильма с ID {}",
                movieId.toString(), movieId.toString());
        return movieService.getMovieInfo(movieId);
    }

    @GetMapping(params = {"page", "size"})
    public List<MovieInfo> getMovies(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        logger.info("GET запрос (/movie, params = [page, size]). Чтение списка фильмов. " +
                        "Пагинация: страница {}, элементов {}", page.toString(), size.toString());
        return movieService.getMovies(page, size);
    }

    @GetMapping(path = "/uuid/{movieId}")
    public UUID getMovieUuid(@PathVariable Integer movieId) {
        logger.info("GET запрос (/movie/uuid/{}). Чтение UUID фильма с ID {}",
                movieId.toString(), movieId.toString());
        return movieService.getMovieUuid(movieId);
    }

    @GetMapping("/fromuid/{movieUid}")
    public MovieInfo getMovieFromUid(@PathVariable UUID movieUid) {
        logger.info("GET запрос (/movie/fromuid/{}). Чтение фильма с UUID {}",
                movieUid.toString(), movieUid.toString());
        return movieService.getMovieFromUid(movieUid);
    }

    @PostMapping(path = "/{movieUuid}")
    public Integer incCount(@PathVariable UUID movieUuid, @RequestBody @Valid IncMovieCountResponse response) {
        logger.info("POST запрос (/movie/cost/{}). Увеличение дней аренды фильма с UUID {} на {}",
                movieUuid.toString(), movieUuid.toString(), response.getDays().toString());
        return movieService.incCount(movieUuid, response);
    }
}
