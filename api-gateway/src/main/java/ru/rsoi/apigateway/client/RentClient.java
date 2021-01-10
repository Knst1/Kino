package ru.rsoi.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.apigateway.model.RentRequest;
import ru.rsoi.apigateway.model.RentResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@FeignClient("rent-service")
public interface RentClient {
    String APPID = "8d03e8dc-5abc-4652-92f0-596a979f46d0";
    String APPSECRET = "80190b1a-1ebe-453f-9d1e-5e43afcf6ea2";

    @PostMapping(path = "/rent")
    ResponseEntity<RentResponse> addRent(@RequestBody @Valid RentRequest rentRequest, @RequestHeader("Authorization") String token);

    @PostMapping(path = "/rent/{rentId}/ok")
    ResponseEntity<RentRequest> confirmRent(@PathVariable Integer rentId, @RequestHeader("Authorization") String token);

    @PostMapping(path = "/rent/{rentId}/duration")
    ResponseEntity<RentRequest> updateDuration(@PathVariable Integer rentId, @RequestBody @Valid Integer duration, @RequestHeader("Authorization") String token);

    @DeleteMapping(path = "/rent")
    ResponseEntity<RentRequest> deleteRent(@RequestBody @Valid Integer rentId, @RequestHeader("Authorization") String token);

    @GetMapping(path = "/rent/{cinemaId}/rentmovies", params = {"page", "size"})
    ResponseEntity<List<RentResponse>> getRentMovies(@PathVariable UUID cinemaId, @RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestHeader("Authorization") String token);

    @GetMapping(path = "/rent/{cinemaId}/prerentmovies", params = {"page", "size"})
    ResponseEntity<List<RentResponse>> getPrerentMovies(@PathVariable UUID cinemaId, @RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestHeader("Authorization") String token);

    @GetMapping(path = "/rent/{cinemaId}/movies", params = {"page", "size"})
    ResponseEntity<List<RentResponse>> getAllMovies(@PathVariable UUID cinemaId, @RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestHeader("Authorization") String token);

    @GetMapping(path = "/rent/{rentId}")
    ResponseEntity<RentRequest> getRent(@PathVariable Integer rentId, @RequestHeader("Authorization") String token);

    @PostMapping("/token")
    String getToken(@RequestParam("username") String username, @RequestParam("password") String password);
}
