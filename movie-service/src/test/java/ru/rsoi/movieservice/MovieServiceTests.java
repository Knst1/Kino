package ru.rsoi.movieservice;

import org.junit.jupiter.api.Test;
import ru.rsoi.movieservice.domain.Movie;
import ru.rsoi.movieservice.model.IncMovieCountResponse;
import ru.rsoi.movieservice.model.MovieInfo;
import ru.rsoi.movieservice.repository.MovieRepository;
import ru.rsoi.movieservice.service.MovieService;
import ru.rsoi.movieservice.service.MovieServiceImpl;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MovieServiceTests {

	private static final Integer ID = 1;
	private static final String NAME = "MovieName";
	private static final Integer YEAR = 1900;
	private static final String PRODUCER = "MovieProducer";
	private static final Integer COST = 5;
	private static final Integer IMDB = 1545132;
	private static final Integer COUNT = 0;
	private static final Integer DAYS = 5;
	private static final UUID TESTUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
    private static final Integer RENTID = 10;

	@Test
	void getMovieInfoTest() {
		final MovieInfo movieInfo = new MovieInfo(ID, NAME, YEAR, PRODUCER, COST, IMDB, COUNT);
		final Movie movie = new Movie()
				.setId(ID)
				.setMovieUid(TESTUUID)
				.setName(NAME)
				.setYear(YEAR)
				.setProducer(PRODUCER)
				.setCost(COST)
				.setImdb(IMDB)
				.setCount(COUNT);

		MovieRepository movieRepository = mock(MovieRepository.class);
		MovieServiceImpl movieService = new MovieServiceImpl(movieRepository);
		when(movieRepository.findById(ID)).thenReturn(Optional.of(movie));
		assertEquals(movieService.getMovieInfo(ID),  movieInfo);
	}

	@Test
	void getMovieUuidTest() {
		final Movie movie = new Movie()
				.setId(ID)
				.setMovieUid(TESTUUID);

		MovieRepository movieRepository = mock(MovieRepository.class);
		MovieService movieService = new MovieServiceImpl(movieRepository);
		when(movieRepository.findById(ID)).thenReturn(Optional.of(movie));
		assertEquals(movieService.getMovieUuid(ID),  TESTUUID);
	}

	@Test
	void incCountTest() {
		final Movie movie = new Movie()
				.setId(ID)
				.setMovieUid(TESTUUID)
				.setName(NAME)
				.setYear(YEAR)
				.setProducer(PRODUCER)
				.setCost(COST)
				.setImdb(IMDB)
				.setCount(COUNT);

		MovieRepository movieRepository = mock(MovieRepository.class);
		MovieService movieService = new MovieServiceImpl(movieRepository);
		when(movieRepository.findByMovieUid(TESTUUID)).thenReturn(Optional.of(movie));
		when(movieRepository.save(movie)).thenReturn(movie);
		assertEquals(movieService.incCount(TESTUUID, new IncMovieCountResponse(DAYS, RENTID)),  ID);
	}

	@Test
	void getMovieFromUidTest() {
		final MovieInfo movieInfo = new MovieInfo(ID, NAME, YEAR, PRODUCER, COST, IMDB, COUNT);
		final Movie movie = new Movie()
				.setId(ID)
				.setMovieUid(TESTUUID)
				.setName(NAME)
				.setYear(YEAR)
				.setProducer(PRODUCER)
				.setCost(COST)
				.setImdb(IMDB)
				.setCount(COUNT);

		MovieRepository movieRepository = mock(MovieRepository.class);
		MovieService movieService = new MovieServiceImpl(movieRepository);
		when(movieRepository.findByMovieUid(TESTUUID)).thenReturn(Optional.of(movie));
		assertEquals(movieService.getMovieFromUid(TESTUUID),  movieInfo);
	}
}
