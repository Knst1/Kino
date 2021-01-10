package ru.rsoi.cinemaservice.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rsoi.cinemaservice.domain.Cinema;
import ru.rsoi.cinemaservice.model.CinemaInfo;
import ru.rsoi.cinemaservice.repository.CinemaRepository;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CinemaServiceImpl implements CinemaService {
    private static final Logger logger = getLogger(CinemaServiceImpl.class);

    private final CinemaRepository cinemaRepository;

    @Autowired
    public CinemaServiceImpl(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Nonnull
    @Override
    public CinemaInfo getCinemaInfo(@Nonnull Integer id) {
        logger.info("Чтение кинотеатра с ID {}", id.toString());
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        if (cinema.isEmpty())
            return new CinemaInfo();
        return cinema.map(this::buildCinemaInfoResponse).get();
    }

    @Override
    public UUID getCinemaUuid(@Nonnull Integer id) {
        logger.info("Чтение UUID кинотеатра с ID {}", id.toString());
        Optional<Cinema> cinema = cinemaRepository.findById(id);
        if (cinema.isEmpty())
            return null;
        return cinema.get().getCinemaUid();
    }

    @Nonnull
    @Override
    public Integer incCount(@Nonnull Integer id) {
        logger.info("Увеличено число аренд кинотеатра с ID {}", id.toString());
        Optional<Cinema> optionalCinema = cinemaRepository.findById(id);
        if (optionalCinema.isEmpty())
            return -1;
        Cinema cinema = optionalCinema.get();
        cinema.setCount(cinema.getCount() + 1);
        cinemaRepository.save(cinema);
        return id;
    }

    @Nonnull
    @Override
    public Integer decCount(@Nonnull UUID uid) {
        logger.info("Увеличено число аренд кинотеатра с UUID {}", uid.toString());
        Optional<Cinema> optionalCinema = cinemaRepository.findByCinemaUid(uid);
        if (optionalCinema.isEmpty())
            return -1;
        Cinema cinema = optionalCinema.get();
        cinema.setCount(cinema.getCount() - 1);
        cinemaRepository.save(cinema);
        return cinema.getId();
    }

    @Nonnull
    private CinemaInfo buildCinemaInfoResponse(@Nonnull Cinema cinema) {
        return new CinemaInfo()
                .setCinemaUid(cinema.getCinemaUid())
                .setName(cinema.getName())
                .setChief(cinema.getChief())
                .setOwner(cinema.getOwner())
                .setPhone(cinema.getPhone())
                .setCount(cinema.getCount())
                .setAddress(cinema.getAddress());
    }
}
