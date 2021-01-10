package ru.rsoi.apigateway.services.cinema;

import ru.rsoi.apigateway.model.Cinema;
import ru.rsoi.apigateway.model.RentInfoResponse;

import javax.annotation.Nonnull;
import java.util.List;

public interface CinemaService {

    @Nonnull
    Cinema getCinema(@Nonnull Integer cinemaId);

    @Nonnull
    List<RentInfoResponse> getAllMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size);

    @Nonnull
    List<RentInfoResponse> getPrerentMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size);

    @Nonnull
    List<RentInfoResponse> getRentMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size);
}