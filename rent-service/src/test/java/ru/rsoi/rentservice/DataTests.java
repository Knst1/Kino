package ru.rsoi.rentservice;

import org.junit.jupiter.api.Test;
import ru.rsoi.rentservice.domain.Rent;
import ru.rsoi.rentservice.model.RentInfo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DataTests {

	private static final Integer ID = 19;
	private static final Integer ID2 = 2;
	private static final UUID CINEMAUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
	private static final UUID MOVIEUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440002");
	private static final UUID MOVIEUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-426655440003");
	private static final Integer DURATION = 5;
	private static final Integer PRICE = 5;

	final RentInfo rentInfo = new RentInfo(ID, CINEMAUUID, MOVIEUUID, DURATION, PRICE, true);
	final RentInfo rentInfo1 = new RentInfo(ID, CINEMAUUID, MOVIEUUID, DURATION, PRICE, true);
	final RentInfo rentInfo2 = new RentInfo(ID2, CINEMAUUID, MOVIEUUID2, DURATION, PRICE, false);
	final Rent rent = new Rent()
			.setId(ID)
			.setCinemaUid(CINEMAUUID)
			.setMovieUid(CINEMAUUID)
			.setDuration(DURATION)
			.setPrice(PRICE)
			.setConfirmed(true);
	final Rent rent1 = new Rent()
			.setId(ID)
			.setCinemaUid(CINEMAUUID)
			.setMovieUid(CINEMAUUID)
			.setDuration(DURATION)
			.setPrice(PRICE)
			.setConfirmed(true);
	final Rent rent2 = new Rent()
			.setId(ID2)
			.setCinemaUid(CINEMAUUID)
			.setMovieUid(CINEMAUUID)
			.setDuration(DURATION)
			.setPrice(PRICE)
			.setConfirmed(false);

	@Test
	void equalsRentInfoTest() {
		assert(rentInfo.equals(rentInfo1));
	}

	@Test
	void hashCodeRentInfoTest() {
		assertEquals(rentInfo.hashCode(),  rentInfo1.hashCode());
	}

	@Test
	void fequalsRentInfoTest() {
		assertFalse(rentInfo.equals(rentInfo2));
	}

	@Test
	void fhashCodeRentInfoTest() {
		assertNotEquals(rentInfo.hashCode(),  rentInfo2.hashCode());
	}

	@Test
	void equalsRentTest() {
		assert(rent.equals(rent1));
	}

	@Test
	void hashCodeRentTest() {
		assertEquals(rent.hashCode(),  rent1.hashCode());
	}

	@Test
	void fequalsRentTest() {
		assertFalse(rent.equals(rent2));
	}

	@Test
	void fhashCodeRentTest() {
		assertNotEquals(rent.hashCode(),  rent2.hashCode());
	}
}
