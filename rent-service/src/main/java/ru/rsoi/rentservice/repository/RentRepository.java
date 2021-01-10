package ru.rsoi.rentservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsoi.rentservice.domain.Rent;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface RentRepository extends JpaRepository<Rent, Integer> {

    Page<Rent> findByCinemaUid(@Nonnull UUID id, @Nonnull Pageable page);

    Page<Rent> findByCinemaUidAndConfirmed(@Nonnull UUID id, @Nonnull Boolean confirmed, @Nonnull Pageable page);
}
