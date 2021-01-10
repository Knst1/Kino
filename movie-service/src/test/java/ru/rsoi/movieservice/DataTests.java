package ru.rsoi.movieservice;

import org.junit.jupiter.api.Test;
import ru.rsoi.movieservice.domain.Movie;
import ru.rsoi.movieservice.model.MovieInfo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DataTests {

	private static final Integer ID = 1;
	private static final String NAME = "MovieName";
	private static final String NAME1 = "MovieName1";
	private static final Integer YEAR = 1900;
	private static final String PRODUCER = "MovieProducer";
	private static final Integer COST = 5;
	private static final Integer IMDB = 1545132;
	private static final Integer COUNT = 0;
	private static final Integer DAYS = 5;
	private static final UUID TESTUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
	final MovieInfo movieInfo = new MovieInfo(ID, NAME, YEAR, PRODUCER, COST, IMDB, COUNT);
	final MovieInfo movieInfo1 = new MovieInfo(ID, NAME, YEAR, PRODUCER, COST, IMDB, COUNT);
	final MovieInfo movieInfo2 = new MovieInfo(ID, NAME1, YEAR, PRODUCER, COST, IMDB, COUNT);
	final Movie movie = new Movie()
			.setId(ID)
			.setMovieUid(TESTUUID)
			.setName(NAME)
			.setYear(YEAR)
			.setProducer(PRODUCER)
			.setCost(COST)
			.setImdb(IMDB)
			.setCount(COUNT);
	final Movie movie1 = new Movie()
			.setId(ID)
			.setMovieUid(TESTUUID)
			.setName(NAME)
			.setYear(YEAR)
			.setProducer(PRODUCER)
			.setCost(COST)
			.setImdb(IMDB)
			.setCount(COUNT);
	final Movie movie2 = new Movie()
			.setId(ID)
			.setMovieUid(TESTUUID)
			.setName(NAME1)
			.setYear(YEAR)
			.setProducer(PRODUCER)
			.setCost(COST)
			.setImdb(IMDB)
			.setCount(COUNT);

	@Test
	void equalsMovieInfoTest() {
		assert(movieInfo.equals(movieInfo1));
	}

	@Test
	void hashCodeMovieInfoTest() {
		assertEquals(movieInfo.hashCode(),  movieInfo1.hashCode());
	}

	@Test
	void fequalsMovieInfoTest() {
		assertFalse(movieInfo.equals(movieInfo2));
	}

	@Test
	void fhashCodeMovieInfoTest() {
		assertNotEquals(movieInfo.hashCode(),  movieInfo2.hashCode());
	}

	@Test
	void equalsMovieTest() {
		assert(movie.equals(movie1));
	}

	@Test
	void hashCodeMovieTest() {
		assertEquals(movie.hashCode(),  movie1.hashCode());
	}

	@Test
	void fequalsMovieTest() {
		assertFalse(movie.equals(movie2));
	}

	@Test
	void fhashCodeMovieTest() {
		assertNotEquals(movie.hashCode(),  movie2.hashCode());
	}
}
