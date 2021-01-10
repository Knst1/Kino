package ru.rsoi.rentservice.service;

import ru.rsoi.rentservice.model.RentInfo;
import ru.rsoi.rentservice.model.RentResponse;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface RentService {

    @Nonnull
    RentResponse addRent(@Nonnull RentInfo request);

    @Nonnull
    RentInfo updateRent(@Nonnull Integer rentId);

    @Nonnull
    RentInfo updateDuration(@Nonnull Integer rentId, @Nonnull Integer duration);

    @Nonnull
    RentInfo deleteRent(@Nonnull Integer rentId);

    @Nonnull
    List<RentInfo> getRentMovies(@Nonnull UUID cinemaId, @Nonnull Integer page, @Nonnull Integer size);

    @Nonnull
    List<RentInfo> getPrerentMovies(@Nonnull UUID cinemaId, @Nonnull Integer page, @Nonnull Integer size);

    @Nonnull
    List<RentInfo> getAllMovies(@Nonnull UUID cinemaId, @Nonnull Integer page, @Nonnull Integer size);

    @Nonnull
    RentInfo getRent(@Nonnull Integer rentId);
}