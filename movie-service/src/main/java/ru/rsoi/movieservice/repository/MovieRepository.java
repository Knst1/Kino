package ru.rsoi.movieservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsoi.movieservice.domain.Movie;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Optional<Movie> findById(@Nonnull Integer id);

    Optional<Movie> findByMovieUid(@Nonnull UUID uuid);

    Page<Movie> findAll(@Nonnull Pageable page);
}
