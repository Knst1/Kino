package ru.rsoi.frontend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.frontend.model.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient("api-gateway")
public interface RestApiClient {

    @GetMapping(path = "/cinema/{cinemaId}")
    ResponseEntity<Cinema> getCinema(@PathVariable Integer cinemaId);

    @GetMapping(path = "/cinema/{cinemaId}/movies", params = {"page", "size"})
    ResponseEntity<List<RentInfoResponse>> getAllMovies(@PathVariable Integer cinemaId, @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size);

    @GetMapping(path = "/cinema/{cinemaId}/prerentmovies", params = {"page", "size"})
    ResponseEntity<List<RentInfoResponse>> getPrerentMovies(@PathVariable Integer cinemaId, @RequestParam("page") Integer page,
                                                   @RequestParam("size") Integer size);

    @GetMapping(path = "/cinema/{cinemaId}/rentmovies", params = {"page", "size"})
    ResponseEntity<List<RentInfoResponse>> getRentMovies(@PathVariable Integer cinemaId, @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size);

    @GetMapping("/movie/{movieId}")
    ResponseEntity<Movie> getMovie(@PathVariable Integer movieId);

    @GetMapping(path = "/movie", params = {"page", "size"})
    ResponseEntity<List<ShortMovie>> allMovies(@RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @PatchMapping("/rent/{rentId}/ok")
    ResponseEntity<RentRequest> confirmRent(@PathVariable Integer rentId);

    @PatchMapping("/rent/{rentId}")
    ResponseEntity<RentRequest> updateDuration(@PathVariable Integer rentId, @RequestBody @Valid Integer duration);

    @DeleteMapping("/rent/{rentId}")
    ResponseEntity<RentRequest> deleteRent(@PathVariable Integer rentId);

    @PostMapping("/rent")
    ResponseEntity<RentInfoResponse> addRent(@RequestBody @Valid AddRentInfoBody body);

    @GetMapping("/rent/{rentId}/{cinemaId}")
    ResponseEntity<Boolean> rentOwner(@PathVariable Integer cinemaId, @PathVariable Integer rentId);
}
