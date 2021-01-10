package ru.rsoi.apigateway.services.cinema;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import feign.FeignException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rsoi.apigateway.client.CinemaClient;
import ru.rsoi.apigateway.client.MovieClient;
import ru.rsoi.apigateway.client.RentClient;
import ru.rsoi.apigateway.exception.*;
import ru.rsoi.apigateway.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CinemaServiceImpl implements CinemaService {
    private static final Logger logger = getLogger(CinemaServiceImpl.class);

    private static String MOVIETOKEN = UUID.randomUUID().toString();
    private static String CINEMATOKEN = UUID.randomUUID().toString();
    private static String RENTTOKEN = UUID.randomUUID().toString();
    private final CinemaClient cinemaClient;
    private final MovieClient movieClient;
    private final RentClient rentClient;

    @Autowired
    public CinemaServiceImpl(CinemaClient cinemaClient, MovieClient movieClient, RentClient rentClient) {
        this.cinemaClient = cinemaClient;
        this.movieClient = movieClient;
        this.rentClient = rentClient;
    }

    public Cinema getCinemaFallback(Integer cinemaId) {
        return new Cinema("", null, null, null, null);
    }

    @HystrixCommand(fallbackMethod = "getCinemaFallback")
    @Nonnull
    @Override
    public Cinema getCinema(@Nonnull Integer cinemaId) {
        logger.info("Чтение кинотеатра с ID {}", cinemaId.toString());
        ResponseEntity<Cinema> response;
        try {
            response = cinemaClient.getCinema(cinemaId, "Bearer " + CINEMATOKEN);
            if (response.getStatusCodeValue() == 200)
                return Objects.requireNonNull(response.getBody());
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginCinema();
                response = cinemaClient.getCinema(cinemaId, "Bearer " + CINEMATOKEN);
                if (response.getStatusCodeValue() == 200)
                    return Objects.requireNonNull(response.getBody());
            }
        }
        return new Cinema("", null, null, null, null);
    }

    public List<RentInfoResponse> getAllMoviesFallback(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        List<RentResponse> rents = getAllRents(cinemaId, page, size);
        List<RentInfoResponse> movies = new ArrayList<>();
        for (RentResponse rent : rents) {
            movies.add(buildRentInfoResponse(rent, null));
        }
        logger.info("Чтение аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}. Элементы {}",
                cinemaId.toString(), page.toString(), size.toString(), movies.toString());
        return movies;
    }

    @HystrixCommand(fallbackMethod = "getAllMoviesFallback")
    @Nonnull
    @Override
    public List<RentInfoResponse> getAllMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        List<RentResponse> rents = getAllRents(cinemaId, page, size);
        List<RentInfoResponse> movies = new ArrayList<>();
        for (RentResponse rent : rents) {
            ResponseEntity<Movie> response;
            try {
                response = movieClient.getMovieFromUid(rent.getMovieUid(), "Bearer " + MOVIETOKEN);
                if (response.getStatusCodeValue() == 200)
                    movies.add(buildRentInfoResponse(rent, response.getBody()));
            } catch (FeignException e) {
                if (e.status() == 401) {
                    loginMovie();
                    response = movieClient.getMovieFromUid(rent.getMovieUid(), "Bearer " + MOVIETOKEN);
                    if (response.getStatusCodeValue() == 200)
                        movies.add(buildRentInfoResponse(rent, response.getBody()));
                }
            }
        }
        logger.info("Чтение аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}. Элементы {}",
                cinemaId.toString(), page.toString(), size.toString(), movies.toString());
        return movies;
    }

    public List<RentInfoResponse> getPrerentMoviesFallback(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        List<RentResponse> rents = getPrerents(cinemaId, page, size);
        List<RentInfoResponse> movies = new ArrayList<>();
        for (RentResponse rent : rents) {
            movies.add(buildRentInfoResponse(rent, null));
        }
        logger.info("Чтение неподтверждённых аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}. Элементы {}",
                cinemaId.toString(), page.toString(), size.toString(), movies.toString());
        return movies;
    }

    @HystrixCommand(fallbackMethod = "getPrerentMoviesFallback")
    @Nonnull
    @Override
    public List<RentInfoResponse> getPrerentMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        List<RentResponse> rents = getPrerents(cinemaId, page, size);
        List<RentInfoResponse> movies = new ArrayList<>();
        for (RentResponse rent : rents) {
            ResponseEntity<Movie> response;
            try {
                response = movieClient.getMovieFromUid(rent.getMovieUid(), "Bearer " + MOVIETOKEN);
                if (response.getStatusCodeValue() == 200)
                    movies.add(buildRentInfoResponse(rent, response.getBody()));
            } catch (FeignException e) {
                if (e.status() == 401) {
                    loginMovie();
                    response = movieClient.getMovieFromUid(rent.getMovieUid(), "Bearer " + MOVIETOKEN);
                    if (response.getStatusCodeValue() == 200)
                        movies.add(buildRentInfoResponse(rent, response.getBody()));
                }
            }
        }
        logger.info("Чтение неподтверждённых аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}. Элементы {}",
                cinemaId.toString(), page.toString(), size.toString(), movies.toString());
        return movies;
    }

    public List<RentInfoResponse> getRentMoviesFallback(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        List<RentResponse> rents = getRents(cinemaId, page, size);
        List<RentInfoResponse> movies = new ArrayList<>();
        for (RentResponse rent : rents) {
            movies.add(buildRentInfoResponse(rent, null));
        }
        logger.info("Чтение подтверждённых аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}. Элементы {}",
                cinemaId.toString(), page.toString(), size.toString(), movies.toString());
        return movies;
    }

    @HystrixCommand(fallbackMethod = "getRentMoviesFallback")
    @Nonnull
    @Override
    public List<RentInfoResponse> getRentMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        List<RentResponse> rents = getRents(cinemaId, page, size);
        List<RentInfoResponse> movies = new ArrayList<>();
        for (RentResponse rent : rents) {
            ResponseEntity<Movie> response;
            try {
                response = movieClient.getMovieFromUid(rent.getMovieUid(), "Bearer " + MOVIETOKEN);
                if (response.getStatusCodeValue() == 200)
                    movies.add(buildRentInfoResponse(rent, response.getBody()));
            } catch (FeignException e) {
                if (e.status() == 401) {
                    loginMovie();
                    response = movieClient.getMovieFromUid(rent.getMovieUid(), "Bearer " + MOVIETOKEN);
                    if (response.getStatusCodeValue() == 200)
                        movies.add(buildRentInfoResponse(rent, response.getBody()));
                }
            }
        }
        logger.info("Чтение подтверждённых аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}. Элементы {}",
                cinemaId.toString(), page.toString(), size.toString(), movies.toString());
        return movies;
    }

    private String getCinemaSUuid(@Nonnull Integer cinemaId){
        ResponseEntity<UUID> response;
        try {
            response = cinemaClient.getUuid(cinemaId, "Bearer " + CINEMATOKEN);
            if (response.getStatusCodeValue() == 200)
                if (response.getBody() == null)
                    return null;
                else
                return response.getBody().toString();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginCinema();
                response = cinemaClient.getUuid(cinemaId, "Bearer " + CINEMATOKEN);
                if (response.getStatusCodeValue() == 200)
                    if (response.getBody() == null)
                        return null;
                    else
                        return response.getBody().toString();
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    private List<RentResponse> getAllMoviesFromCinemaUuid(UUID uuid, Integer page, Integer size) {
        ResponseEntity<List<RentResponse>> response;
        try {
            response = rentClient.getAllMovies(uuid, page, size, "Bearer " + RENTTOKEN);
            if (response.getStatusCodeValue() == 200)
                if (response.getBody() == null)
                    return null;
                else
                    return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginRent();
                response = rentClient.getAllMovies(uuid, page, size, "Bearer " + RENTTOKEN);
                if (response.getStatusCodeValue() == 200)
                    if (response.getBody() == null)
                        return null;
                    else
                        return response.getBody();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private List<RentResponse> getPrerentMoviesFromCinemaUuid(UUID uuid, Integer page, Integer size) {
        ResponseEntity<List<RentResponse>> response;
        try {
            response = rentClient.getPrerentMovies(uuid, page, size, "Bearer " + RENTTOKEN);
            if (response.getStatusCodeValue() == 200)
                if (response.getBody() == null)
                    return null;
                else
                    return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginRent();
                response = rentClient.getPrerentMovies(uuid, page, size, "Bearer " + RENTTOKEN);
                if (response.getStatusCodeValue() == 200)
                    if (response.getBody() == null)
                        return null;
                    else
                        return response.getBody();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private List<RentResponse> getRentMoviesFromCinemaUuid(UUID uuid, Integer page, Integer size) {
        ResponseEntity<List<RentResponse>> response;
        try {
            response = rentClient.getRentMovies(uuid, page, size, "Bearer " + RENTTOKEN);
            if (response.getStatusCodeValue() == 200)
                if (response.getBody() == null)
                    return null;
                else
                    return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginRent();
                response = rentClient.getRentMovies(uuid, page, size, "Bearer " + RENTTOKEN);
                if (response.getStatusCodeValue() == 200)
                    if (response.getBody() == null)
                        return null;
                    else
                        return response.getBody();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private RentInfoResponse buildRentInfoResponse(RentResponse rent, Movie movie){
        return new RentInfoResponse()
                .setId(rent.getId())
                .setMovie(movie)
                .setConfirmed(rent.getConfirmed())
                .setDuration(rent.getDuration())
                .setPrice(rent.getPrice());
    }

    private List<RentResponse> getAllRents(Integer cinemaId, Integer page, Integer size) {
        String suuid = getCinemaSUuid(cinemaId);
        if (suuid == null)
            throw new CinemaException();
        if (suuid.equals(""))
            throw new CinemaServiceAccessException();
        UUID uuid = UUID.fromString(suuid);
        List<RentResponse> rents = getAllMoviesFromCinemaUuid(uuid, page, size);
        if (rents == null)
            throw new RentServiceAccessException();
        return rents;
    }

    private List<RentResponse> getPrerents(Integer cinemaId, Integer page, Integer size) {
        String suuid = getCinemaSUuid(cinemaId);
        if (suuid == null)
            throw new CinemaException();
        if (suuid.equals(""))
            throw new CinemaServiceAccessException();
        UUID uuid = UUID.fromString(suuid);
        List<RentResponse> rents = getPrerentMoviesFromCinemaUuid(uuid, page, size);
        if (rents == null)
            throw new RentServiceAccessException();
        return rents;
    }

    private List<RentResponse> getRents(Integer cinemaId, Integer page, Integer size) {
        String suuid = getCinemaSUuid(cinemaId);
        if (suuid == null)
            throw new CinemaException();
        if (suuid.equals(""))
            throw new CinemaServiceAccessException();
        UUID uuid = UUID.fromString(suuid);
        List<RentResponse> rents = getRentMoviesFromCinemaUuid(uuid, page, size);
        if (rents == null)
            throw new RentServiceAccessException();
        return rents;
    }

    private void loginMovie() {
        MOVIETOKEN = movieClient.getToken(movieClient.APPID, movieClient.APPSECRET);
    }

    private void loginCinema() {
        CINEMATOKEN = cinemaClient.getToken(cinemaClient.APPID, cinemaClient.APPSECRET);
    }

    private void loginRent() {
        RENTTOKEN = rentClient.getToken(rentClient.APPID, rentClient.APPSECRET);
    }
}
