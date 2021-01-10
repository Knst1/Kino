package ru.rsoi.apigateway.exception;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.rsoi.apigateway.model.ErrorResponse;

import static org.slf4j.LoggerFactory.getLogger;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler(CinemaException.class)
    protected ResponseEntity<ErrorResponse> handleCinemaException() {
        logger.warn("Cinema not exist");
        return new ResponseEntity<>(new ErrorResponse("Cinema not exist"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieException.class)
    protected ResponseEntity<ErrorResponse> handleMovieException() {
        logger.warn("Movie not exist");
        return new ResponseEntity<>(new ErrorResponse("Movie not exist"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RentException.class)
    protected ResponseEntity<ErrorResponse> handleRentException() {
        logger.warn("Rent not exist");
        return new ResponseEntity<>(new ErrorResponse("Rent not exist"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieServiceAccessException.class)
    protected ResponseEntity<ErrorResponse> handleMovieServiceAccessException() {
        logger.warn("movie-service is unavailable");
        return new ResponseEntity<>(new ErrorResponse("movie-service is unavailable"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(CinemaServiceAccessException.class)
    protected ResponseEntity<ErrorResponse> handleCinemaServiceAccessException() {
        logger.warn("cinema-service is unavailable");
        return new ResponseEntity<>(new ErrorResponse("cinema-service is unavailable"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(RentServiceAccessException.class)
    protected ResponseEntity<ErrorResponse> handleRentServiceAccessException() {
        logger.warn("rent-service is unavailable");
        return new ResponseEntity<>(new ErrorResponse("rent-service is unavailable"), HttpStatus.SERVICE_UNAVAILABLE);
    }
}