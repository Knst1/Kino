package ru.rsoi.apigateway.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.apigateway.exception.CinemaException;
import ru.rsoi.apigateway.exception.CinemaServiceAccessException;
import ru.rsoi.apigateway.model.Cinema;
import ru.rsoi.apigateway.model.RentInfoResponse;
import ru.rsoi.apigateway.services.cinema.CinemaService;

import javax.validation.Valid;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/cinema")
class CinemaController {
    private static final Logger logger = getLogger(CinemaController.class);

    private final CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping(path = "/{cinemaId}")
    public Cinema getCinema(@PathVariable Integer cinemaId) {
        logger.info("GET запрос (/cinema/{}). Чтение кинотеатра с ID {}",
                cinemaId.toString(), cinemaId.toString());
        Cinema cinema = cinemaService.getCinema(cinemaId);
        if (cinema.getName() == null)
            throw new CinemaException();
        if (cinema.getChief() == null)
            throw new CinemaServiceAccessException();
        return cinema;
    }

    @GetMapping(path = "/{cinemaId}/movies", params = {"page", "size"})
    public List<RentInfoResponse> getAllMovies(@PathVariable Integer cinemaId, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        logger.info("GET запрос (/cinema/{}/movies, params = [page, size]). Чтение аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}.",
                cinemaId.toString(), cinemaId.toString(), page.toString(), size.toString());
        List<RentInfoResponse> ll = cinemaService.getAllMovies(cinemaId, page, size);
        return ll;
    }

    @GetMapping(path = "/{cinemaId}/prerentmovies", params = {"page", "size"})
    public List<RentInfoResponse> getPrerentMovies(@PathVariable Integer cinemaId, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        logger.info("GET запрос (/cinema/{}/prerentmovies, params = [page, size]). Чтение неподтверждённых аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}.",
                cinemaId.toString(), cinemaId.toString(), page.toString(), size.toString());
        return cinemaService.getPrerentMovies(cinemaId, page, size);
    }

    @GetMapping(path = "/{cinemaId}/rentmovies", params = {"page", "size"})
    public List<RentInfoResponse> getRentMovies(@PathVariable Integer cinemaId, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        logger.info("GET запрос (/cinema/{}/rentmovies, params = [page, size]). Чтение подтверждённых аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}.",
                cinemaId.toString(), cinemaId.toString(), page.toString(), size.toString());
        return cinemaService.getRentMovies(cinemaId, page, size);
    }
}
