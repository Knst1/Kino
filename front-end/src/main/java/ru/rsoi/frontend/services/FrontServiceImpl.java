package ru.rsoi.frontend.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rsoi.frontend.client.RestApiClient;

import ru.rsoi.frontend.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class FrontServiceImpl implements FrontService {
    private static final Logger logger = getLogger(FrontServiceImpl.class);

    private final RestApiClient client;

    @Autowired
    public FrontServiceImpl(RestApiClient client) {
        this.client = client;
    }

    @Override
    public Cinema getCinema(@Nonnull Integer id) {
        ResponseEntity<Cinema> response;
        try{ response = client.getCinema(id);}
        catch(Exception e){
            return null;
        }
        logger.info("Чтение кинотеатра с ID {}", id);
        if (response.getStatusCodeValue() == 200)
            return response.getBody();
        return null;
    }

    @Override
    public List<ShortMovie> getMovies(@Nonnull Integer page, @Nonnull Integer size) {
        ResponseEntity<List<ShortMovie>> response;
        try{ response = client.allMovies(page, size);}
        catch(Exception e){
            return null;
        }
        logger.info("Чтение списка фильмов. Пагинация: страница {}, элементов {}", page.toString(), size.toString());
        if (response.getStatusCodeValue() == 200)
            return response.getBody();
        return null;
    }

    @Override
    public Movie getMovie(@Nonnull Integer id) {
        ResponseEntity<Movie> response;
        try{ response = client.getMovie(id);}
        catch(Exception e){
            return null;
        }
        logger.info("Чтение фильма с ID {}", id.toString());
        if (response.getStatusCodeValue() == 200)
            return response.getBody();
        if (response.getStatusCodeValue() == 404)
            return new Movie();
        return null;
    }

    @Override
    public RentInfoResponse addRent(@Nonnull AddRentInfoBody body) {
        ResponseEntity<RentInfoResponse> response;
        try {
            response = client.addRent(body);
        }
        catch(Exception e)
        {return null;}
        logger.info("Добавление аренды {}", body.toString());
        if (response.getStatusCodeValue() == 200)
            return response.getBody();
        return null;
    }

    @Override
    public List<RentInfoResponse> getAllMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        ResponseEntity<List<RentInfoResponse>> response;
        try {
            response = client.getAllMovies(cinemaId, page, size);
        }
        catch(Exception e)
        {return null;}
        logger.info("Чтение всех аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}.\nСписок {}",
                cinemaId.toString(), page.toString(), size.toString(), response.toString());
        if (response.getStatusCodeValue() == 200)
            return response.getBody();
        return null;
    }

    @Override
    public List<RentInfoResponse> getPrerentMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        ResponseEntity<List<RentInfoResponse>> response;
        try {
            response = client.getPrerentMovies(cinemaId, page, size);
        }
        catch(Exception e)
        {return null;}
        logger.info("Чтение неподтверждённых аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}.\nСписок {}",
                cinemaId.toString(), page.toString(), size.toString(), response.toString());
        if (response.getStatusCodeValue() == 200)
            return response.getBody();
        return null;
    }

    @Override
    public List<RentInfoResponse> getRentMovies(@Nonnull Integer cinemaId, @Nonnull Integer page, @Nonnull Integer size) {
        ResponseEntity<List<RentInfoResponse>> response;
        try {
            response = client.getRentMovies(cinemaId, page, size);
        }
        catch(Exception e)
        {return null;}
        logger.info("Чтение подтверждённых аренд кинотеатра с ID {}. Пагинация: страница {}, элементов {}.\nСписок {}",
                cinemaId.toString(), page.toString(), size.toString(), response.toString());
        if (response.getStatusCodeValue() == 200)
            return response.getBody();
        return null;
    }

    public Boolean setDurationFallback(@Nonnull Integer cinemaId, @Nonnull Integer rentId, @Nonnull Integer duration) {
        return null;
    }

    @HystrixCommand(fallbackMethod = "setDurationFallback")
    @Override
    public Boolean setDuration(@Nonnull Integer cinemaId, @Nonnull Integer rentId, @Nonnull Integer duration) {
        ResponseEntity<Boolean> boolRresponse = client.rentOwner(cinemaId, rentId);
        if (boolRresponse.getStatusCodeValue() != 200)
            return null;
        if (boolRresponse.getBody() == null || !boolRresponse.getBody())
            return false;
        ResponseEntity<RentRequest> durationResponse = client.updateDuration(rentId, duration);
        if (durationResponse.getStatusCodeValue() != 200)
            return null;
        return true;
    }


    @Override
    public Boolean confirmRent(@Nonnull Integer cinemaId, @Nonnull Integer rentId) {
        ResponseEntity<Boolean> boolRresponse = client.rentOwner(cinemaId, rentId);
        if (boolRresponse.getStatusCodeValue() != 200)
            return null;
        if (boolRresponse.getBody() == null || !boolRresponse.getBody())
            return false;
        ResponseEntity<RentRequest> response = client.confirmRent(rentId);
        logger.info("Подтверждение аренды с ID {}", rentId);
        if (response.getStatusCodeValue() != 200)
            return null;
        return true;
    }

    public Boolean deleteRentFallback(@Nonnull Integer cinemaId, @Nonnull Integer rentId) {
        return null;
    }

    @HystrixCommand(fallbackMethod = "deleteRentFallback")
    @Override
    public Boolean deleteRent(@Nonnull Integer cinemaId, @Nonnull Integer rentId) {
        ResponseEntity<Boolean> boolRresponse = client.rentOwner(cinemaId, rentId);
        if (boolRresponse.getStatusCodeValue() != 200)
            return null;
        if (boolRresponse.getBody() == null || !boolRresponse.getBody())
            return false;
        ResponseEntity<RentRequest> response = client.deleteRent(rentId);
        logger.info("Удаление аренды с ID {}", rentId);
        if (response.getStatusCodeValue() != 200)
            return null;
        return true;
    }
}
