package ru.rsoi.apigateway.services.movie;

import ru.rsoi.apigateway.model.Movie;
import ru.rsoi.apigateway.model.ShortMovie;

import javax.annotation.Nonnull;
import java.util.List;

public interface MovieService {
    List<ShortMovie> allMovies(@Nonnull Integer page, @Nonnull Integer size);

    @Nonnull
    Movie getMovie(@Nonnull Integer movieId);
}