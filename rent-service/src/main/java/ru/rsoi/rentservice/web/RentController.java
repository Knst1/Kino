package ru.rsoi.rentservice.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.rentservice.model.RentInfo;
import ru.rsoi.rentservice.model.RentResponse;
import ru.rsoi.rentservice.service.RentService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/rent")
public class RentController {
    private static final Logger logger = getLogger(RentController.class);
    private final RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping
    public RentResponse addRent(@RequestBody @Valid RentInfo request) {
        logger.info("POST запрос (/rent) добаления новой аренды '{}'", request.toString());
        return rentService.addRent(request);
    }

    @PostMapping(path = "/{rentId}/ok")
    public RentInfo updateRent(@PathVariable Integer rentId) {
        logger.info("POST запрос (/rent/{}/ok). Подтверждение аренды с ID {}",
                rentId.toString(), rentId.toString());
        return rentService.updateRent(rentId);
    }

    @PostMapping(path = "/{rentId}/duration")
    public RentInfo updateDuration(@PathVariable Integer rentId, @RequestBody @Valid Integer duration) {
        logger.info("POST запрос (/rent/{}/duration). Изменение срока аренды с ID {} на значение {}",
                rentId.toString(), rentId.toString(), duration.toString());
        return rentService.updateDuration(rentId, duration);
    }

    @DeleteMapping
    public RentInfo deleteRent(@RequestBody @Valid Integer rentId) {
        logger.info("DELETE запрос (/rent). Удаление аренды ID {}", rentId.toString());
        return rentService.deleteRent(rentId);
    }

    @GetMapping(path = "/{cinemaUid}/rentmovies", params = {"page", "size"})
    public List<RentInfo> getRentMovies(@PathVariable UUID cinemaUid, @RequestParam("page") Integer page, @RequestParam("size") Integer size){
        logger.info("GET запрос (/rent/{}/rentmovies, params = [page, size]). Чтение всех подтверждённых аренд кинотеатра с UUID {}. " +
                        "Пагинация: страница {}, элементов {}",
                cinemaUid.toString(), cinemaUid.toString(), page.toString(), size.toString());
        return rentService.getRentMovies(cinemaUid, page, size);
    }

    @GetMapping(path = "/{cinemaUid}/prerentmovies", params = {"page", "size"})
    public List<RentInfo> getPrerentMovies(@PathVariable UUID cinemaUid, @RequestParam("page") Integer page, @RequestParam("size") Integer size){
        logger.info("GET запрос (/rent/{}/prerentmovies, params = [page, size]). Чтение всех неподтверждённых аренд кинотеатра с UUID {}. " +
                        "Пагинация: страница {}, элементов {}",
                cinemaUid.toString(), cinemaUid.toString(), page.toString(), size.toString());
        return rentService.getPrerentMovies(cinemaUid, page, size);
    }

    @GetMapping(path = "/{cinemaUid}/movies", params = {"page", "size"})
    public List<RentInfo> getAllMovies(@PathVariable UUID cinemaUid, @RequestParam("page") Integer page, @RequestParam("size") Integer size){
        logger.info("GET запрос (/rent/{}/movies, params = [page, size]). Чтение всех аренд кинотеатра с UUID {}. " +
                        "Пагинация: страница {}, элементов {}",
                cinemaUid.toString(), cinemaUid.toString(), page.toString(), size.toString());
        return rentService.getAllMovies(cinemaUid, page, size);
    }

    @GetMapping("/{rentId}")
    public RentInfo getRent(@PathVariable Integer rentId) {
        logger.info("GET запрос (/rent/{}). Чтение аренды с ID {}",
                rentId.toString(), rentId.toString());
        return rentService.getRent(rentId);
    }
}
