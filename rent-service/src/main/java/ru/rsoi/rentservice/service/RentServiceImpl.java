package ru.rsoi.rentservice.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.rsoi.rentservice.domain.Rent;
import ru.rsoi.rentservice.model.RentInfo;
import ru.rsoi.rentservice.model.RentResponse;
import ru.rsoi.rentservice.repository.RentRepository;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RentServiceImpl implements RentService {
    private static final Logger logger = getLogger(RentServiceImpl.class);

    private final RentRepository rentRepository;

    @Autowired
    public RentServiceImpl(RentRepository movieRepository) {
        this.rentRepository = movieRepository;
    }

    @Nonnull
    @Override
    public RentResponse addRent(@Nonnull RentInfo request) {
        final Rent rent = buildRent(request);
        rentRepository.save(rent);
        logger.info("Добавлена новая аренда '{}'", rent.toString());
        return buildRentResponse(rent);
    }

    @Nonnull
    @Override
    public RentInfo updateRent(@Nonnull Integer rentId) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);
        if (optionalRent.isEmpty())
            return new RentInfo();
        Rent rent = optionalRent.get();
        Boolean oldConfirmed = rent.getConfirmed();
        rent.setConfirmed(true);
        rentRepository.save(rent);
        logger.info("Подтверждена аренда {}", rent.toString());
        return buildRentInfo(rent.setConfirmed(oldConfirmed));
    }

    @Nonnull
    @Override
    public RentInfo updateDuration(@Nonnull Integer rentId, @Nonnull Integer duration) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);
        if (optionalRent.isEmpty())
            return new RentInfo();
        Rent rent = optionalRent.get();
        if (!rent.getConfirmed() && duration>=0) {
            rent.setPrice(rent.getPrice()/rent.getDuration()*duration);
            rent.setDuration(duration);
            rentRepository.save(rent);
            logger.info("Изменён срок аренды {} на {} дней", rent.toString(), duration);
        }
        return buildRentInfo(rent);
    }

    @Nonnull
    @Override
    public RentInfo deleteRent(@Nonnull Integer rentId) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);
        RentInfo rent = new RentInfo();
        if (optionalRent.isPresent()) {
            rent = buildRentInfo(optionalRent.get());
            if (!rent.getConfirmed()) {
                rentRepository.deleteById(rentId);
                logger.info("Удалена аренда {}", rent.toString());
            }
        }
        return rent;
    }

    @Nonnull
    @Override
    public List<RentInfo> getAllMovies(@Nonnull UUID cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        if (size<1 || size>1000)
            size = 21;
        List<Rent> rents = rentRepository.findByCinemaUid(cinemaId, PageRequest.of(Math.max(0, page-1), size,
                Sort.by("id").descending())).toList();
        logger.info("Чтение всех аренд кинотеатра с UUID {}. Пагинация: страница {}, элементов {}.",
                cinemaId.toString(), page.toString(), size.toString());
        return getMovies(rents);
    }

    @Nonnull
    @Override
    public List<RentInfo> getPrerentMovies(@Nonnull UUID cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        if (size<1 || size>1000)
            size = 21;
        List<Rent> rents = rentRepository.findByCinemaUidAndConfirmed(cinemaId, Boolean.FALSE,
                PageRequest.of(Math.max(0, page-1), size, Sort.by("id").descending())).toList();
        logger.info("Чтение неподтверждённых аренд кинотеатра с UUID {}. Пагинация: страница {}, элементов {}.",
                    cinemaId.toString(), page.toString(), size.toString());
        return getMovies(rents);
    }

    @Nonnull
    @Override
    public List<RentInfo> getRentMovies(@Nonnull UUID cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        if (size<1 || size>1000)
            size = 21;
        List<Rent> rents = rentRepository.findByCinemaUidAndConfirmed(cinemaId, Boolean.TRUE,
                PageRequest.of(Math.max(0, page-1), size, Sort.by("id").descending())).toList();
        logger.info("Чтение подтверждённых аренд кинотеатра с UUID {}. Пагинация: страница {}, элементов {}.",
                    cinemaId.toString(), page.toString(), size.toString());
        return getMovies(rents);
    }

    @Nonnull
    @Override
    public RentInfo getRent(@Nonnull Integer rentId) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);
        RentInfo rent = new RentInfo();
        if (optionalRent.isPresent()) {
            rent = buildRentInfo(optionalRent.get());
            logger.info("Чтение аренды {}.", rent.toString());
        }
        return rent;
    }

    @Nonnull
    private List<RentInfo> getMovies(@Nonnull List<Rent> rents) {
        List<RentInfo> ans = new ArrayList<>();
        for (Rent rent : rents)
            ans.add(buildRentInfo(rent));
        return ans;
    }

    @Nonnull
    private Rent buildRent(@Nonnull RentInfo request) {
        return new Rent()
                .setCinemaUid(request.getCinemaUid())
                .setMovieUid(request.getMovieUid())
                .setConfirmed(false)
                .setDuration(request.getDuration())
                .setPrice(request.getPrice());
    }

    @Nonnull
    private RentInfo buildRentInfo(@Nonnull Rent rent) {
        return new RentInfo()
                .setId(rent.getId())
                .setCinemaUid(rent.getCinemaUid())
                .setMovieUid(rent.getMovieUid())
                .setDuration(rent.getDuration())
                .setPrice(rent.getPrice())
                .setConfirmed(rent.getConfirmed());
    }

    @Nonnull
    private RentResponse buildRentResponse(@Nonnull Rent rent) {
        return new RentResponse()
                .setId(rent.getId())
                .setDuration(rent.getDuration())
                .setPrice(rent.getPrice())
                .setConfirmed(rent.getConfirmed());
    }
}
