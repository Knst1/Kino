package ru.rsoi.cinemaservice.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.cinemaservice.model.CinemaInfo;
import ru.rsoi.cinemaservice.service.CinemaService;
import javax.validation.Valid;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/cinema")
public class CinemaController {
    private static final Logger logger = getLogger(CinemaController.class);

    private final CinemaService cinemaService;

    @Autowired
    public CinemaController(CinemaService cinemasService) {
        this.cinemaService = cinemasService;
    }

    @GetMapping(path = "/uuid/{cinemaId}")
    public UUID getCinemaUuid(@PathVariable Integer cinemaId) {
        logger.info("GET запрос (/cinema/uuid/{}). Чтение UUID кинотеатра с ID {}",
                cinemaId.toString(), cinemaId.toString());
        return cinemaService.getCinemaUuid(cinemaId);
    }

    @PostMapping(path = "/{cinemaId}")
    public Integer incCount(@PathVariable Integer cinemaId) {
        logger.info("POST запрос (/cinema/{}). Увеличено количество аренд кинотеатра с ID {}",
                cinemaId.toString(), cinemaId.toString());
        return cinemaService.incCount(cinemaId);
    }

    @GetMapping(path = "/{cinemaId}")
    public CinemaInfo getCinemaInfo(@PathVariable Integer cinemaId) {
        logger.info("GET запрос (/cinema/{}). Чтение конотеатра с ID {}",
                cinemaId.toString(), cinemaId.toString());
        return cinemaService.getCinemaInfo(cinemaId);
    }

    @PostMapping(path = "/dec")
    public Integer decCount(@RequestBody @Valid UUID cinemaUuid) {
        logger.info("POST запрос (/cinema/dec). Увеличено количество аренд кинотеатра с UUID {}",
                cinemaUuid.toString());
        return cinemaService.decCount(cinemaUuid);
    }
}
