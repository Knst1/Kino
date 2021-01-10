package ru.rsoi.cinemaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsoi.cinemaservice.domain.Cinema;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public interface CinemaRepository extends JpaRepository<Cinema, Integer> {

    Optional<Cinema> findById(@Nonnull Integer id);

    Optional<Cinema> findByCinemaUid(@Nonnull UUID uid);
}
