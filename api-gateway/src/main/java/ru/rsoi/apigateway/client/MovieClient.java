package ru.rsoi.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rsoi.apigateway.model.IncMovieCountRequest;
import ru.rsoi.apigateway.model.Movie;
import ru.rsoi.apigateway.model.ShortMovie;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@FeignClient("movie-service")
public interface MovieClient {
    String APPID = "c119621b-21f9-4305-a90c-5df5c5d0150e";
    String APPSECRET = "168f38d0-f3aa-4e98-ab3b-10d7a78ef279";

    @GetMapping(value = "/movie", params = {"page", "size"})
    ResponseEntity<List<ShortMovie>> readMovie(@RequestParam("page") Integer page, @RequestParam("size") Integer size,
                                               @RequestHeader("Authorization") String token);

    @GetMapping(value = "/movie/{movieId}")
    ResponseEntity<Movie> getMovie(@PathVariable Integer movieId, @RequestHeader("Authorization") String token);

    @GetMapping(value = "/movie/fromuid/{movieUid}")
    ResponseEntity<Movie> getMovieFromUid(@PathVariable UUID movieUid, @RequestHeader("Authorization") String token);

    @GetMapping(value = "/movie/uuid/{movieId}")
    ResponseEntity<UUID> getUuid(@PathVariable Integer movieId, @RequestHeader("Authorization") String token);

    @PostMapping(value = "/movie/{movieUuid}")
    ResponseEntity<Integer> incCount(@PathVariable UUID movieUuid, @RequestBody @Valid IncMovieCountRequest request,
                                     @RequestHeader("Authorization") String token);

    @PostMapping("/token")
    String getToken(@RequestParam("username") String username, @RequestParam("password") String password);
}
