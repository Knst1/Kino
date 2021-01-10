package ru.rsoi.apigateway.services.rent;

import ru.rsoi.apigateway.model.RentInfoResponse;
import ru.rsoi.apigateway.model.RentRequest;

import javax.annotation.Nonnull;

public interface RentService {
    @Nonnull
    RentRequest confirmRent(@Nonnull Integer rentId);

    @Nonnull
    RentRequest updateDuration(@Nonnull Integer rentId, @Nonnull Integer duration);

    @Nonnull
    RentRequest deleteRent(@Nonnull Integer rentId);

    @Nonnull
    RentInfoResponse addRent(@Nonnull Integer cinemaId, @Nonnull Integer movieId, @Nonnull Integer duration);

    @Nonnull
    Boolean rentOwner(@Nonnull Integer cinemaId, @Nonnull Integer rentId);
}