package ru.rsoi.cinemaservice;

import org.junit.jupiter.api.Test;
import ru.rsoi.cinemaservice.domain.Cinema;
import ru.rsoi.cinemaservice.model.CinemaInfo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DataTests {

	private static final Integer ID = 19;
	private static final UUID CINEMAUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
	private static final UUID CINEMAUUID1 = UUID.fromString("123e4567-e89b-12d3-a456-426655440001");
	private static final String NAME = "NAME";
	private static final String CHIEF = "CHIEF";
	private static final String OWNER = "OWNER";
	private static final String PHONE = "+79999999999";
	private static final Integer COUNT = 0;
	private static final String ADDRESS = "ADDRESS";

	final CinemaInfo cinemaInfo = new CinemaInfo(CINEMAUUID, NAME, CHIEF, OWNER, PHONE, COUNT, ADDRESS);
	final CinemaInfo cinemaInfo1 = new CinemaInfo(CINEMAUUID, NAME, CHIEF, OWNER, PHONE, COUNT, ADDRESS);
	final CinemaInfo cinemaInfo2 = new CinemaInfo(CINEMAUUID1, NAME, CHIEF, OWNER, PHONE, COUNT, ADDRESS);
	final Cinema cinema = new Cinema()
			.setId(ID)
			.setCinemaUid(CINEMAUUID)
			.setName(NAME)
			.setChief(CHIEF)
			.setOwner(OWNER)
			.setPhone(PHONE)
			.setCount(COUNT)
			.setAddress(ADDRESS);
	final Cinema cinema1 = new Cinema()
			.setId(ID)
			.setCinemaUid(CINEMAUUID)
			.setName(NAME)
			.setChief(CHIEF)
			.setOwner(OWNER)
			.setPhone(PHONE)
			.setCount(COUNT)
			.setAddress(ADDRESS);
	final Cinema cinema2 = new Cinema()
			.setId(ID)
			.setCinemaUid(CINEMAUUID1)
			.setName(NAME)
			.setChief(CHIEF)
			.setOwner(OWNER)
			.setPhone(PHONE)
			.setCount(COUNT)
			.setAddress(ADDRESS);

	@Test
	void equalsCinemaInfoTest() {
		assert(cinemaInfo.equals(cinemaInfo1));
	}

	@Test
	void hashCodeCinemaInfoTest() {
		assertEquals(cinemaInfo.hashCode(),  cinemaInfo1.hashCode());
	}

	@Test
	void fequalsCinemaInfoTest() {
		assertFalse(cinemaInfo.equals(cinemaInfo2));
	}

	@Test
	void fhashCodeCinemaInfoTest() {
		assertNotEquals(cinemaInfo.hashCode(),  cinemaInfo2.hashCode());
	}

	@Test
	void equalsCinemaTest() {
		assert(cinema.equals(cinema1));
	}

	@Test
	void hashCodeCinemaTest() {
		assertEquals(cinema.hashCode(),  cinema1.hashCode());
	}

	@Test
	void fequalsCinemaTest() {
		assertFalse(cinema.equals(cinema2));
	}

	@Test
	void fhashCodeCinemaTest() {
		assertNotEquals(cinema.hashCode(),  cinema2.hashCode());
	}
}
