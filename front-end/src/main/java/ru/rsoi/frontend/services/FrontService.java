package ru.rsoi.frontend.services;

import ru.rsoi.frontend.model.*;

import javax.annotation.Nonnull;
import java.util.List;

public interface FrontService {
    Cinema getCinema(@Nonnull Integer id);

    List<ShortMovie> getMovies(@Nonnull Integer page, @Nonnull Integer size);

    Movie getMovie(@Nonnull Integer movieId);

    RentInfoResponse addRent(@Nonnull AddRentInfoBody body);

    List<RentInfoResponse> getAllMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size);

    List<RentInfoResponse> getPrerentMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size);

    List<RentInfoResponse> getRentMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size);

    Boolean setDuration(@Nonnull Integer cinemaId, @Nonnull Integer rentId, @Nonnull Integer duration);

    Boolean confirmRent(@Nonnull Integer cinemaId, @Nonnull Integer rentId);

    Boolean deleteRent(@Nonnull Integer cinemaId, @Nonnull Integer rentId);
}