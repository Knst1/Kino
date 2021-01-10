package ru.rsoi.apigateway.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.apigateway.model.AddRentInfoBody;
import ru.rsoi.apigateway.model.RentInfoResponse;
import ru.rsoi.apigateway.model.RentRequest;
import ru.rsoi.apigateway.services.rent.RentService;

import javax.validation.Valid;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/rent")
class RentController {
    private static final Logger logger = getLogger(RentController.class);

    private final RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PatchMapping(path = "/{rentId}/ok")
    public RentRequest confirmRent(@PathVariable Integer rentId) {
        logger.info("PATCH запрос (/rent/{}/ok). Подтверждение аренды с ID {}", rentId.toString(), rentId.toString());
        return rentService.confirmRent(rentId);
    }

    @PatchMapping(path = "/{rentId}")
    public RentRequest updateDuration(@PathVariable Integer rentId, @RequestBody @Valid Integer duration) {
        logger.info("PATCH запрос (/rent/{}). Изменение срока неподтверждённой аренды с ID {} на значение {}",
                rentId.toString(), rentId.toString(), duration.toString());
        return rentService.updateDuration(rentId, duration);
    }

    @DeleteMapping(path = "/{rentId}")
    public RentRequest deleteRent(@PathVariable Integer rentId) {
        logger.info("DELETE запрос (/rent/{}). Удаление неподтверждённой аренды с ID {}",
                rentId.toString(), rentId.toString());
        return rentService.deleteRent(rentId);
    }

    @PostMapping()
    public RentInfoResponse addRent(@RequestBody @Valid AddRentInfoBody body) {
        logger.info("POST запрос (/rent). Добавление аренды кинотеатром с ID {} фильма с ID {} на {} дней.",
                body.getCinemaId().toString(), body.getMovieId().toString(), body.getDuration().toString());
        return rentService.addRent(body.getCinemaId(), body.getMovieId(), body.getDuration());
    }

    @GetMapping("/{rentId}/{cinemaId}")
    public Boolean rentOwner(@PathVariable Integer cinemaId, @PathVariable Integer rentId) {
        return rentService.rentOwner(cinemaId, rentId);
    }
}
