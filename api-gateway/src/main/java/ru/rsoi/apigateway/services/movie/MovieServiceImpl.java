package ru.rsoi.apigateway.services.movie;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import feign.FeignException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rsoi.apigateway.client.MovieClient;
import ru.rsoi.apigateway.model.Movie;
import ru.rsoi.apigateway.model.ShortMovie;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class MovieServiceImpl implements MovieService {
    private static final Logger logger = getLogger(MovieServiceImpl.class);

    private static String TOKEN = UUID.randomUUID().toString();
    private final MovieClient movieClient;

    @Autowired
    public MovieServiceImpl(MovieClient movieClient) {
        this.movieClient = movieClient;
    }

    public Movie getMovieFallback(Integer movieId) {
        return new Movie().setCost(-1);
    }

    public List<ShortMovie> allMoviesFallback(Integer page, Integer size) {
        return null;
    }

    @HystrixCommand(fallbackMethod = "getMovieFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "31"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "11000"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")})
    @Nonnull
    @Override
    public Movie getMovie(@Nonnull Integer movieId) {
        logger.info("Чтение фильма по ID {}", movieId.toString());
        ResponseEntity<Movie> response;
        try {
            response = movieClient.getMovie(movieId, "Bearer " + TOKEN);
            if (response.getStatusCodeValue() == 200)
                return Objects.requireNonNull(response.getBody());
        } catch (FeignException e) {
            if (e.status() == 401) {
                login();
                response = movieClient.getMovie(movieId, "Bearer " + TOKEN);
                if (response.getStatusCodeValue() == 200)
                    return Objects.requireNonNull(response.getBody());
            }
        }
        return new Movie().setCost(-1);
    }

    @HystrixCommand(fallbackMethod = "allMoviesFallback")
    @Override
    public List<ShortMovie> allMovies(@Nonnull Integer page, @Nonnull Integer size) {
        logger.info("Чтение списка фильмов. Пагинация: страница {}, элементов {}", page.toString(), size.toString());
        ResponseEntity<List<ShortMovie>> response;
        try {
            response = movieClient.readMovie(page, size, "Bearer " + TOKEN);
            if (response.getStatusCodeValue() == 200)
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                login();
                response = movieClient.readMovie(page, size, "Bearer " + TOKEN);
                if (response.getStatusCodeValue() == 200)
                    return response.getBody();
            }
        }
        return null;
    }

    private void login() {
        TOKEN = movieClient.getToken(movieClient.APPID, movieClient.APPSECRET);
    }
}
