package ru.rsoi.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.apigateway.model.Cinema;

import javax.validation.Valid;
import java.util.UUID;

@FeignClient("cinema-service")
public interface CinemaClient {
    String APPID = "49801d29-a24c-4b8e-88ca-2737575f9408";
    String APPSECRET = "f2d548f9-46bb-42a2-926b-aecc87c21518";

    @GetMapping("/cinema/{cinemaId}")
    ResponseEntity<Cinema> getCinema(@PathVariable Integer cinemaId, @RequestHeader("Authorization") String token);

    @GetMapping("/cinema/uuid/{cinemaId}")
    ResponseEntity<UUID> getUuid(@PathVariable Integer cinemaId, @RequestHeader("Authorization") String token);

    @PostMapping("/cinema/{cinemaId}")
    ResponseEntity<Integer> incCount(@PathVariable("cinemaId") Integer cinemaId, @RequestHeader("Authorization") String token);

    @PostMapping("/cinema/dec")
    ResponseEntity<Integer> decCount(@RequestBody @Valid UUID cinemaUid, @RequestHeader("Authorization") String token);

    @PostMapping("/token")
    String getToken(@RequestParam("username") String username, @RequestParam("password") String password);
}
