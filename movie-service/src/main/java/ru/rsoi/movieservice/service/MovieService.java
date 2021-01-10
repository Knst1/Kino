package ru.rsoi.movieservice.service;

import ru.rsoi.movieservice.model.IncMovieCountResponse;
import ru.rsoi.movieservice.model.MovieInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface MovieService {

    @Nonnull
    MovieInfo getMovieInfo(@Nonnull Integer id);

    @Nonnull
    List<MovieInfo> getMovies(@Nonnull Integer page, @Nonnull Integer size);

    UUID getMovieUuid(@Nonnull Integer id);

    @Nonnull
    Integer incCount(@Nonnull UUID uuid, @Nonnull IncMovieCountResponse response);

    @Nonnull
    MovieInfo getMovieFromUid(@Nonnull UUID uuid);
}