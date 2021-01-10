package ru.rsoi.cinemaservice.service;

import ru.rsoi.cinemaservice.model.CinemaInfo;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface CinemaService {

    @Nonnull
    CinemaInfo getCinemaInfo(@Nonnull Integer id);

    UUID getCinemaUuid(@Nonnull Integer id);

    @Nonnull
    Integer incCount(@Nonnull Integer id);

    @Nonnull
    Integer decCount(@Nonnull UUID uid);
}