package ru.rsoi.movieservice.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.rsoi.movieservice.domain.Movie;
import ru.rsoi.movieservice.model.IncMovieCountResponse;
import ru.rsoi.movieservice.model.MovieInfo;
import ru.rsoi.movieservice.repository.MovieRepository;
import javax.annotation.Nonnull;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class MovieServiceImpl implements MovieService {
    private static final Logger logger = getLogger(MovieServiceImpl.class);

    private final Integer BUFFER_CAPACITY = 5000;
    private final MovieRepository movieRepository;
    private List<Integer> ringBuffer;
    private Integer label;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        ringBuffer = new ArrayList<>(Collections.nCopies(BUFFER_CAPACITY, 0));
        label = 0;
    }

    @Nonnull
    @Override
    public MovieInfo getMovieInfo(@Nonnull Integer id) {
        logger.info("Чтение фильма с ID {}", id.toString());
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty())
            return new MovieInfo();
        return movie.map(this::buildMovieInfo).get();
    }

    @Override
    public UUID getMovieUuid(@Nonnull Integer id) {
        logger.info("Чтение UUID фильма с ID {}", id.toString());
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty())
            return null;
        return movie.get().getMovieUid();
    }

    @Nonnull
    @Override
    public Integer incCount(@Nonnull UUID uuid, @Nonnull IncMovieCountResponse response) {
        logger.info("Увеличение дней аренды фильма с UUID {} на {}", uuid.toString(), response.getDays().toString());
        Optional<Movie> optionalMovie = movieRepository.findByMovieUid(uuid);
        if (optionalMovie.isEmpty())
            return -1;
        Movie movie = optionalMovie.get();
        if (ringBuffer.contains(response.getRentId()))
            return movie.getId();
        ringBuffer.set(label, response.getRentId());
        label = (label + 1) % BUFFER_CAPACITY;
        movie.setCount(movie.getCount() + response.getDays());
        movieRepository.save(movie);
        return movie.getId();
    }

    @Nonnull
    @Override
    public MovieInfo getMovieFromUid(@Nonnull UUID uuid) {
        logger.info("Чтение фильма по его UUID {}", uuid.toString());
        Optional<Movie> optionalMovie = movieRepository.findByMovieUid(uuid);
        if (optionalMovie.isEmpty())
            return new MovieInfo();
        return buildMovieInfo(optionalMovie.get());
    }

    @Nonnull
    @Override
    public List<MovieInfo> getMovies(@Nonnull Integer page, @Nonnull Integer size) {
        logger.info("Чтение всех фильмов. Пагинация: страница {}, элементов {}.",
                page.toString(), size.toString());
        List<MovieInfo> movieInfos= new ArrayList<>();
        if (size<1 || size>1000)
            size = 21;
        List<Movie> movies = movieRepository.findAll(PageRequest.of(Math.max(0, page-1), size, Sort.by("id").descending())).toList();
        for (Movie movie:movies) {
            movieInfos.add(buildMovieInfo(movie));
        }
        return movieInfos;
    }

    @Nonnull
    private MovieInfo buildMovieInfo(@Nonnull Movie movie) {
        return new MovieInfo()
                .setId(movie.getId())
                .setName(movie.getName())
                .setYear(movie.getYear())
                .setProducer(movie.getProducer())
                .setCost(movie.getCost())
                .setImdb(movie.getImdb())
                .setCount(movie.getCount());
    }
}
