package ru.rsoi.apigateway.services.rent;

import feign.FeignException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rsoi.apigateway.client.CinemaClient;
import ru.rsoi.apigateway.client.MovieClient;
import ru.rsoi.apigateway.client.RentClient;
import ru.rsoi.apigateway.exception.*;
import ru.rsoi.apigateway.model.*;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RentServiceImpl implements RentService {
    private static final Logger logger = getLogger(RentServiceImpl.class);

    private static String MOVIETOKEN = UUID.randomUUID().toString();
    private static String CINEMATOKEN = UUID.randomUUID().toString();
    private static String RENTTOKEN = UUID.randomUUID().toString();
    private Map<UUID, Integer> counter;
    private BlockingQueue<HashMap.SimpleEntry<UUID, IncMovieCountRequest>> queue;
    private final MovieClient movieClient;
    private final RentClient rentClient;
    private final CinemaClient cinemaClient;

    @Autowired
    public RentServiceImpl(CinemaClient cinemaClient, MovieClient movieClient, RentClient rentClient) {
        this.movieClient = movieClient;
        this.rentClient = rentClient;
        this.cinemaClient = cinemaClient;
        counter = new HashMap<>();
        queue = new LinkedBlockingDeque<>();
        Thread queueThread = new Thread(this::queueThreadRunner,"queueThread");
        queueThread.start();
    }

    @Nonnull
    @Override
    public RentRequest confirmRent(@Nonnull Integer rentId) {
        RentRequest rent = setConfirmedTrue(rentId);
        if (rent == null)
            throw new RentServiceAccessException();
        if (rent.getCinemaUid() == null)
            throw new RentException();
        if (!rent.getConfirmed()) {
            IncMovieCountRequest request = new IncMovieCountRequest(rent.getDuration(), rentId);
            Integer id = incMovieCount(rent.getMovieUid(), request);
            if (id == null) {
                boolean durationAdded;
                try {
                    durationAdded = queue.offer(new HashMap.SimpleEntry<>(rent.getMovieUid(), request),
                            5L,
                            TimeUnit.SECONDS);
                }
                catch(InterruptedException e) {
                    throw new MovieServiceAccessException();
                }
                if (!durationAdded)
                    throw new MovieServiceAccessException();
            } else if (id == -1)
                throw new MovieException();
        }
        logger.info("Подтверждена аренда {}", rent.toString());
        return rent;
    }

    @Nonnull
    @Override
    public RentRequest updateDuration(@Nonnull Integer rentId, @Nonnull Integer duration) {
        RentRequest rent = updateRentDuration(rentId, duration);
        if (rent == null)
            throw new RentServiceAccessException();
        if (rent.getCinemaUid() == null)
            throw new RentException();
        logger.info("Изменена продолжительность аренды с ID {} на значение {}", rentId.toString(), duration.toString());
        return rent;
    }

    @Nonnull
    @Override
    public RentRequest deleteRent(@Nonnull Integer rentId) {
        RentRequest rent = delete(rentId);
        if (rent == null)
            throw new RentServiceAccessException();
        if (rent.getCinemaUid() == null)
            throw new RentException();
        if (!rent.getConfirmed()) {
            Integer id = decCinemaCount(rent.getCinemaUid());
            if (id == null)
                throw new CinemaServiceAccessException();
            logger.info("Удалена аренда {}", rent.toString());
        }
        return rent;
    }

    @Nonnull
    @Override
    public RentInfoResponse addRent(@Nonnull Integer cinemaId, @Nonnull Integer movieId, @Nonnull Integer duration) {
        String cinemaSUuid = getCinemaSUuid(cinemaId);
        if (cinemaSUuid == null)
            throw new CinemaException();
        if (cinemaSUuid.equals(""))
            throw new CinemaServiceAccessException();
        UUID cinemaUuid = UUID.fromString(cinemaSUuid);
        String movieSUuid = getMovieSUuid(movieId);
        if (movieSUuid == null)
            throw new MovieException();
        if (movieSUuid.equals(""))
            throw new MovieServiceAccessException();
        UUID movieUuid = UUID.fromString(movieSUuid);
        Movie movie = getMovie(movieId);
        if (movie == null)
            throw new MovieServiceAccessException();
        if (movie.getName() == null)
            throw new MovieException();
        RentRequest rentRequest = new RentRequest(cinemaUuid,
                movieUuid,
                duration,
                movie.getCost() * duration,
                false);
        Integer cinID = incCinemaCount(cinemaId);
        if (cinID == null)
            throw new CinemaServiceAccessException();
        if (cinID == -1)
            throw new CinemaException();
        RentResponse rent = addRent(rentRequest);
        if (rent == null)
            if (decCinemaCount(cinemaUuid) == null)
            {
                if (counter.containsKey(cinemaUuid))
                    counter.put(cinemaUuid, counter.get(cinemaUuid) + 1);
                else
                    counter.put(cinemaUuid, 1);
            }
        if (rent == null)
            throw new RentServiceAccessException();
        logger.info("Добавлена аренда {}", rent.toString());
        return new RentInfoResponse()
                .setId(rent.getId())
                .setMovie(movie)
                .setConfirmed(false)
                .setDuration(rent.getDuration())
                .setPrice(rent.getPrice());
    }

    @Nonnull
    @Override
    public Boolean rentOwner(@Nonnull Integer cinemaId, @Nonnull Integer rentId) {
        ResponseEntity<UUID> response;
        UUID cinemaUid = null;
        try {
            response = cinemaClient.getUuid(cinemaId, "Bearer " + CINEMATOKEN);
            if (response.getStatusCodeValue() == 200)
                cinemaUid = response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginCinema();
                response = cinemaClient.getUuid(cinemaId, "Bearer " + CINEMATOKEN);
                if (response.getStatusCodeValue() == 200)
                    cinemaUid = response.getBody();
            }
        } catch(Exception e) {
            throw new CinemaServiceAccessException();
        }
        if (cinemaUid == null) {
            return false;
        }
        while (counter.containsKey(cinemaUid)) {
            if (counter.get(cinemaUid) > 0) {
                if (decCinemaCount(cinemaUid) != null) {
                    counter.put(cinemaUid, counter.get(cinemaUid) - 1);
                } else break;
            } else counter.remove(cinemaUid);
        }
        ResponseEntity<RentRequest> rentResponse;
        RentRequest rent = null;
        try {
            rentResponse = rentClient.getRent(rentId, "Bearer " + RENTTOKEN);
            if (rentResponse.getStatusCodeValue() == 200)
                rent = rentResponse.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginRent();
                rentResponse = rentClient.getRent(rentId, "Bearer " + RENTTOKEN);
                if (rentResponse.getStatusCodeValue() == 200)
                    rent = rentResponse.getBody();
            }
        } catch(Exception e) {
            throw new RentServiceAccessException();
        }
        if (rent == null)
            throw new RentServiceAccessException();
        if (rent.getCinemaUid() == null) {
            return false;
        }
        return rent.getCinemaUid().equals(cinemaUid);
    }

    private RentRequest setConfirmedTrue(Integer rentId) {
        ResponseEntity<RentRequest> response;
        RentRequest rent = null;
        try {
            response = rentClient.confirmRent(rentId, "Bearer " + RENTTOKEN);
            if (response.getStatusCodeValue() == 200)
                rent = response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginRent();
                response = rentClient.confirmRent(rentId, "Bearer " + RENTTOKEN);
                if (response.getStatusCodeValue() == 200)
                    rent = response.getBody();
            }
        } catch(Exception e) {
            return null;
        }
        return rent;
    }

    private synchronized Integer incMovieCount(UUID uuid, IncMovieCountRequest request) {
        ResponseEntity<Integer> response;
        try {
            response = movieClient.incCount(uuid, request, "Bearer " + MOVIETOKEN);
            if (response.getStatusCodeValue() == 200)
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginMovie();
                response = movieClient.incCount(uuid, request, "Bearer " + MOVIETOKEN);
                if (response.getStatusCodeValue() == 200)
                    return response.getBody();
            }
        } catch(Exception e) {
            return null;
        }
        return null;
    }

    private RentRequest updateRentDuration(Integer rentId, Integer duration) {
        ResponseEntity<RentRequest> response;
        try {
            response = rentClient.updateDuration(rentId, duration, "Bearer " + RENTTOKEN);
            if (response.getStatusCodeValue() == 200)
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginRent();
                response = rentClient.updateDuration(rentId, duration, "Bearer " + RENTTOKEN);
                if (response.getStatusCodeValue() == 200)
                    return response.getBody();
            }
        } catch(Exception e) {
            return null;
        }
        return null;
    }

    private RentRequest delete(Integer rentId) {
        ResponseEntity<RentRequest> response;
        try {
            response = rentClient.deleteRent(rentId, "Bearer " + RENTTOKEN);
            if (response.getStatusCodeValue() == 200)
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginRent();
                response = rentClient.deleteRent(rentId, "Bearer " + RENTTOKEN);
                if (response.getStatusCodeValue() == 200)
                    return response.getBody();
            }
        } catch(Exception e) {
            return null;
        }
        return null;
    }

    private Integer decCinemaCount(UUID uuid) {
        ResponseEntity<Integer> response;
        try {
            response = cinemaClient.decCount(uuid, "Bearer " + CINEMATOKEN);
            if (response.getStatusCodeValue() == 200)
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginCinema();
                response = cinemaClient.decCount(uuid, "Bearer " + CINEMATOKEN);
                if (response.getStatusCodeValue() == 200)
                    return response.getBody();
            }
        } catch(Exception e) {
            return null;
        }
        return null;
    }

    private String getCinemaSUuid(@Nonnull Integer cinemaId) {
        ResponseEntity<UUID> response;
        UUID uuid = null;
        try {
            response = cinemaClient.getUuid(cinemaId, "Bearer " + CINEMATOKEN);
            if (response.getStatusCodeValue() == 200)
                uuid = response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginCinema();
                response = cinemaClient.getUuid(cinemaId, "Bearer " + CINEMATOKEN);
                if (response.getStatusCodeValue() == 200)
                    uuid = response.getBody();
            }
        } catch (Exception e) {
            return "";
        }
        if (uuid == null)
            return null;
        return uuid.toString();
    }

    private String getMovieSUuid(@Nonnull Integer movieId){
        ResponseEntity<UUID> response;
        UUID uuid = null;
        try {
            response = movieClient.getUuid(movieId, "Bearer " + MOVIETOKEN);
            if (response.getStatusCodeValue() == 200)
                uuid = response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginMovie();
                response = movieClient.getUuid(movieId, "Bearer " + MOVIETOKEN);
                if (response.getStatusCodeValue() == 200)
                    uuid = response.getBody();
            }
        } catch(Exception e) {
            return "";
        }
        if (uuid == null)
            return null;
        return uuid.toString();
    }

    private Movie getMovie(Integer movieId) {
        ResponseEntity<Movie> response;
        try {
            response = movieClient.getMovie(movieId, "Bearer " + MOVIETOKEN);
            if (response.getStatusCodeValue() == 200)
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginMovie();
                response = movieClient.getMovie(movieId, "Bearer " + MOVIETOKEN);
                if (response.getStatusCodeValue() == 200)
                    return response.getBody();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private RentResponse addRent(RentRequest rent) {
        ResponseEntity<RentResponse> response;
        try {
            response = rentClient.addRent(rent, "Bearer " + RENTTOKEN);
            if (response.getStatusCodeValue() == 200)
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginRent();
                response = rentClient.addRent(rent, "Bearer " + RENTTOKEN);
                if (response.getStatusCodeValue() == 200)
                    return response.getBody();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private Integer incCinemaCount(Integer cinemaId) {
        ResponseEntity<Integer> response;
        try {
            response = cinemaClient.incCount(cinemaId, "Bearer " + CINEMATOKEN);
            if (response.getStatusCodeValue() == 200)
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 401) {
                loginCinema();
                response = cinemaClient.incCount(cinemaId, "Bearer " + CINEMATOKEN);
                if (response.getStatusCodeValue() == 200)
                    return response.getBody();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private void queueThreadRunner() {
        Map.Entry<UUID, IncMovieCountRequest> entry;
        while (true)
        {
            try {
                entry = queue.poll(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {entry = null;}
            if (entry != null) {
                Integer id = incMovieCount(entry.getKey(), entry.getValue());
                while (id == null) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ignored) {
                    }
                    id = incMovieCount(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private void loginMovie() {
        MOVIETOKEN = movieClient.getToken(movieClient.APPID, movieClient.APPSECRET);
    }

    private void loginCinema() {
        CINEMATOKEN = cinemaClient.getToken(cinemaClient.APPID, cinemaClient.APPSECRET);
    }

    private void loginRent() {
        RENTTOKEN = rentClient.getToken(rentClient.APPID, rentClient.APPSECRET);
    }
}
